import java.awt.Point;

public class BaseTile {
    public int _index;
    public int _buttonIndex;
    public int _xCoord;
    public int _yCoord;
    public boolean _isMine;

    BaseTile(int index, int butIndex, int xCoord, int yCoord, boolean isMine)
    {
        _index = index;         //0 is empty //99 is a mine //else is a surrounding number of mines
        _buttonIndex = butIndex;
        _xCoord = xCoord;
        _yCoord = yCoord;
        _isMine = isMine;
    }

    //Init function
    public void initBaseTile(){
        _index = 0;
        _xCoord = 0;
        _yCoord = 0;
        _isMine = false;
    }

    //Get and set the index, xCoord and yCoord
    public int getIndex()
    {
        return (_index);
    }

    public void setIndex(int index)
    {
        _index = index; 
    }

    public int getButtonIndex()
    {
        return (_buttonIndex);
    }

    public void setButtonIndex(int butIndex)
    {
        _buttonIndex = butIndex; 
    }

    //Increment the index
    public void incIndex()
    {
        _index++;
    }
    
    public String ReturnIndex()
    {
        return (Integer.toString(_index));
    }

    public int getX()
    {
        return (_xCoord);
    }
    
    public void setX(int x)
    {
        _xCoord = x;
    }

    public int getY()
    {
        return (_yCoord);
    }

    public void setY(int y)
    {
        _yCoord = y;
    }

    //Get the coordinates for the mine
    public Point getCoord(){

        Point tempPoint = new Point(_xCoord, _yCoord);
        return (tempPoint); 
    }
}
