package co.hoolah.challenge_java;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;
import picocli.CommandLine.TypeConversionException;

/**
 * Main CLI processor and business app runner
 */
@Command(mixinStandardHelpOptions = true, version = "version 0.1", description = "Prints transactions statistic for csv stdin stream")
public class CliProcessor implements Callable<Integer> {

  @Option(names = {"-f", "--file"}, description = ".csv fileStream with transactions", converter = FileStreamConverter.class)
  InputStream file;

  @Option(names= "--fromDate", required =true, description = "'" + Formatter.DATE_TIME_FORMAT + "' format", converter = TimeConverter.class)
  LocalDateTime from;

  @Option(names = "--toDate", required = true, description = "'" + Formatter.DATE_TIME_FORMAT + "' format", converter = TimeConverter.class)
  LocalDateTime to;

  @Option(names = "--merchant", required = true, description = "merchant name")
  String merchant;

  private HoolahApp hoolahApp;

  CliProcessor(HoolahApp hoolahApp) {
    this.hoolahApp = hoolahApp;
  }

  public Integer call() throws IOException {
    InputStream csvStream = file == null? System.in: file;
    TxStatistic stats = hoolahApp.execute(csvStream, from, to, merchant);
    System.out.println(String
        .format("Number of transactions: %d\nAverage Transaction Value: %.2f\n", stats.transactions,
            stats.avgValue));
    return 0;
  }

  private static class TimeConverter implements ITypeConverter<LocalDateTime> {

    @Override
    public LocalDateTime convert(String value) throws Exception {
      try {
        return LocalDateTime.parse(value, Formatter.DATE_TIME);
      } catch (DateTimeParseException e) {
        throw new TypeConversionException(String.format("bad format %s, should be %s", value, Formatter.DATE_TIME_FORMAT));
      }
    }
  }

  private static class FileStreamConverter implements ITypeConverter<InputStream> {

    @Override
    public InputStream convert(String value) throws Exception {
      try {
        return new BufferedInputStream(new FileInputStream(value));
      } catch (FileNotFoundException e) {
        throw new TypeConversionException(String.format("bad fileStream name '%s'", value));
      }
    }
  }
}
