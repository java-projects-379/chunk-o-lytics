package com.log.validator;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log.response.ValidationResult;

@Component
@ConfigurationProperties
public class LogValidator {

	private static final String LOG_REGEX = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
			+ " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

	private final Pattern pattern = Pattern.compile(LOG_REGEX);

	@Value("${in}")
	private String inputFile;

	@Value("${out}")
	private String outputFile;

	@Value("${max-client-ips:10}")
	private int maxClientIPs;

	@Value("${max-paths:10}")
	private int maxPaths;

	public void validateLog() throws IOException {
		int successCounter = 0;
		int failureCounter = 0;
		Map<String, Integer> clientIPCounterMapping = new HashMap<>();
		Map<String, Double> pathAvgResponseTimeMapping = new HashMap<>();
		try (LineIterator it = FileUtils.lineIterator(new File(inputFile), "UTF-8")) {

			while (it.hasNext()) {
				String line = it.nextLine();
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					String clientIP = matcher.group(1);
					// Integer responseCode = Integer.valueOf(matcher.group(8));
					String path = matcher.group(6);
					Double responseTime = Double.valueOf(matcher.group(9));

					//For Client IPs
					if (clientIPCounterMapping.containsKey(clientIP)) {
						clientIPCounterMapping.put(clientIP, clientIPCounterMapping.get(clientIP) + 1);
					} else {
						clientIPCounterMapping.put(clientIP, 1);
					}

					// For path average response
					if (pathAvgResponseTimeMapping.containsKey(path)) {
						pathAvgResponseTimeMapping.put(path, (pathAvgResponseTimeMapping.get(path) + responseTime));
					} else {
						int indexString = path.indexOf('?');
						String finalPath;
						if (indexString == -1) {
							pathAvgResponseTimeMapping.put(path, responseTime);
						} else {
							finalPath = path.substring(0, indexString);
							if (pathAvgResponseTimeMapping.containsKey(finalPath)) {
								pathAvgResponseTimeMapping.put(finalPath,
										(pathAvgResponseTimeMapping.get(finalPath) + responseTime));
							} else {
								pathAvgResponseTimeMapping.put(finalPath, responseTime);
							}
						}
					}
					
					//Top Client IPs
					clientIPCounterMapping =clientIPCounterMapping.entrySet()
							  .stream()
							  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(maxClientIPs)
							  .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
						                LinkedHashMap::new));
					
					//Top Path Avg Seconds
					pathAvgResponseTimeMapping =pathAvgResponseTimeMapping.entrySet()
							  .stream()
							  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(maxPaths)
							  .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue() / 1000, (e1, e2) -> e2,
						                LinkedHashMap::new));
					successCounter++;
				} else {
					failureCounter++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.writeValue(new File(outputFile), new ValidationResult(successCounter + failureCounter,
				successCounter, failureCounter, clientIPCounterMapping, pathAvgResponseTimeMapping));

	}
}