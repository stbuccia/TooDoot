package model;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static model.Task.State.COMPLETED;
import static model.Task.State.ONGOING;
import static model.Task.State.PENDING;

public class Task {
    private String name;
    private String description;
    // list
    List<String> tags = new ArrayList<String>();
    private DateRange dateRange;

    private Priority priority;

    public enum State {
        PENDING, ONGOING, COMPLETED
    }
    private State state;


    private static Date getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return date;
    }

    public Task(String n, String d, Date startDate, Date endDate, int p) {
        name = n;
        description = d;
        //tags
        dateRange = new DateRange(startDate, endDate);
        priority = new Priority(p);
        uncompleteTask();
    }

    public void addTaskInFile(Context context, Context activity){
        //add in description
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String text;
        text = name + " " + description + " " + formatter.format(dateRange.getStartDate()) + " " + formatter.format(dateRange.getEndDate()) + " " + priority + "\n";
        if (state == COMPLETED)
            text += (" " + "X");
        //write the task in the beginning of file
        File file = new File(context.getFilesDir(), "todo.txt");
        try {
            RandomAccessFile f = new RandomAccessFile(file, "rw");
            f.seek(0);
            f.write(text.getBytes());
            f.close();
            Toast.makeText(activity, "Tutto a buon fine", Toast.LENGTH_LONG).show();
        }
        catch(IOException e){
            Toast.makeText(activity, "Qualcosa non va", Toast.LENGTH_LONG).show();
        }

    }
    public String getName(){
        return name;
    }

    public void completeTask(){
        state = COMPLETED;
    }

    public void uncompleteTask(){
        Date date = getCurrentDate();
        if (dateRange.isInRange(date))
            state = ONGOING;
        else
            state = PENDING;
    }

}
