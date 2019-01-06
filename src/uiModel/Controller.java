package uiModel;

import dataModel.ToDoData;
import dataModel.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {

    private List<ToDoItem> toDoItems;


    @FXML
    private ListView<ToDoItem> listView;


    @FXML
    private TextArea textArea;

    @FXML
    private Label dueDate;

    @FXML
    private BorderPane border;

    @FXML
    private ContextMenu menu;

    @FXML
    public void initialize() {



//        toDoData.addToDoItem(new ToDoItem("Meet Shivam", "Meet Shivam at PARADISE RESORT ."
//                , LocalDate.of(2018 ,Month.OCTOBER, 22)));
//        toDoData.addToDoItem(new ToDoItem("Drive Rolls Royce", "Drive to PARADISE RESORT ."
//                , LocalDate.of(2018 ,Month.OCTOBER, 25)));
//
//        try {
//            toDoData.readToDoItem();
//        } catch (IOException e) {
//            System.out.println(
//                    "Error writing File .!"
//            );
//        }
//
//        //toDoItems.add(new ToDoItem("Meet Shivam", "Meet Shivam at PARADISE RESORT ."
//          //              , LocalDate.of(2018 ,Month.OCTOBER, 22)));
//
//
//        System.out.println(toDoData.getToDoItem());

//        textArea.setText(toDoData.getToDoItem().get(0).getDetails());
//        dueDate.setText(toDoData.getToDoItem().get(0).getDate().toString());


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
                if (newValue!=null){
                    ToDoItem item=listView.getSelectionModel().getSelectedItem();
                    textArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    dueDate.setText(df.format(item.getDate()));
                }
            }
        });

        listView.setItems(ToDoData.getInstance().getToDoItem());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectFirst();



        menu = new ContextMenu();
        MenuItem editMenuItem=new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");


        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItem item=listView.getSelectionModel().getSelectedItem();
                deleteToDoItem(item);
            }
        });

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItem item=listView.getSelectionModel().getSelectedItem();
                editToDoItem(item);
            }
        });

        menu.getItems().addAll(editMenuItem,deleteMenuItem);

        listView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {

                ListCell<ToDoItem> cell =new ListCell<ToDoItem>(){
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty){
                            setText(null);
                        }else {
                            setText(item.getShortDescription());
                            setTextFill(Color.BLACK);
                            if (item.getDate().equals(LocalDate.now())){
                                setText(item.getShortDescription()+" -- Due today ! ");
                                setTextFill(Color.RED);
                            }
                        }

                    }
                };

                cell.emptyProperty().addListener(
                        (obs,wasEmpty ,nowEmpty)->{
                            if (nowEmpty){
                                cell.setContextMenu(null);
                            }else {
                                cell.setContextMenu(menu);
                            }
                        }
                );

                return cell;
            }
        });


    }

    public void showNewItemDialog(){
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(border.getScene().getWindow());

        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogPane.fxml"));

        dialog.setTitle("Add New Todo Item");
        dialog.setHeaderText("Use this dialog to add a new ToDo item");
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load DIALOG");
            e.printStackTrace();
            return;
        }
        ButtonType ADD =new ButtonType("ADD");
        dialog.getDialogPane().getButtonTypes().add(ADD);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result=dialog.showAndWait();



        if (result.isPresent() && result.get()==ADD){
            DialogController controller=fxmlLoader.getController();
            ToDoItem item=controller.readNewItem();

            if (item!=null){
                ToDoData.getInstance().addToDoItem(item);
                listView.setItems(ToDoData.getInstance().getToDoItem());
                listView.getSelectionModel().select(item);
            }

        }
    }


    // Delete ToDo Item using CONTEXT Menu.

    public void deleteToDoItem(ToDoItem item){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete ToDo Item ");
        alert.setHeaderText("Are you sure to delete the selected item ?");
        Optional<ButtonType> result=alert.showAndWait();
        if (result.isPresent()&& result.get()==ButtonType.OK){
            ToDoData.getInstance().deleteToDoItem(item);
        }
    }

    // Edit ToDo Item using CONTEXT Menu.

    public void editToDoItem(ToDoItem item){
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(border.getScene().getWindow());

        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogPane.fxml"));

        dialog.setTitle("Edit ToDo List ");
        dialog.setHeaderText("Use this dialog to Edit ToDo item ");

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("Can't load Dialog Pane !");
            e.printStackTrace();
            return;
        }

        ButtonType SAVE=new ButtonType("SAVE");
        dialog.getDialogPane().getButtonTypes().addAll(SAVE,ButtonType.CANCEL);

        DialogController controller=fxmlLoader.getController();
        controller.updateItem(item);


        Optional<ButtonType> result=dialog.showAndWait();
        if (result.isPresent() && result.get().equals(SAVE)){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm changes");
            alert.setHeaderText("Are you sure to make the changes ?");
            Optional<ButtonType> result1=alert.showAndWait();
            if (result1.get().equals(ButtonType.OK) && result1.isPresent()){

                ToDoItem newItem=controller.readNewItem();
                ToDoData.getInstance().replaceToDoItem(item,newItem);
            }

        }

    }

    public void handelEXIT(){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("EXIT");
        alert.setHeaderText("Are you sure to exit ?");
        Optional<ButtonType> buttonType=alert.showAndWait();

        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)){
            System.exit(0);
        }
    }

    @FXML
    public void handelAbout(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("ToDo List Application");
        alert.setHeight(500.0);
        alert.setContentText("This application is developed by Shivam Shukla.\nFor any feedback, mail at shivam.dev1097@gmail.com ");
        Optional<ButtonType> buttonType=alert.showAndWait();
    }



}
