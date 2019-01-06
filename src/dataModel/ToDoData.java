package dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {

    public ObservableList<ToDoItem> toDoItems;
    private String filename="ToDo.txt";
    private static ToDoData instance=new ToDoData();

    public static ToDoData getInstance(){
        return instance;
    }

    public DateTimeFormatter df=DateTimeFormatter.ofPattern("dd-MM-yyyy");

//    public ToDoData(){
//        toDoItems=FXCollections.observableArrayList();
//
//    }

    public ObservableList<ToDoItem> getToDoItems()
    {
        return toDoItems;
    }

    public void writeToDoItem() throws IOException{

        Path path=Paths.get(filename);
        BufferedWriter bw=Files.newBufferedWriter(path);

        try {
            Iterator<ToDoItem> iter = toDoItems.iterator();
            while (iter.hasNext()) {
                ToDoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDate().format(df)));
                bw.newLine();
            }

        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public void readToDoItem() throws IOException{

        toDoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, df);
                ToDoItem todoItem = new ToDoItem(shortDescription, details, date);
                toDoItems.add(todoItem);
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
    public void deleteToDoItem(ToDoItem item){
        toDoItems.remove(item);
    }
    public void addToDoItem(ToDoItem item){
        toDoItems.add(item);


    }
    public ObservableList<ToDoItem> getToDoItem(){
        return toDoItems;
    }
    public void replaceToDoItem(ToDoItem item,ToDoItem newItem){
        toDoItems.add(toDoItems.indexOf(item),newItem);
        toDoItems.remove(item);
    }

}
