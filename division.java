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

public class division extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("Division Converter");
        Pane root = new Pane();
        Scene mainScene = new Scene(root, 400, 400);
        mainStage.setScene(mainScene);

        // custom code below --------------------------------------------

        Label messageLabel = new Label("Enter two numbers to divide:");
        messageLabel.setLayoutX(5);
        messageLabel.setLayoutY(25);
        root.getChildren().add(messageLabel);

        TextField numberField1 = new TextField("Numerator");
        numberField1.setLayoutX(5);
        numberField1.setLayoutY(75);
        root.getChildren().add(numberField1);

        TextField numberField2 = new TextField("Denominator");
        numberField2.setLayoutX(5);
        numberField2.setLayoutY(125);
        root.getChildren().add(numberField2); 
        
        
        Label resultLabel = new Label();

        Button addButton = new Button("Convert to decimal");
        addButton.setLayoutX(5);
        addButton.setLayoutY(175);
        addButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    //declared and initialized variables to hold the input text, used in the try/catch below and for later use after them for a result
                    double a = 0;
                    double b = 0;
                    //using the try/catch will detect if the user enters a non integer value and sets the text in the box to diplay an error
                    try
                    { 
                       // TODO: handle parse exceptions with try/catch
                       

                    }
                    catch(Exception error)
                    {
                        numberField1.setText("Must enter an integer");
                        return;
                    }
                    
                    try
                    {
                       b = Double.parseDouble( numberField2.getText() );
                       
                       //this conditional checks to see if the user entered a zero in the denominator and displays that it can not divide by it
                       if( b <= 0)
                       {
                           resultLabel.setText("Can not Divide by Zero");
                           return;
                       }
                    }
                    catch(Exception error)
                    {
                        numberField2.setText("Must enter an integer");
                        return;
                    }
                    //calculates the answer in the correct format and sets a label to display for the user to see
                       double c = a/b;
                       String result = a + " / " + b + " = " + c;
                       resultLabel.setText( result );
                    
                }
            }
        );
        root.getChildren().add(addButton);
        
        resultLabel.setLayoutX(5);
        resultLabel.setLayoutY(275);
        root.getChildren().add(resultLabel); 

        // custom code above --------------------------------------------

        mainStage.show();
    }
}
