package dataModel;

import java.time.LocalDate;

public class ToDoItem {
    private String shortDescription;
    private String details;
    private LocalDate date;

    public ToDoItem(String shortDescription, String details, LocalDate date) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.date=date;
    }
    @Override
    public String toString(){
        return shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}

