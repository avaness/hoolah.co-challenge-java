# hoolah.co-challenge-java

## Scope
- pure CLI
- input csv file given in stdin or by `--file` option

## Out of scope
- skipping optimisation since we don't have expectations constraints - amounts as bigdecimals, 
processing whole stream, no indexing and preprocessing    
- no interactive console mode
- no input charset cli option, UTF-8 always as default. Charset is still manageable by by JVM option `-Dfile.encoding`
- no floating point fine tuning optimization, using BigDecimals instead

## Build

### Prerequisites
- JDK 11+
- maven 3.3+

# Build and Run
Please use `bash` for multiline commands below
```bash
mvn clean package
cd target

#show usage info
java -jar challenge.java-0.1-SNAPSHOT-jar-with-dependencies.jar 

#running using stdin pipe
java -jar challenge.java-0.1-SNAPSHOT-jar-with-dependencies.jar --fromDate "20/08/2018 12:00:00" \
--toDate "20/08/2018 13:00:00" --merchant Kwik-E-Mart < ../src/test/resources/co/hoolah/challenge_java/sample.csv

#running with test sample.csv from src/test/resources
java -jar challenge.java-0.1-SNAPSHOT-jar-with-dependencies.jar --fromDate "20/08/2018 12:00:00" \
--toDate "20/08/2018 13:00:00" --merchant Kwik-E-Mart -f ../src/test/resources/co/hoolah/challenge_java/sample.csv

#running using heredoc
java -jar challenge.java-0.1-SNAPSHOT-jar-with-dependencies.jar --fromDate "20/08/2018 12:00:00" \
--toDate "20/08/2018 13:00:00" --merchant Kwik-E-Mart <<EOF 
ID, Date, Amount, Merchant, Type, Related Transaction 
WLMFRDGD, 20/08/2018 12:45:33, 59.99, Kwik-E-Mart, PAYMENT, 
YGXKOEIA, 20/08/2018 12:46:17, 10.95, Kwik-E-Mart, PAYMENT, 
LFVCTEYM, 20/08/2018 12:50:02, 5.00, MacLaren, PAYMENT, 
SUOVOISP, 20/08/2018 13:12:22, 5.00, Kwik-E-Mart, PAYMENT, 
AKNBVHMN, 20/08/2018 13:14:11, 10.95, Kwik-E-Mart, REVERSAL, YGXKOEIA 
JYAPKZFZ, 20/08/2018 14:07:10, 99.50, MacLaren, PAYMENT, 
EOF
```
