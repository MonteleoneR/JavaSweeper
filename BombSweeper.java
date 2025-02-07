import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.beans.value.*;
import javafx.event.*; 
import javafx.animation.*;
import javafx.geometry.*;

import java.awt.Point;
import java.util.*;

import javax.swing.GroupLayout.Alignment;

@SuppressWarnings("unused")
public class BombSweeper extends Application 
{

    //We can make the table based off of an array of points
    //These points would contain the position of either x or y for the x
    //and a boolean(isMine) for the y

    //We can make a custom class to hold the button constructor
    //this can have the boolean(isMine) and the coodinates int x and int y
    //this would
    public static void main(String[] args) 
    {
        launch(args);
    }
    Pane root;
    BorderPane parentPane;
    BaseTile[][] table;
    int mines;
    int tableSizeX;
    int tableSizeY;
    int rootButtonIndex;
    int nonMineTileCount;
    String gameDifficulty;

    HBox guiBox;
    int timerCount;
    Text timerText;
    Text mineCountText;
    Text winLossText;


    //LinkedList<Integer> mineList;
    public void start(Stage mainStage) 
    {
        //
        //Set up the initial window
        mainStage.setTitle("Bomb Sweeper");
       
        parentPane = new BorderPane();
        parentPane.setCenter(root);
        root = new Pane();
        //root.setPrefSize(400,400);
        
        Scene mainScene = new Scene(parentPane, 500, 500);
        mainStage.setScene(mainScene);
        mainStage.sizeToScene();
        
        guiBox = new HBox();
        guiBox.setAlignment(Pos.CENTER);
        guiBox.setPadding( new Insets(8));
        guiBox.setSpacing(8);

        winLossText = new Text();
        gameDifficulty = "Medium";

        //Top bar that hold the smile reset button, keeps time and keeps track of how many mines are left
        timerText = new Text("00");
        mineCountText = new Text("");
        Button smileyButton = new Button();
        smileyButton.setGraphic(new ImageView(new Image("assets/javaSweeperSmiley.jpg")));
        smileyButton.setMaxSize(64,64);
        List<Node> guiTopList = guiBox.getChildren();

        guiTopList.add(timerText);
        guiTopList.add(smileyButton);
        guiTopList.add(mineCountText);        

        //Menu to hold standard menu features
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);

        MenuItem newItem = new MenuItem("New Game");
        MenuItem optionsItem = new MenuItem("Options");
        MenuItem quitItem = new MenuItem("Quit");

        fileMenu.getItems().addAll(newItem, optionsItem, quitItem);

        VBox topVBox = new VBox();
        topVBox.getChildren().add(menuBar);
        topVBox.getChildren().addAll(guiBox);
        topVBox.setSpacing(16);
        topVBox.setAlignment(Pos.CENTER);

        parentPane.setTop(topVBox);

        //Add on button click actions for menus
        newItem.setOnAction((ActionEvent event) ->
            {
                //Clear the grid and reset variables to start a new game
                restartGame();
            } 
        );

        optionsItem.setOnAction((ActionEvent event) ->
            {
                //
                //Open a new window and have game options for difficulty and possibly colors
                //Custom alert box
                Alert optionsAlert = new Alert(AlertType.NONE);
                optionsAlert.setTitle("Options");
                optionsAlert.setHeaderText("Current difficulty is " + gameDifficulty);
                optionsAlert.setContentText("Select a new difficulty");

                //Would like these to be radio buttons...
                ButtonType easyBt = new ButtonType("Easy");
                ButtonType mediumBt = new ButtonType("Medium");
                ButtonType hardBt = new ButtonType("Hard");
                ButtonType closeBt = new ButtonType("Close");

                optionsAlert.getButtonTypes().setAll(easyBt, mediumBt, hardBt, closeBt);

                //Show the custom alert window and set the difficulty accordingly
                Optional<ButtonType> returnedDifficulty = optionsAlert.showAndWait();
                if(returnedDifficulty.isPresent())
                {
                    ButtonType chosenDifficulty = returnedDifficulty.get();
                    if(!chosenDifficulty.getText().equals("Close"))
                    {    
                        gameDifficulty = chosenDifficulty.getText();
                        restartGame();
                    }
                }

            }
        );

        quitItem.setOnAction((ActionEvent event) ->
            {
                //Quit the game
                mainStage.close();
            }
        );

        smileyButton.setOnAction((ActionEvent event) ->
            {
                //Clear the grid and reset variables to start a new game
                restartGame();
            } 
        );

