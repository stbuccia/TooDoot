package model;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static model.Task.State.COMPLETED;
import static model.Task.State.ONGOING;
import static model.Task.State.PENDING;

public class Task {
    private String name;
    private String description;
    // list
    List<String> tags = new ArrayList<String>();
    private Date creation_date;
    private Date completation_date;

    private Priority priority;

    public enum State {
        PENDING, ONGOING, COMPLETED
    }
    private State state;
    private String textdef; //string definition in text (contained in todo.txt)


    private static Date getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return date;
    }


    private String deleteInitialSpaces(String s){
        if (s.charAt(0) == ' ')
            s.substring(s.split("\\s+")[0].length());
        return s;
    }

    public Task(String n, String d, Date date, int p) {
        name = n;
        description = d;
        //tags
        creation_date = date;
        priority = new Priority(p);
        uncompleteTask();
    }



    private Task(String text) throws ParseException {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        textdef = text;
        text = text.replaceAll("\\s+"," ");
        //x to indicate if is complete
        if (text.split("\\s")[0].equals("x")) {
            state = COMPLETED;
            text = text.substring(2); //2 is length of "x "
        }
        //(A) letters in bracket indicate priority
        if (text.split("\\s")[0].matches("^\\([A-Z]\\)")) {
            priority = new Priority(Priority.fromCharToInt(text.charAt(2))); //letter is in second position
            text = text.substring(4); //4 is length of "(A)"
        }
        else
            priority = new Priority(0);

        //completation date
        if (state == COMPLETED){
            completation_date = ((SimpleDateFormat) formatter).parse(text.split("\\s")[0]);
            text = text.substring(text.split("\\s")[0].length());

        }
        //optional: creation date
        try{
            creation_date = ((SimpleDateFormat) formatter).parse(text.split("\\s")[0]);
            text = text.substring(text.split("\\s")[0].length());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //task name and description
        //name = text.split(" ::")[0];
        name = text;
        if (text.split("::").length > 1)
            description = text.split(" ::")[1];
    }

    private String getTextTask(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String text = "";
        if (state == COMPLETED)
            text = "x ";
        text += "(" + priority.getCharValue() + ") " + ((SimpleDateFormat) formatter).format(creation_date) + " " + name;
        if (description != "")
            text +=" ::" + description;
        return text;
    }

    public void addTaskInFile(Context context, Context activity){
        //add in description
        textdef = getTextTask();
        String text = textdef + "\n";
        //write the task in the beginning of file
        File file = new File(context.getFilesDir(), "todo.txt");
        try {
            RandomAccessFile f = new RandomAccessFile(file, "rw");
            f.seek(f.length());
            f.write(text.getBytes());
            f.close();
            //Toast.makeText(activity, "Tutto a buon fine", Toast.LENGTH_LONG).show();
        }
        catch(IOException e){
            Toast.makeText(activity, "Qualcosa non va", Toast.LENGTH_LONG).show();
        }

    }

    public static ArrayList<Task> getSavedTasks(Context context, Context activity){
        ArrayList<Task> tasks = new ArrayList<Task>();
        File file = new File(context.getFilesDir(), "todo.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //Toast.makeText(activity, line, Toast.LENGTH_LONG).show();
                try{
                    tasks.add(new Task(line));
                }

                catch (ParseException e){
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } catch(FileNotFoundException e) {
            Toast.makeText(activity, "todo.txt non trovato", Toast.LENGTH_LONG).show();
        }
        return tasks;

    }

    public void getTaskInFile(Context context, Context activity){
        File file = new File(context.getFilesDir(), "todo.txt");
        try {
            Scanner scanner = new Scanner(file);
            String text = getTextTask();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line == text) {
                    Toast.makeText(activity, line, Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(activity, line, Toast.LENGTH_LONG).show();
            }
        } catch(FileNotFoundException e) {
            Toast.makeText(activity, "Todo.txt non trovato", Toast.LENGTH_LONG).show();
        }
    }


    public String getName(){
        return name;
    }

    public void completeTask(){
        state = COMPLETED;
        completation_date = getCurrentDate();
    }

    public void uncompleteTask(){
        //TODO: consider the day
        Date now = getCurrentDate();
        if (creation_date.after(now))
        if (creation_date.after(now))
            state = ONGOING;
        else
            state = PENDING;
    }

}