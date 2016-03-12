import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarGenerator {
    static Calendar current;
    Calendar last;
    SimpleDateFormat dateFormat;
    public CalendarGenerator(int startYear){
        last = new GregorianCalendar();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        current = new GregorianCalendar(startYear,12,0);
    }
    public Calendar generateData() {
        current.add(Calendar.DAY_OF_MONTH, 1);
        if (current.compareTo(last)<0) {
            System.out.println("currentin. " + dateFormat.format(current.getTime()));
            return current;
        }else
            return null;
    }
}
