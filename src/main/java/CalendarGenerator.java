import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarGenerator {
    private static Calendar current;
    private Calendar last;
    private SimpleDateFormat dateFormat;
    public CalendarGenerator(int startYear){
        last = new GregorianCalendar();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        current = new GregorianCalendar(startYear,12,0);
    }
    public Calendar generateData() {
        current.add(Calendar.DAY_OF_MONTH, 1);
        if (current.compareTo(last)<0) {
            return current;
        }else
            return null;
    }
}