        initGame();

        mainStage.show();
    }

    //
    //Create the board with new random mines 
    private void initGame()
    {

        if (gameDifficulty.equals("Easy")) 
        {
            mines = 20;
            tableSizeX = 16;
            tableSizeY = 16;            
        }
        
        else if(gameDifficulty.equals("Medium"))
        {
            mines = 40;
            tableSizeX = 16;
            tableSizeY = 16;
        }
        else if(gameDifficulty.equals("Hard"))
        {
            mines = 60;
            tableSizeX = 16;
            tableSizeY = 16;
        }

        table = new BaseTile[tableSizeX][tableSizeY];//Table size declaration

        int random = 0;
        nonMineTileCount = (tableSizeX * tableSizeY) - mines - 1; //-1 adjustment for base of 0;
        rootButtonIndex = 0;

        timerText.setText("00");
        mineCountText.setText(Integer.toString(mines));
        //int totalSize = tableSizeX * tableSizeY;
        //mineList = new LinkedList<Integer>();

        //Initialize the array
        for(int y = 0; y < tableSizeY; y++)
        {  
            for(int x = 0; x < tableSizeX; x++)
            { 
                table[x][y] = new BaseTile(0, rootButtonIndex, x, y, false);
                rootButtonIndex++;
            }
        }

        //Create the mine list to hold the mines
        //ArrayList<BaseTile> mineList = new ArrayList<BaseTile>();
        do {
            mines--;
            double randX = Math.random() * (tableSizeX - 1);
            //rounding up to the nearest int is 
            int randTopX = (int) randX;

            double randY = Math.random() * (tableSizeY - 1);
            //rounding up to the nearest int is
            int randTopY = (int) randY;
            
            //
            //0,0     1,0       2,0         dont sub 1x     
            //0,1    Mine       2,1         dont sub 1x
            //0,2     1,2       2,2         dont sub 1x
            //
            //Set thhis coordinate as a mine and set the surrounding coordinates to increase their mine count
            //
            //If the random mine coordinates are already a mine, then skip the check and increment the mines back to the previous number
            //This check also fixes duplicate mines from increasing the surrounding box numbers
            if(!table[randTopX][randTopY]._isMine)
            {
                table[randTopX][randTopY]._isMine = true;
                table[randTopX][randTopY].setIndex(99);
                    if(randTopX == 0 && randTopY == 0)
                    {
                        //
                        //Top left corner
                        //Keep, right, bottom right, bottom
                        table[randTopX+1][randTopY].incIndex();       //Right tile
                        table[randTopX][randTopY+1].incIndex();       //Bottom Tile
                        table[randTopX+1][randTopY+1].incIndex();     //Bottom right tile
                    }
                    else if (randTopX == 0 && randTopY == (tableSizeY - 1)) {
                        //
                        //Bottom Left
                        //Keep current, top, top right, right
                        table[randTopX][randTopY-1].incIndex();       //Top tile
                        table[randTopX+1][randTopY-1].incIndex();     //Top Right tile
                        table[randTopX+1][randTopY].incIndex();       //Right tile
                    }
                    else if (randTopX == 0) {
                        //
                        //Left side
                        //Keep current, top, top right, right, bottom right, bottom
                        table[randTopX][randTopY-1].incIndex();       //Top tile
                        table[randTopX+1][randTopY-1].incIndex();     //Top Right tile
                        table[randTopX+1][randTopY].incIndex();       //Right tile
                        table[randTopX+1][randTopY+1].incIndex();     //Bottom right tile
                        table[randTopX][randTopY+1].incIndex();       //Bottom Tile
                    }
                    else if (randTopX == (tableSizeX - 1)) {
                        //
                        //Right side
                        //Keep, top, top left, left, bottom left, bottom
                        table[randTopX][randTopY-1].incIndex();       //Top tile
                        table[randTopX-1][randTopY-1].incIndex();     //Top Left tile
                        table[randTopX-1][randTopY].incIndex();       //Left tile
                        table[randTopX-1][randTopY+1].incIndex();     //Bottom left tile
                        table[randTopX][randTopY+1].incIndex();       //Bottom Tile
                    }
                    else if (randTopY == 0) {
                        //
                        //Top
                        //Keep, left, bottom left, bottom, bottom right, right
                        table[randTopX-1][randTopY].incIndex();       //Left tile
                        table[randTopX-1][randTopY+1].incIndex();     //Bottom left tile
                        table[randTopX][randTopY+1].incIndex();       //Bottom Tile
                        table[randTopX+1][randTopY+1].incIndex();     //Bottom right tile
                        table[randTopX+1][randTopY].incIndex();       //Right tile
                    }
                    else if (randTopY == (tableSizeY - 1)) {
                        //
                        //Bottom
                        //Keep, left, top left, top, top right, right,
                        table[randTopX-1][randTopY].incIndex();       //Left tile
                        table[randTopX-1][randTopY-1].incIndex();     //Top Left tile
                        table[randTopX][randTopY-1].incIndex();       //Top tile
                        table[randTopX+1][randTopY-1].incIndex();     //Top Right tile
                        table[randTopX+1][randTopY].incIndex();       //Right tile

                    }
                    else if (randTopX == (tableSizeX - 1) && randTopY == 0) {
                        //
                        //Top right
                        //Keep, left, bottom left, bottom
                        table[randTopX-1][randTopY].incIndex();       //Left tile
                        table[randTopX-1][randTopY+1].incIndex();     //Bottom left tile
                        table[randTopX][randTopY+1].incIndex();       //Bottom Tile
                    }
                    else if (randTopX == (tableSizeX - 1) && randTopY == (tableSizeY - 1)) {
                        //
                        //Bottom right
                        //Keep, top, top left, left
                        table[randTopX][randTopY-1].incIndex();       //Top tile
                        table[randTopX-1][randTopY-1].incIndex();     //Top Left tile
                        table[randTopX-1][randTopY].incIndex();       //Left tile
                    }
                    else{
                        //
                        //Current
                        table[randTopX-1][randTopY-1].incIndex();     //Top Left tile
                        table[randTopX][randTopY-1].incIndex();       //Top tile
                        table[randTopX+1][randTopY-1].incIndex();     //Top Right tile
                        table[randTopX-1][randTopY].incIndex();       //Left tile
                        table[randTopX+1][randTopY].incIndex();       //Right tile
                        table[randTopX-1][randTopY+1].incIndex();     //Bottom left tile
                        table[randTopX][randTopY+1].incIndex();       //Bottom Tile
                        table[randTopX+1][randTopY+1].incIndex();     //Bottom right tile
                    }
            }else{
                //replace the decrement mine back so we can get an accurate number of mines
                mines++;
            }
        } while (mines >= 0);

        
        //
        //Start generating the table with buttons and mines
        for(int y = 0; y < tableSizeY; y++)
        {  
            for(int x = 0; x < tableSizeX; x++)
            {           
                        //
                        //Default empty tile spot

                        Button iButton;
                        int dupX = x;
                        int dupY = y;
                        
                        if(table[dupX][dupY]._isMine)
                        {
                            iButton = new Button("  ");//Button("" + x + ""+ y);
                            iButton.setLayoutX((x * 25));
                            iButton.setLayoutY((y * 25));
                            iButton.setOnAction(
                                        new EventHandler<ActionEvent>()
                                        {
                                            public void handle(ActionEvent e)
                                            {
                                                //System.out.println(table[dupX][dupY]._isMine + " " + table[dupX][dupY].ReturnIndex());
                                                MineBlown();
                                            }
                                        }
                                    );
                        }

                        else
                        {
                            //For all other tiles, default them to nothing
                            iButton = new Button("  ");//(table[dupX][dupY].ReturnIndex());//Button("" + x + ""+ y);
                            iButton.setLayoutX((x * 25));
                            iButton.setLayoutY((y * 25));
                            iButton.setOnAction(
                                        new EventHandler<ActionEvent>()
                                        {
                                            public void handle(ActionEvent e)
                                            {
                                                //System.out.println(table[dupX][dupY].ReturnIndex());

                                                //DisableTile(table[dupX][dupY].getButtonIndex());
                                                ExposeTile(table[dupX][dupY]);
                                            }
                                        }
                                    );
                        }

                        root.getChildren().add(iButton);
            }
        }

        parentPane.setCenter(root);
    }

    private void startTimer()
    {
        AnimationTimer timeTicker = new AnimationTimer() {
            public void handle(long time)
            {
                timerCount += time;
                timerText.setText(Integer.toString(timerCount));
            }
        };
        timeTicker.start();
    }

    //
    //Method that clears the board and re-inits the game
    private void restartGame()
    {
        winLossText.setText("");
        root.getChildren().clear();
        initGame();
    }

    //
    //When a mine is clicked, delete all of the tiles and display a lose message
    private void MineBlown()
    {
        root.getChildren().clear();

        winLossText.setText("You Lost!");
        winLossText.setFont(new Font(50));
        winLossText.setX(150);
        winLossText.setY(250);
        //lossText.setX(root.getWidth()/2);
        //lossText.setY(root.getHeight()/2);
        winLossText.setTextAlignment(TextAlignment.LEFT);

        parentPane.setCenter(winLossText);
    }

    //
    //when a normal tile is selected, check the location against the 2D table array
    //Remove it and decrement the nonTileMine count
    //Once all of the non tiles are selected, display the win text
    private void DisableTile(int butIndex, int mineIndex)
    {  
        nonMineTileCount--;
        System.out.println(nonMineTileCount);

        try {
            Node tempNode = root.getChildren().get(butIndex);
            Button bt = (Button)tempNode;
            if(!Integer.toString(mineIndex).equals("0"))
            {
                bt.setText(Integer.toString(mineIndex));
            }
            else{
                bt.setText("  ");

            }
            root.getChildren().get(butIndex).setDisable(true);

        
        } catch (Exception e) {
            System.out.println("grab node from root error");
        }
        
        if(nonMineTileCount == 0)
        {
            winLossText.setText("You Win!");
            winLossText.setFont(new Font(50));
            winLossText.setX(150);
            winLossText.setY(250);
            //lossText.setX(root.getWidth()/2);
            //lossText.setY(root.getHeight()/2);
            winLossText.setTextAlignment(TextAlignment.LEFT);

            parentPane.setCenter(winLossText);
        }
    }
    
    //
    //When a tile that has no tiles around it is selected
    //Check surrounding tiles and open them by default
    private void ExposeTile(BaseTile indexTile)
    {
        //declare list of surrounding 0 tiles
        ArrayList<BaseTile> surroundingZeroTiles = new ArrayList<BaseTile>();
        boolean openNext = false;
        DisableTile(indexTile._buttonIndex, indexTile._index);
        
        if(indexTile._index == 0)
        {   

            CheckSurroundingTiles(indexTile, surroundingZeroTiles);
            
            //If gathered list is not empty then run through the list
            if(!surroundingZeroTiles.isEmpty())
            {
                do
                {
                    //for each listed 0 tile, check surrounding tiles, if 0 add them to the list
                    //      also for each 0 tile, call DisableTile() to auto select and remove that tile
                    int toDelete = 0;
                    for(int fakeIndexTile = 0; fakeIndexTile < surroundingZeroTiles.size(); fakeIndexTile++)
                    {
                        toDelete++;
                        BaseTile fakeBaseTile = surroundingZeroTiles.get(fakeIndexTile);
                        //CheckSurroundingTiles(fakeBaseTile, surroundingZeroTiles);
                        DisableTile(fakeBaseTile._buttonIndex, fakeBaseTile._index);
                    }
                    
                    //delete the already processed surroundingzeroTiles array
                    if(toDelete != -1)
                    {
                        do
                        {
                            toDelete--;
                            surroundingZeroTiles.remove(toDelete);
                        }while(toDelete != 0);
                    }

                    openNext = true;

                } while(surroundingZeroTiles.isEmpty());
            }
        }
    }

    private void CheckSurroundingTiles(BaseTile indexTile, ArrayList<BaseTile> tempList)
    {

        if(indexTile._xCoord == 0 && indexTile._yCoord == 0)
        {
            //
            //Top left corner
            //Keep, right, bottom right, bottom
            if(table[indexTile._xCoord+1][indexTile._yCoord]._index == 0 && 
                 root.getChildren().get( table[indexTile._xCoord+1][indexTile._yCoord]._buttonIndex ).isVisible())
                {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord]);}   //Right tile

            if(table[indexTile._xCoord][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
                {tempList.add(table[indexTile._xCoord][indexTile._yCoord+1]);}       //Bottom Tile
                
            if(table[indexTile._xCoord+1][indexTile._yCoord+1]._index == 0 && 
                    root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
                {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord+1]);}     //Bottom right tile
        }
        else if (indexTile._xCoord == 0 && indexTile._yCoord == (tableSizeY - 1)) { 
            //
            //Bottom Left
            //Keep current, top, top right, right
            if(table[indexTile._xCoord][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord-1]);}       //Top tile
            if(table[indexTile._xCoord+1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord-1]);}     //Top Right tile
            if(table[indexTile._xCoord+1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord]);}       //Right tile
        }
        else if ( indexTile._xCoord == 0 && indexTile._yCoord != 0 && indexTile._yCoord != (tableSizeY - 1)) {
            //
            //Left side
            //Keep current, top, top right, right, bottom right, bottom
            if(table[indexTile._xCoord][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord-1]);}       //Top tile
            if(table[indexTile._xCoord+1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord-1]);}     //Top Right tile
            if(table[indexTile._xCoord+1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord]);}       //Right tile
            if(table[indexTile._xCoord+1][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord+1]);}     //Bottom right tile
            if(table[indexTile._xCoord][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord+1]);}       //Bottom Tile
        }
        else if (indexTile._xCoord == (tableSizeX - 1) && indexTile._yCoord != 0 && indexTile._yCoord != (tableSizeY -1)) {
            //
            //Right side
            //Keep, top, top left, left, bottom left, bottom
            if(table[indexTile._xCoord][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord-1]);}       //Top tile
            if(table[indexTile._xCoord-1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord-1]);}     //Top Left tile
            if(table[indexTile._xCoord-1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord]);}       //Left tile
            if(table[indexTile._xCoord-1][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord+1]);}     //Bottom left tile
            if(table[indexTile._xCoord][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord+1]);}       //Bottom Tile
        }
        else if (indexTile._xCoord != 0 && indexTile._xCoord != (tableSizeX -1) && indexTile._yCoord == 0) {
            //
            //Top
            //Keep, left, bottom left, bottom, bottom right, right
            if(table[indexTile._xCoord-1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord]);}       //Left tile
            if(table[indexTile._xCoord-1][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord+1]);}     //Bottom left tile
            if(table[indexTile._xCoord][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord+1]);}       //Bottom Tile
            if(table[indexTile._xCoord+1][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord+1]);}     //Bottom right tile
            if(table[indexTile._xCoord+1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord]);}       //Right tile
        }
        else if (indexTile._xCoord != 0 && indexTile._xCoord != (tableSizeX -1) && indexTile._yCoord == (tableSizeY - 1)) {
            //
            //Bottom
            //Keep, left, top left, top, top right, right,
            if(table[indexTile._xCoord-1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord]);}       //Left tile
            if(table[indexTile._xCoord-1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord-1]);}     //Top Left tile
            if(table[indexTile._xCoord][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord-1]);}       //Top tile
            if(table[indexTile._xCoord+1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord-1]);}     //Top Right tile
            if(table[indexTile._xCoord+1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord]);}       //Right tile

        }
        else if (indexTile._xCoord == (tableSizeX - 1) && indexTile._yCoord == 0) {
            //
            //Top right
            //Keep, left, bottom left, bottom
            if(table[indexTile._xCoord-1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord]);}       //Left tile
            if(table[indexTile._xCoord-1][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord+1]);}     //Bottom left tile
            if(table[indexTile._xCoord][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord+1]);}       //Bottom Tile
        }
        else if (indexTile._xCoord == (tableSizeX - 1) && indexTile._yCoord == (tableSizeY - 1)) {
            //
            //Bottom right
            //Keep, top, top left, left
            if(table[indexTile._xCoord][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord-1]);}       //Top tile
            if(table[indexTile._xCoord-1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord-1]);}     //Top Left tile
            if(table[indexTile._xCoord-1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord]);}       //Left tile
        }
        else{
            //
            //Current
            if(table[indexTile._xCoord-1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord-1]);}     //Top Left tile
            if(table[indexTile._xCoord][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord-1]);}       //Top tile
            if(table[indexTile._xCoord+1][indexTile._yCoord-1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord-1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord-1]);}     //Top Right tile
            if(table[indexTile._xCoord-1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord]);}       //Left tile
            if(table[indexTile._xCoord+1][indexTile._yCoord]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord]);}       //Right tile
            if(table[indexTile._xCoord-1][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord-1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord-1][indexTile._yCoord+1]);}     //Bottom left tile
            if(table[indexTile._xCoord][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord][indexTile._yCoord+1]);}       //Bottom Tile
            if(table[indexTile._xCoord+1][indexTile._yCoord+1]._index == 0 && 
                root.getChildren().get(table[indexTile._xCoord+1][indexTile._yCoord+1]._buttonIndex ).isVisible()) 
            {tempList.add(table[indexTile._xCoord+1][indexTile._yCoord+1]);}     //Bottom right tile
        }
    }

}
