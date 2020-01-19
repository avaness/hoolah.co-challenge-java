package co.hoolah.challenge_java;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.csv.CSVRecord;

class TxStatistic {
  final int transactions;
  final double avgValue;

  TxStatistic(int transactions, double avgValue) {
    this.transactions = transactions;
    this.avgValue = avgValue;
  }
}
