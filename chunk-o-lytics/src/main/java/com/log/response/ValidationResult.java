package com.log.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationResult {


	@JsonProperty("total_number_of_lines_processed")
	private int totalNumberOfLinesProcessed;
	
	@JsonProperty("total_number_of_lines_ok")
	private int totalNumberOfLinesOk;
	
	@JsonProperty("total_number_of_lines_failed")
	private int totalNumberOfLinesFailed;
	
	@JsonProperty("top_client_ips")
	Map<String, Integer> clientIPCounterMapping;
	
	@JsonProperty("top_path_avg_seconds")
	Map<String, Double> pathAvgResponseTimeMapping;
	

	
	public ValidationResult(int totalNumberOfLinesProcessed, int totalNumberOfLinesOk, int totalNumberOfLinesFailed,
			Map<String, Integer> clientIPCounterMapping, Map<String, Double> pathAvgResponseTimeMapping) {
		super();
		this.totalNumberOfLinesProcessed = totalNumberOfLinesProcessed;
		this.totalNumberOfLinesOk = totalNumberOfLinesOk;
		this.totalNumberOfLinesFailed = totalNumberOfLinesFailed;
		this.clientIPCounterMapping = clientIPCounterMapping;
		this.pathAvgResponseTimeMapping = pathAvgResponseTimeMapping;
	}

	public int getTotalNumberOfLinesProcessed() {
		return totalNumberOfLinesProcessed;
	}

	public void setTotalNumberOfLinesProcessed(int totalNumberOfLinesProcessed) {
		this.totalNumberOfLinesProcessed = totalNumberOfLinesProcessed;
	}

	public int getTotalNumberOfLinesOk() {
		return totalNumberOfLinesOk;
	}

	public void setTotalNumberOfLinesOk(int totalNumberOfLinesOk) {
		this.totalNumberOfLinesOk = totalNumberOfLinesOk;
	}

	public int getTotalNumberOfLinesFailed() {
		return totalNumberOfLinesFailed;
	}

	public void setTotalNumberOfLinesFailed(int totalNumberOfLinesFailed) {
		this.totalNumberOfLinesFailed = totalNumberOfLinesFailed;
	}

	public Map<String, Integer> getClientIPCounterMapping() {
		return clientIPCounterMapping;
	}

	public void setClientIPCounterMapping(Map<String, Integer> clientIPCounterMapping) {
		this.clientIPCounterMapping = clientIPCounterMapping;
	}

	public Map<String, Double> getPathAvgResponseTimeMapping() {
		return pathAvgResponseTimeMapping;
	}

	public void setPathAvgResponseTimeMapping(Map<String, Double> pathAvgResponseTimeMapping) {
		this.pathAvgResponseTimeMapping = pathAvgResponseTimeMapping;
	}
	
}
