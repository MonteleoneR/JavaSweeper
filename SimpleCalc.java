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

public class SimpleCalc extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("SimpleCalc");
        Pane root = new Pane();
        Scene mainScene = new Scene(root, 400, 400);
        mainStage.setScene(mainScene);

        // custom code below --------------------------------------------

        Label messageLabel = new Label("Enter two numbers:");
        messageLabel.setLayoutX(5);
        messageLabel.setLayoutY(25);
        root.getChildren().add(messageLabel); 

        TextField numberField1 = new TextField("337");
        numberField1.setLayoutX(5);
        numberField1.setLayoutY(75);
        root.getChildren().add(numberField1); 

        TextField numberField2 = new TextField("14");
        numberField2.setLayoutX(5);
        numberField2.setLayoutY(125);
        root.getChildren().add(numberField2); 

        Label resultLabel = new Label();

        Button addButton = new Button("Calculate Sum");
        addButton.setLayoutX(5);
        addButton.setLayoutY(175);
        addButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    // TODO: handle parse exceptions with try/catch
                    double a = Double.parseDouble( numberField1.getText() );
                    double b = Double.parseDouble( numberField2.getText() );
                    double c = a+b;
                    String result = a + " + " + b + " = " + c;
                    resultLabel.setText( result );
                }
            }
        );
        root.getChildren().add(addButton);

        Button subButton = new Button("Calculate Difference");
        subButton.setLayoutX(5);
        subButton.setLayoutY(225);
        subButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    // TODO: handle parse exceptions with try/catch
                    double a = Double.parseDouble( numberField1.getText() );
                    double b = Double.parseDouble( numberField2.getText() );
                    double c = a-b;
                    String result = a + " - " + b + " = " + c;
                    resultLabel.setText( result );
                }
            }
        );
        root.getChildren().add(subButton);

        resultLabel.setLayoutX(5);
        resultLabel.setLayoutY(275);
        root.getChildren().add(resultLabel); 

        // custom code above --------------------------------------------

        mainStage.show();
    }
}
