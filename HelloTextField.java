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

public class HelloTextField extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("TextField Demo");
        Pane root = new Pane();
        Scene mainScene = new Scene(root, 300, 300);
        mainStage.setScene(mainScene);
        
        // custom code below --------------------------------------------
        
        Label messageLabel = new Label("Enter your name:");
        messageLabel.setLayoutX(5);
        messageLabel.setLayoutY(25);
        root.getChildren().add(messageLabel); 
        
        TextField nameField = new TextField("...");
        nameField.setLayoutX(5);
        nameField.setLayoutY(75);
        root.getChildren().add(nameField); 
        
        // need to declare/initialize this before setting up EventHandler
        Label greetingLabel = new Label();
        
        Button helloButton = new Button("Say Hello");
        helloButton.setLayoutX(5);
        helloButton.setLayoutY(125);
        helloButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    String name = nameField.getText();
                    greetingLabel.setText("Hello, " + name + "!");
                }
            }
        );
        root.getChildren().add(helloButton);
        
        greetingLabel.setLayoutX(5);
        greetingLabel.setLayoutY(175);
        root.getChildren().add(greetingLabel); 
        
        // custom code above --------------------------------------------

        mainStage.show();
    }
}
