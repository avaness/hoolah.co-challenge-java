package co.hoolah.challenge_java;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.apache.commons.csv.CSVRecord;

/**
 * Simplest logic for Transaction fields from CSV without oany overhead,
 * can be easily extracted to rich POJO on later optimisation demands
 */
class TxParser {

  private static final String REVERSAL_TYPE = "REVERSAL";

  static String parseId(CSVRecord record) {
    return record.get(0).strip();
  }

  static LocalDateTime parseDate(CSVRecord record) {
    return LocalDateTime.parse(record.get(1).strip(), Formatter.DATE_TIME);
  }

  static BigDecimal parseAmount(CSVRecord record) {
    return new BigDecimal(record.get(2).strip());
  }

  static String parseMerchant(CSVRecord record) {
    return record.get(3).strip();
  }

  private static String parseType(CSVRecord record) {
    return record.get(4).strip();
  }

  static boolean isReversal(CSVRecord record) {
    return REVERSAL_TYPE.equals(parseType(record));
  }

  static String parseRelatedTxId(CSVRecord record) {
    return record.get(5).strip();
  }
}
