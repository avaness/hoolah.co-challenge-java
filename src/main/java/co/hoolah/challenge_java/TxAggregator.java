package co.hoolah.challenge_java;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.csv.CSVRecord;

class TxAggregator {
  private BigDecimal avgValue = new BigDecimal(0);
  private HashSet<String> transactions = new HashSet<>();
  private List<CSVRecord> nonMatchedReversals = new ArrayList<>();

  void addTx(CSVRecord item) {
    if (!TxParser.isReversal(item)) {
      avgValue = avgValue.add(TxParser.parseAmount(item));
      transactions.add(TxParser.parseId(item));
    }
    else {
      addReversal(item);
    }
  }

  private void addReversal(CSVRecord item) {
    if (transactions.contains(TxParser.parseRelatedTxId(item))) {
      transactions.remove(TxParser.parseRelatedTxId(item));
      avgValue = avgValue.subtract(TxParser.parseAmount(item));
    }
    else {
      nonMatchedReversals.add(item);
    }
  }

  TxAggregator combine(TxAggregator agg2) {
    avgValue = avgValue.add(agg2.avgValue);

    agg2.nonMatchedReversals.forEach(t -> addReversal(t));

    transactions.addAll(agg2.transactions);
    return this;
  }

  TxStatistic calcStats() {
    return new TxStatistic(transactions.size(), avgValue.doubleValue()/(transactions.size() >0? transactions.size(): 1));
  }
}
