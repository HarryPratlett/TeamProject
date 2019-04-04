package com.myst.networking;

/**
 * Reports error with the server
 */
public class Report {

  public static void behaviour(String message) {
    System.err.println(message);
  }

  public static void error(String message) {
    System.err.println(message);
  }

  public static void errorAndGiveUp(String message) {
    Report.error(message);
    System.exit(1);
  }
}
