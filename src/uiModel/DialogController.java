package uiModel;

import dataModel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDescriptionFXML;
    @FXML
    private TextArea detailsFXML;
    @FXML
    private DatePicker datepicker;

    public ToDoItem readNewItem(){
        String shortDesc=shortDescriptionFXML.getText().trim();
        String detail=detailsFXML.getText().trim();
        LocalDate date=datepicker.getValue();

        if (shortDesc!=null && detail!=null && date!=null){
            return new ToDoItem(shortDesc,detail,date);
        }
        return null;
    }

    public void updateItem(ToDoItem item){
        shortDescriptionFXML.setText(item.getShortDescription());
        detailsFXML.setText(item.getDetails());
        datepicker.setValue(item.getDate());
    }


}
