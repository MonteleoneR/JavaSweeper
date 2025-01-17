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
    int tableSizeX;
    int tableSizeY;
    int rootButtonIndex;
    int nonMineTileCount;
    HBox guiBox;
    Text timerText;
    Text mineCountText;

    //LinkedList<Integer> mineList;
    public void start(Stage mainStage) 
    {
        //
        //Set up the initial window
        mainStage.setTitle("Bomb Sweeper");
        
        root = new Pane();
        parentPane = new BorderPane();
        parentPane.setCenter(root);

        guiBox = new HBox();
        guiBox.setAlignment(Pos.CENTER);
        guiBox.setPadding( new Insets(8));
        guiBox.setSpacing(8);

        //Top bar that hold the smile reset button, keeps time and keeps track of how many mines are left
        List<Node> topBoxList = guiBox.getChildren();
        Button smileyButton;

        Scene mainScene = new Scene(parentPane, 500, 500);
        mainStage.setScene(mainScene);

        int mines = 40;                 //Total number of mines
        tableSizeX = 16;            //==Table==
        tableSizeY = 16;            //==Size===
        
        table = new BaseTile[tableSizeX][tableSizeY];//Table size declaration

        int random = 0;
        nonMineTileCount = (tableSizeX * tableSizeY) - mines - 1; //-1 adjustment for base of 0;
        rootButtonIndex = 0;

        timerText = new Text("00");
        mineCountText = new Text();

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
        //Declare boolean to skip writing an extra blank tile
        boolean mineSet = false;
        
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
                            iButton = new Button("X");//Button("" + x + ""+ y);
                            iButton.setLayoutX((x * 30));
                            iButton.setLayoutY((y * 30));
                            iButton.setOnAction(
                                        new EventHandler<ActionEvent>()
                                        {
                                            public void handle(ActionEvent e)
                                            {
                                                System.out.println(table[dupX][dupY]._isMine + " " + table[dupX][dupY].ReturnIndex());
                                                MineBlown();
                                            }
                                        }
                                    );
                        }

                        else
                        {
                            //For all other tiles, default them to nothing
                            iButton = new Button(table[dupX][dupY].ReturnIndex());//Button("" + x + ""+ y);
                            iButton.setLayoutX((x * 30));
                            iButton.setLayoutY((y * 30));
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
                        //     //break;
                        //         //System.out.println("#" + i + " totalSize=" + totalSize + " pos=" + pos);

                        // }
                        // }
            }
        }

            // //
            // //2-12-24
            // for ( BaseTile mine : mineList)
            // {
            //     if(x==mine._xCoord && y == mine._yCoord)
            //     {
            //         BaseTile[_xCoord][yCoord] 

            //         //break;
            //     }
            // }

        // custom code above --------------------------------------------

        mainStage.show();
    }

    //
    //When a mine is clicked, delete all of the tiles and display a lose message
    private void MineBlown()
    {
        root.getChildren().clear();

        Text lossText = new Text("You Lost!");
        lossText.setFont(new Font(50));
        lossText.setX(150);
        lossText.setY(250);
        //lossText.setX(root.getWidth()/2);
        //lossText.setY(root.getHeight()/2);
        lossText.setTextAlignment(TextAlignment.LEFT);

        root.getChildren().add(lossText);
    }

    //
    //when a normal tile is selected, check the location against the 2D table array
    //Remove it and decrement the nonTileMine count
    //Once all of the non tiles are selected, display the win text
    private void DisableTile(int butIndex)
    {   
        nonMineTileCount--;
        root.getChildren().get(butIndex).setVisible(false);
        
        //table[coordPoint.x][coordPoint.y].
        
        System.out.println(nonMineTileCount);
        if(nonMineTileCount == 0)
        {
            Text winText = new Text("You Win!");
            winText.setFont(new Font(50));
            winText.setX(150);
            winText.setY(250);
            //lossText.setX(root.getWidth()/2);
            //lossText.setY(root.getHeight()/2);
            winText.setTextAlignment(TextAlignment.LEFT);

            root.getChildren().add(winText);
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
        DisableTile(indexTile._buttonIndex);
        
        if(indexTile._index == 0)
        {   

            CheckSurroundingTiles(indexTile, surroundingZeroTiles);
            
            //If gathered list is not empty then run through the list
            if(surroundingZeroTiles.isEmpty() != true)
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
                        DisableTile(fakeBaseTile._buttonIndex);
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
