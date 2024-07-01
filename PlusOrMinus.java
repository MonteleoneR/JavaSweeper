import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.beans.value.*;
import javafx.event.*; 
import javafx.animation.*;
import javafx.geometry.*;
import java.util.*;

public class PlusOrMinus extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("Plus Or Minus");
        Pane root = new Pane();
        Scene mainScene = new Scene(root, 300, 300);
        mainStage.setScene(mainScene);

        // custom code below --------------------------------------------

        Label messageLabel = new Label("Enter a number:");
        messageLabel.setLayoutX(5);
        messageLabel.setLayoutY(25);
        root.getChildren().add(messageLabel); 

        TextField numberField = new TextField();
        numberField.setLayoutX(5);
        numberField.setLayoutY(75);
        numberField.setPromptText("Enter Number Here");
        root.getChildren().add(numberField); 

        Button addButton = new Button("+1");
        addButton.setLayoutX(5);
        addButton.setLayoutY(125);
        addButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    String numberString = numberField.getText();
                    try
                    {
                        double numberValue = Double.parseDouble(numberString);
                        double plusOneValue = numberValue + 1;
                        String plusOneString = Double.toString(plusOneValue);
                        numberField.setText( plusOneString );
                    }
                    catch (Exception error)
                    {
                        numberField.setText("Invalid entry.");
                    }
                }
            }
        );
        root.getChildren().add(addButton);

        Button subButton = new Button("-1");
        subButton.setLayoutX(5);
        subButton.setLayoutY(175);
        subButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    String numberString = numberField.getText();
                    try
                    {
                        double numberValue = Double.parseDouble(numberString);
                        double minusOneValue = numberValue - 1;
                        String minusOneString = Double.toString(minusOneValue);
                        numberField.setText( minusOneString );
                    }
                    catch (Exception error)
                    {
                        numberField.setText("Invalid entry.");
                    }
                }
            }
        );
        root.getChildren().add(subButton);

        // custom code above --------------------------------------------

        mainStage.show();
    }
}
