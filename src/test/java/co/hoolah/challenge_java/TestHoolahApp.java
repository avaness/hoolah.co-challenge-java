package co.hoolah.challenge_java;


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.Test;

public class TestHoolahApp {

  @Test
  public void original_usage_test() throws IOException {
    TxStatistic stats = new HoolahApp()
        .execute(new ByteArrayInputStream(SampleData.CSV.getBytes()), SampleData.FROM_DATE, SampleData.TO_DATE, SampleData.MERCHANT);

    assertEquals(1, stats.transactions);
    assertEquals(59.99, stats.avgValue, 0);
  }

  @Test
  public void check_no_div_by_zero_on_empty_stats() throws IOException {
    TxStatistic stats = new HoolahApp().execute(new ByteArrayInputStream(new byte[]{}), LocalDateTime.MIN, LocalDateTime.MAX, "");

    assertEquals(0, stats.transactions);
    assertEquals(0, stats.avgValue, 0);
  }

  @Test
  public void check_filtering_wrong_merchant_is_empty() throws IOException {
    TxStatistic stats = new HoolahApp().execute(new ByteArrayInputStream(SampleData.CSV.getBytes()), LocalDateTime.MIN, LocalDateTime.MAX, "ABC");

    assertEquals(0, stats.transactions);
    assertEquals(0, stats.avgValue, 0);
  }
}
