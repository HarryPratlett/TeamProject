package com.myst.junit;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
	
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(JUnitTests.class);
		System.out.println((result.getRunCount()-result.getFailureCount()) + "/" +result.getRunCount() + " Tests Passed");
		
		for(Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
	}

}
