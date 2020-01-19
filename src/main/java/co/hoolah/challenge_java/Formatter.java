package co.hoolah.challenge_java;

import java.time.format.DateTimeFormatter;

class Formatter {
  static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
  static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
}
