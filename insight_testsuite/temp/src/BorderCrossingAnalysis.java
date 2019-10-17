import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BorderCrossingAnalysis {

//  _FILE_INPUT : Input file for Border Crossign Data

    static final String _FILE_INPUT = "input/Border_Crossing_Entry_Data.csv";

    /**
     * dataCacheMap - Hashmap<K,V>
     * K - Key combination of < Border+Date+Measure>
     * V - Data Cache consists of all required information to generate report
     */
    static private HashMap<String, DataCache> dataCacheMap = new HashMap<>();

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


//      Read Data from input file and load it into in-memory Cache
        readDataFromFile();

//      Generate Report and Load in in-memory dataCacheMap and generate Average value for final report
        generateAndLoadCache();

//      Sort all rows in DataCache before writing them to file.
        Collection<DataCache> values = dataCacheMap.values();
        List<DataCache> reportValues = new ArrayList(values);

        Collections.sort(reportValues, new DataCache.CacheComparator(
                new DataCache.DateSorter(),
                new DataCache.ValueSorter(),
                new DataCache.BorderSorter(),
                new DataCache.MeasureSorter()).reversed()
        );

//      Generate Report, write all rows to output report file.
        WriteReport fileWriter = new WriteReport();
        fileWriter.writeReportTofile(reportValues);
    }


//  Load all data into DataCache , Calculate average using valueCacheMap and update DataCache.

    static void generateAndLoadCache() {

        Iterator it = dataCacheMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();

            String k = (String) pairs.getKey();
            DataCache tempD = (DataCache) pairs.getValue();
            Long value = tempD.getValue();

            Long totalCrossings = Long.valueOf(0);

            String token[] = k.split("_");
            String[] tk = token[1].split("-");
            int currentMonth = Integer.parseInt(tk[1]);

//          Skip current month by decementing value of month
            int prevMonth = currentMonth--;

            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(token[1], DATE_TIME_FORMATTER);

            /**
             *
             *  Iterate through crossings from all previous months.
             *  Calculate totalCrossings value for all previous months.
             *
             */
            while (currentMonth > 0) {
                date = date.minusMonths(1);
                String d = date.toString();
                String newKey = token[0] + "_" + d + "_" + token[2];

                if (dataCacheMap.get(newKey) != null && dataCacheMap.get(newKey).getValue() != null) {
                    totalCrossings += dataCacheMap.get(newKey).getValue();
                }

                currentMonth--;
            }

//          Calculate average of all previous months for current crossing Type
            double average = (prevMonth - 1 == 0) ? Long.valueOf(0) : (Math.ceil(totalCrossings / (prevMonth - 1)));
            DataCache c = dataCacheMap.get(k);


//          Update dataCacheMap with calculated average of number of crossings
            c.value = value;
            c.average = (long) average;
            dataCacheMap.put(k, c);
        }
    }

    /**
     * Read data from input files and Load it into Cache
     *
     * @throws IOException
     */
    static void readDataFromFile() throws IOException {
        BufferedReader buffReader = new BufferedReader(new FileReader(_FILE_INPUT));
        String csvSplitter = ",";

        /* Read first line to skip processing column names*/
        String inputLine = buffReader.readLine();

        /**
         *  Read all data from input file line by line.
         *  Process and load all rows into dataCacheMap, pupulate number of crossings for each crossing measure
         */
        while ((inputLine = buffReader.readLine()) != null) {

            String[] border_crossing_data = inputLine.split(csvSplitter);

            if (border_crossing_data.length < 7) {
                continue;
            }

            String border = border_crossing_data[3];
            String dateString = border_crossing_data[4];
            String measure = border_crossing_data[5];

            long value = Long.parseLong(border_crossing_data[6]);

            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
            LocalDate date = LocalDate.parse(dateString, DATE_TIME_FORMATTER);
            DataCache data = new DataCache(border, date, measure);

            // Generate dataKey by combining border+date+measure
            String dataKey = border + "_" + date + "_" + measure;



            /**
             *              Load data into dataCacheMap
             * if current dataKey(month + crossing type + border) is present in cache,
             * then update it's total crossing value.
             * Otherwise update cache with new dataKey and data
             */
            if (!dataCacheMap.containsKey(dataKey)) {
                dataCacheMap.put(dataKey, data);
            } else {
                DataCache tempData = dataCacheMap.get(dataKey);
                long v = tempData.getValue() == null ? 0 : tempData.getValue();
                tempData.setValue(v + value);
                dataCacheMap.put(dataKey, tempData);

            }
        }

        buffReader.close();
    }
}
