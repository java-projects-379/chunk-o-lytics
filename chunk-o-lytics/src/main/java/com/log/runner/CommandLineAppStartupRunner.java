package com.log.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.log.validator.LogValidator;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	private LogValidator logValidator;

	@Override
	public void run(String... args) throws Exception {
		logValidator.validateLog();

	}
}
