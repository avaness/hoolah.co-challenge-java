package co.hoolah.challenge_java;

import picocli.CommandLine;

public class Main {
  public static void main(String[] args) {
    System.exit(new CommandLine(new CliProcessor(new HoolahApp())).execute(args));
  }
}
