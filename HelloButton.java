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

public class HelloButton extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("Button Demo");
        Pane root = new Pane();
        Scene mainScene = new Scene(root, 300, 300);
        mainStage.setScene(mainScene);
        
        // custom code below --------------------------------------------
        
        Label messageLabel = new Label("Press a button...");
        messageLabel.setLayoutX(5);
        messageLabel.setLayoutY(25);
        root.getChildren().add(messageLabel); 
        
        Button helloButton = new Button("Say Hello");
        helloButton.setLayoutX(5);
        helloButton.setLayoutY(75);
        helloButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    messageLabel.setText("Hello!");
                }
            }
        );
        root.getChildren().add(helloButton);
        
        Button goodbyeButton = new Button("Say Goodbye");
        goodbyeButton.setLayoutX(5);
        goodbyeButton.setLayoutY(125);
        goodbyeButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    messageLabel.setText("Goodbye!");
                }
            }
        );
        root.getChildren().add(goodbyeButton);
        
        // custom code above --------------------------------------------

        mainStage.show();
    }
}
