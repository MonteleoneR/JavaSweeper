import java.awt.Point;

public class Mine {

    public int index;
    public int xCoord;
    public int yCoord;

    public void Mine(int index, int x, int y)
    {
        this.index = index;
        this.xCoord = x;
        this.yCoord = y;
    }

    // //
    // //Get and set the index, xCoord and yCoord
    // public int getIndex()
    // {
    //     return (index);
    // }
    // public int getX()
    // {
    //     return (xCoord);
    // }
    // public int getY()
    // {
    //     return (yCoord);
    // }

    // public void setIndex(int index)
    // {
    //     this.index = index; 
    // }
    // public void setX(int x)
    // {
    //     xCoord = x;
    // }
    // public void setY(int y)
    // {
    //     yCoord = y;
    // }

    //
    //Get the coordinates for the mine
    public Point getCoord(){

        Point tempPoint = new Point(xCoord, yCoord);
        return (tempPoint); 
    }
}
