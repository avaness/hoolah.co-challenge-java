package co.hoolah.challenge_java;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

class SampleData {

  static final String CSV =
      "ID, Date, Amount, Merchant, Type, Related Transaction\n"
          + "WLMFRDGD, 20/08/2018 12:45:33, 59.99, Kwik-E-Mart, PAYMENT,\n"
          + "YGXKOEIA, 20/08/2018 12:46:17, 10.95, Kwik-E-Mart, PAYMENT,\n"
          + "LFVCTEYM, 20/08/2018 12:50:02, 5.00, MacLaren, PAYMENT,\n"
          + "SUOVOISP, 20/08/2018 13:12:22, 5.00, Kwik-E-Mart, PAYMENT,\n"
          + "AKNBVHMN, 20/08/2018 13:14:11, 10.95, Kwik-E-Mart, REVERSAL, YGXKOEIA\n"
          + "JYAPKZFZ, 20/08/2018 14:07:10, 99.50, MacLaren, PAYMENT,";

  static final String MERCHANT = "Kwik-E-Mart";

  static final String FROM_DATE_STRING = "20/08/2018 12:00:00";
  static final String TO_DATE_STRING = "20/08/2018 13:00:00";

  static final LocalDateTime FROM_DATE = LocalDateTime.parse(FROM_DATE_STRING, Formatter.DATE_TIME);
  static final LocalDateTime TO_DATE = LocalDateTime.parse(TO_DATE_STRING, Formatter.DATE_TIME);
}
