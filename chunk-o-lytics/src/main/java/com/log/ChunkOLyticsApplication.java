package com.log;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ChunkOLyticsApplication implements ExitCodeGenerator {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(ChunkOLyticsApplication.class, args)));
	}

	@Override
	public int getExitCode() {
		return 0;
	}

	@Bean
	public ExitCodeExceptionMapper exitCodeToexceptionMapper() {
		return exception -> {
			if (exception.getCause() instanceof IllegalArgumentException) {
				return 1;
			}
			if (exception.getCause() instanceof NumberFormatException) {
				return 2;
			}
			if (exception.getCause() instanceof NullPointerException) {
				return 3;
			}
			return 111;
		};
	}

	@Bean
	public CommandLineRunner createException() {
		return args -> {
			if (args.length > 2) {
				String args2=args[2].split("=")[1];
				int maxClientIPs = Integer.parseInt(args2);
				if (maxClientIPs < 0 || maxClientIPs > 10000) {
					throw new IllegalArgumentException();
				}
			}
			if (args.length > 3) {
				String args3=args[3].split("=")[1];
				Integer maxPaths = Integer.parseInt(args3);
				if (maxPaths < 0 || maxPaths > 10000) {
					throw new IllegalArgumentException();
				}
			}
		};
	}

	@Bean
	public ExitCodeEventListener exitEventListener() {
		return new ExitCodeEventListener();
	}

	private static class ExitCodeEventListener {

		@EventListener
		public void exitEvent(ExitCodeEvent event) {
			System.out.println("Application Exit code: " + event.getExitCode());
		}
	}

}
