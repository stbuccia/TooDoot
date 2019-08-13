package model;
import java.util.Date;

public class DateRange {
    private Date startDate;
    private Date endDate;


    DateRange(Date start, Date end) {
        startDate = start;
        endDate = end;
    }

    public boolean isInRange(Date date) {
        return (date.before(endDate) && date.after(startDate));
    }

    public Date getStartDate(){
        return startDate;
    }

    public Date getEndDate(){
        return endDate;
    }
}