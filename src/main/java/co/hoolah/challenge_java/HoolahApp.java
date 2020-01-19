package co.hoolah.challenge_java;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

/**
 * Simplest streaming aggregator of csv transaction entries
 *
 * Streaming processing is always good and can be easily extracted and reused on optimisation opportunities later
 */
class HoolahApp {

  TxStatistic execute(InputStream csv, LocalDateTime fromDate, LocalDateTime toDate, String merchant) throws IOException {

    CSVParser records = new CSVParser(new InputStreamReader(csv), CSVFormat.EXCEL.withFirstRecordAsHeader().withTrim());
    return StreamSupport.stream(records.spliterator(), false)
        .filter(record -> merchant.equals(TxParser.parseMerchant(record)))
        .filter(record -> {
          LocalDateTime txDate = TxParser.parseDate(record);
          return !txDate.isBefore(fromDate) && (!txDate.isAfter(toDate) || TxParser.isReversal(record));
        })
        .collect(Collector.of(
            () -> new TxAggregator(),
            (result, item) -> result.addTx(item),
            (agg1, agg2) -> agg1.combine(agg2),
            result -> result.calcStats(),
            Characteristics.CONCURRENT)
        );
  }

}
