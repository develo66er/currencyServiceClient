import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarGenerator {
    private static Calendar current;
    private Calendar last;
    public CalendarGenerator(int numOfMonthAgo){
        last = new GregorianCalendar();
        current = new GregorianCalendar();
        for(int i = 0;i<numOfMonthAgo;i++)
        current.add(Calendar.MONTH,-1);
    }
    public CalendarGenerator(Long startDate, Long endDate){
        Date d = new Date(endDate);
        last = new GregorianCalendar();
        last.setTime(d);
        d.setTime(startDate);
        current = new GregorianCalendar();
        current.setTime(d);
        current.add(Calendar.DAY_OF_MONTH,-1);

    }

    public Calendar generateData() {
        current.add(Calendar.DAY_OF_MONTH, 1);
        if (current.compareTo(last)<=0) {
            return current;
        }else
            return null;
    }
}
