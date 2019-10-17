import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 */
public class WriteReport {

    /**
     * @param cachedData
     * @throws IOException
     */
    public void writeReportTofile(List<DataCache> cachedData) throws IOException {

//      Columns in final report and report name and path
        final String _FILE_OUTPUT_REPORT = "output/report.csv";
        final String _REPORT_COLUMN_BORDER = "Border,";
        final String _REPORT_COLUMN_DATE = "Date,";
        final String _REPORT_COLUMN_MEASURE = "Measure,";
        final String _REPORT_COLUMN_VALUE = "Value,";
        final String _REPORT_COLUMN_AVERAGE = "Average";

        FileOutputStream out = null;
        out = new FileOutputStream(_FILE_OUTPUT_REPORT);

//      Generate Row that needs to be written to report file
        StringBuilder finalString = new StringBuilder();

        finalString.append(_REPORT_COLUMN_BORDER);
        finalString.append(_REPORT_COLUMN_DATE);
        finalString.append(_REPORT_COLUMN_MEASURE);
        finalString.append(_REPORT_COLUMN_VALUE);
        finalString.append(_REPORT_COLUMN_AVERAGE);


        out.write(finalString.toString().getBytes());
        out.write("\n".getBytes());

//      Write all data from cache to Report file
        for (DataCache row : cachedData) {

            finalString.setLength(0);

            finalString.append(row.border);
            finalString.append(",");
            finalString.append(row.date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + " 12:00:00 AM");
            finalString.append(",");
            finalString.append(row.measure);
            finalString.append(",");
            finalString.append(row.value);
            finalString.append(",");
            finalString.append(row.average);
            out.write(finalString.toString().getBytes());
            out.write("\n".getBytes());
        }

        out.close();
    }
}
