package model;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.Task.State.COMPLETED;
import static model.Task.State.ONGOING;
import static model.Task.State.PENDING;

public class Task implements Serializable {
    private String name;
    private String description;
    List<String> tags = new ArrayList<String>();
    List<String> lists = new ArrayList<String>();
    private Date creation_date = null;
    private Date completation_date;
    private static ArrayList<String> allTags = new ArrayList<>();
    private static ArrayList<String> allLists = new ArrayList<>();


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


    public Task(String n, String d, Date date, char p, ArrayList<String> t, ArrayList<String> l) {
        name = n;
        description = d;
        if (date != null)
            creation_date = date;
        if (p != ' ')
            priority = new Priority(Priority.fromCharToInt(p));
        if (t.size() != 0) {
            tags = t;
            addAllTags(t);
        }
        if (l.size() != 0) {
            lists = l;
            addAllLists(l);
        }
        uncompleteTask();
    }

    public Task(String text) throws ParseException {
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
            priority = new Priority(Priority.fromCharToInt(text.charAt(1))); //letter is in second position
            text = text.substring(4); //4 is length of "(A) "
        }
        else
            priority = new Priority(0);

        //completation date
        if (state == COMPLETED){
            completation_date = ((SimpleDateFormat) formatter).parse(text.split("\\s")[0]);
            text = text.substring(text.split("\\s")[0].length() + 1);
        }

        //optional: creation date
        try{
            creation_date = ((SimpleDateFormat) formatter).parse(text.split("\\s")[0]);
            text = text.substring(text.split("\\s")[0].length() + 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //tags
        String regex = "@\\s*(\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            String tagText = (text.substring(matcher.start() + 1, matcher.end()));
            tags.add(tagText);
            ArrayList<String> addTags = new ArrayList<String>();
            addTags.add(tagText);
            addAllTags(addTags);
        }
        text = text.replaceAll(regex, "");
        text = text.replaceAll("\\s+"," ");


        //lists
        regex = "\\+\\s*(\\w+)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(text);
        while (matcher.find()){
            String listText  = text.substring(matcher.start() + 1, matcher.end());
            lists.add(listText);
            ArrayList<String> addLists = new ArrayList<String>();
            addLists.add(listText);
            addAllLists(addLists);
        }
        text = text.replaceAll(regex, "");
        text = text.replaceAll("\\s+"," ");
        //task name and description
        name = text.split(" ::")[0];
        //name = text;
        if (text.split("::").length > 1)
            description = text.substring(text.split(" ::")[0].length() + 3);
        else
            description = "";
    }

    private String getTextTask(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String text = "";
        if (state == COMPLETED)
            text = "x ";

        if (priority.getValue() > 0)
            text += "(" + priority.getCharValue() + ") ";

        if (state == COMPLETED)
            text += ((SimpleDateFormat) formatter).format(completation_date) + " ";

        if (creation_date != null)
            text += ((SimpleDateFormat) formatter).format(creation_date) + " ";

        text += name;

        if (description.length() != 0) {
            text += " ::" + description;
        }

        Iterator i = tags.iterator();
        while (i.hasNext())
            text += (" @" + i.next());


        i = lists.iterator();
        while (i.hasNext())
            text += (" +" + i.next());
        text = text.replaceAll("\\s+"," ");

        return text;
    }

    public void addTaskInFile(Context context){
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
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

    public static ArrayList<Task> getSavedTasks(Context context, Context activity){
        ArrayList<Task> tasks = new ArrayList<Task>();
        File file = new File(context.getFilesDir(), "todo.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

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

    public void updateTaskInFile(Context context){
        File file = new File(context.getFilesDir(), "todo.txt");
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = fileIn.readLine()) != null) {
                if (line.equals(textdef)){
                    line = getTextTask();
                    textdef = line;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            fileIn.close();


            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeTaskInFile(Context context){
        //Copy all file except interested line
        File file = new File(context.getFilesDir(), "todo.txt");
        File tmp = new File(context.getFilesDir(), "tmp.txt");
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            BufferedWriter fileTmp = new BufferedWriter(new FileWriter(tmp));
            String line;

            while ((line = fileIn.readLine()) != null) {
                if (line.equals(textdef)) {
                    continue;
                }
                fileTmp.write(line + System.getProperty("line.separator"));
            }
            fileTmp.close();
            fileIn.close();
            tmp.renameTo(file);
        }
        catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    public String getName(){
        return name;
    }

    public boolean isComplete(){
        return (state == COMPLETED);
    }

    public void completeTask(){
        state = COMPLETED;
        completation_date = getCurrentDate();
    }

    public void uncompleteTask(){
        //TODO: consider the day?
        Date now = getCurrentDate();
        if (creation_date == null || creation_date.after(now))
            state = ONGOING;
        else
            state = PENDING;
    }

    private void addAllTags(ArrayList<String> newTags){
        for(int i = 0; i < newTags.size(); i++){
            if (!allTags.contains(newTags.get(i)))
                allTags.add(newTags.get(i));
        }
    }

    private void addAllLists(ArrayList<String> newLists){
        for(int i = 0; i < newLists.size(); i++){
            if (!allLists.contains(newLists.get(i)))
                allLists.add(newLists.get(i));
        }
    }

    public static ArrayList<String> getAllTags(){
        return allTags;
    }

    public static ArrayList<String> getAllLists(){
        return allLists;
    }

    public Priority getPriority(){
        return priority;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public List<String> getLists() {
        return lists;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public String getTextdef() {
        return textdef;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setPriority(char priority) {
        this.priority = new Priority(Priority.fromCharToInt(priority));
    }

    public void setName(String name) {
        this.name = name;
    }
}