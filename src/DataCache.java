import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DataCache {

    /**
     * border  - Designates what border was crossed
     * date    - Timestamp indicating month and year of crossing
     * measure - Indicates means, or type, of crossing being measured (e.g., vehicle, equipment, passenger or pedestrian)
     * value   - Number of crossings
     * average - Average of Number of crossings in previous months of this year
     */

    String border;
    LocalDate date;
    String measure;
    Long value;
    Long average;

    /**
     * @param border  Designates what border was crossed
     * @param date    Timestamp indicating month and year of crossing
     * @param measure Indicates means, or type, of crossing being measured
     */
    public DataCache(String border, LocalDate date, String measure, Long value) {
        this.border = border;
        this.date = date;
        this.measure = measure;
        this.value = value;
        this.average = Long.valueOf(0);
    }

    /**
     *
     */
    static class DateSorter implements Comparator<DataCache> {

        @Override
        public int compare(DataCache o1, DataCache o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }

    /**
     *
     */
    static class ValueSorter implements Comparator<DataCache> {

        @Override
        public int compare(DataCache o1, DataCache o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    }

    /**
     *
     */
    static class MeasureSorter implements Comparator<DataCache> {

        @Override
        public int compare(DataCache o1, DataCache o2) {
            return o1.getMeasure().compareTo(o2.getMeasure());
        }
    }

    /**
     *
     */
    static class BorderSorter implements Comparator<DataCache> {

        @Override
        public int compare(DataCache o1, DataCache o2) {
            return o1.getBorder().compareTo(o2.getBorder());
        }
    }

    /**
     *
     */
    public static class CacheComparator implements Comparator<DataCache> {

        private List<Comparator<DataCache>> listComparators;

        /**
         * @param comparators
         */
//
        public CacheComparator(Comparator<DataCache>... comparators) {
            this.listComparators = Arrays.asList(comparators);
        }

        /**
         * @param row1
         * @param row2
         * @return
         */
        @Override
        public int compare(DataCache row1, DataCache row2) {
            for (Comparator<DataCache> comparator : listComparators) {
                int result = comparator.compare(row1, row2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }

    /**
     * @return
     */
    private String getBorder() {
        return border;
    }

    /**
     * @return
     */
    private LocalDate getDate() {
        return date;
    }

    /**
     * @return
     */
    private String getMeasure() {
        return measure;
    }

    /**
     * @return
     */
    public Long getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(Long value) {
        this.value = value;
    }

}
