package co.hoolah.challenge_java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import picocli.CommandLine;

@RunWith(MockitoJUnitRunner.class)
public class TestCliProcessing {

  @Mock
  HoolahApp mockedApp;

  @Test
  public void cli_parameters_sample() {
    CliProcessor cli = (CliProcessor) new CommandLine(new CliProcessor(mockedApp))
        .parseArgs("--fromDate", SampleData.FROM_DATE_STRING, "--toDate", SampleData.TO_DATE_STRING, "--merchant", SampleData.MERCHANT)
        .commandSpec().userObject();

    assertNull(cli.file);
    assertEquals(SampleData.FROM_DATE, cli.from);
    assertEquals(SampleData.TO_DATE, cli.to);
    assertEquals(SampleData.MERCHANT, cli.merchant);
  }

  @Test
  public void check_help_and_version_args() {
    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();

    int result = new CommandLine(new CliProcessor(mockedApp))
        .setOut(new PrintWriter(stdout))
        .setErr(new PrintWriter(stderr))
        .execute("-h");

    assertEquals(0, result);
    assertFalse(stdout.toString().isBlank());
    assertTrue(stderr.toString().isBlank());

    System.out.print(stdout.toString());
    System.err.print(stderr.toString());
  }

  @Test
  public void missing_required_params_should_fail() {
    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();

    int result = new CommandLine(new CliProcessor(mockedApp))
        .setOut(new PrintWriter(stdout))
        .setErr(new PrintWriter(stderr))
        .execute("--fromDate", SampleData.FROM_DATE_STRING, "--toDate", SampleData.TO_DATE_STRING);

    assertEquals(2, result);
    assertTrue(stdout.toString().isBlank());
    assertFalse(stderr.toString().isBlank());

    stderr.reset();
    stdout.reset();
    result = new CommandLine(new CliProcessor(mockedApp))
        .setOut(new PrintWriter(stdout))
        .setErr(new PrintWriter(stderr))
        .execute("--toDate", SampleData.TO_DATE_STRING, "--merchant", SampleData.MERCHANT);

    assertEquals(2, result);
    assertTrue(stdout.toString().isBlank());
    assertFalse(stderr.toString().isBlank());

    stderr.reset();
    stdout.reset();
    result = new CommandLine(new CliProcessor(mockedApp))
        .setOut(new PrintWriter(stdout))
        .setErr(new PrintWriter(stderr))
        .execute("--fromDate", SampleData.FROM_DATE_STRING, "--merchant", SampleData.MERCHANT);

    assertEquals(2, result);
    assertTrue(stdout.toString().isBlank());
    assertFalse(stderr.toString().isBlank());
  }

  @Test
  public void missing_param_value_should_fail() {
    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();

    int result = new CommandLine(new CliProcessor(mockedApp))
        .setOut(new PrintWriter(stdout))
        .setErr(new PrintWriter(stderr))
        .execute("--fromDate", SampleData.FROM_DATE_STRING, "--toDate", SampleData.TO_DATE_STRING, "--merchant");

    assertEquals(2, result);
    assertTrue(stdout.toString().isBlank());
    assertFalse(stderr.toString().isBlank());
  }

  @Test
  public void bad_time_format_should_fail() {
    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();

    int result = new CommandLine(new CliProcessor(mockedApp))
        .setOut(new PrintWriter(stdout))
        .setErr(new PrintWriter(stderr))
        .execute("--fromDate", "12345", "--toDate", SampleData.TO_DATE_STRING, "--merchant", SampleData.MERCHANT);

    assertEquals(2, result);
    assertTrue(stdout.toString().isBlank());
    assertFalse(stderr.toString().isBlank());
  }

  @Test
  public void stdin_should_be_considered_as_csv_input() throws IOException {
    InputStream stdin = System.in;
    PrintStream stdout = System.out;
    System.setIn(new ByteArrayInputStream(SampleData.CSV.getBytes()));
    System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    try {

      when(mockedApp.execute(any(InputStream.class), any(LocalDateTime.class), any(LocalDateTime.class), anyString()))
          .thenReturn(new TxStatistic(0, 0));

      int result  = new CommandLine(new CliProcessor(mockedApp))
          .execute("--fromDate", SampleData.FROM_DATE_STRING, "--toDate", SampleData.TO_DATE_STRING, "--merchant", SampleData.MERCHANT);

      assertEquals(0, result);

      var captor = ArgumentCaptor.forClass(InputStream.class);
      Mockito.verify(mockedApp, times(1)).execute(captor.capture(), any(), any(), anyString());

      assertEquals(1, captor.getAllValues().size());
      assertEquals(SampleData.CSV, new String(captor.getValue().readAllBytes()));

    } finally {
      System.setIn(stdin);
      System.setOut(stdout);
    }
  }

  @Test
  public void file_should_be_considered_as_csv_input() throws IOException, URISyntaxException {
    PrintStream stdout = System.out;
    System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    try {
      when(mockedApp.execute(any(InputStream.class), any(LocalDateTime.class), any(LocalDateTime.class), anyString()))
          .thenReturn(new TxStatistic(0, 0));

      Path fileUri = Paths.get(Objects.requireNonNull(getClass().getResource("sample.csv")).toURI());

      int result  = new CommandLine(new CliProcessor(mockedApp))
          .execute("--fromDate", SampleData.FROM_DATE_STRING, "--toDate", SampleData.TO_DATE_STRING, "--merchant", SampleData.MERCHANT, "--file", fileUri.toString());

      assertEquals(0, result);

      var captor = ArgumentCaptor.forClass(InputStream.class);
      Mockito.verify(mockedApp, times(1)).execute(captor.capture(), any(), any(), anyString());

      assertEquals(1, captor.getAllValues().size());
      assertEquals(new String(Files.readAllBytes(fileUri)), new String(captor.getValue().readAllBytes()));
    } finally {
      System.setOut(stdout);
    }
  }
}
