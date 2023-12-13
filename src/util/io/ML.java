package util.io;

import util.Rect;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Dictionary;
import java.util.Hashtable;

public class ML implements MouseMotionListener, MouseListener {


    private static ML mouseListener = new ML();
    private Dictionary<Integer,Boolean> buttonPressedDictionary = new Hashtable<>();

    public boolean isM1Down = false;


    private double x = 0.0, y = 0.0;

    /**
     * <p>
     * Creates singleton Instance of the util.io.ML class to be used across the entire program
     *</p>
     * @return      The ML Singleton instance
     */
    public static ML getMouseListener(){
        if(ML.mouseListener == null){
            ML.mouseListener = new ML();
        }
        return ML.mouseListener;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1){
            isM1Down = false;
        }
    }

    /**
     * <p>
     *sets the entry of the MouseEvent in the dictionary to true when the button is pressed
     * </p>
     */
    @Override
    public void mousePressed(MouseEvent e){
        x = e.getX();
        y = e.getY();
        buttonPressedDictionary.put(e.getButton(),true);
        if (e.getButton() == MouseEvent.BUTTON1){
            isM1Down = true;
        }
        e.consume();

    }

    /**
     * <p>
     * sets the entry of the MouseEvent in the dictionary to false when the button is released
     * </p>
     */
    @Override
    public void mouseReleased(MouseEvent e){
        buttonPressedDictionary.put(e.getButton(),false);
        if (e.getButton() == MouseEvent.BUTTON1){
            isM1Down = false;
        }
//        mouseMoved(e);
        e.consume();

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * <p>
     * records the mouse Position when the mouse is Moved
     * </p>
     */
    @Override
    public void mouseMoved(MouseEvent e){
        x = e.getX();
        y = e.getY();
        e.consume();
    }

    /**
     * <p>
     * Records the mouse Position when the mouse is Moved and sets MouseDragged to true
     * </p>
     */
    @Override
    public void mouseDragged(MouseEvent e){
        System.out.println("mouseDragged");
        x = e.getPoint().getX();
        y = e.getPoint().getY();
        e.consume();
    }

    /**
     * <p>
     * Compares the position of the mouse to the bounds of the rectangle
     * </p>
     * @return Boolean
     */
    public boolean isMouseInsideRect(Rect rect){



        boolean  leftEdge  = this.x >= rect.x;
        boolean  RightEdge = this.x <= rect.x+ rect.w;
        boolean  UpperEdge = this.y >= rect.y;
        boolean  LowerEdge = this.y <= rect.y+rect.h;

        return (leftEdge && RightEdge && UpperEdge &&  LowerEdge );
    }


    /**
     * <p>
     *  Compares the position of the mouse to the bounds of the rectangle
     * </p>
     * @return Boolean
     */
    public boolean isMouseInsideRect(double x, double y, double w, double h)    {

        boolean  leftEdge  = mouseListener.x >= x;
        boolean  RightEdge = mouseListener.x <= x + w;
        boolean  UpperEdge = mouseListener.y >= y;
        boolean  LowerEdge = mouseListener.y <= y + h;

        return (leftEdge && RightEdge && UpperEdge &&  LowerEdge );
    }


    /**
     * <p>
     * Returns the current state of the specified mouse button. The Code must be obtained from java.awt.event.MouseEvent;
     * <br>
     * <br>
     * Returns false even if the button was never recorded
     *</p>
     * @param  buttonCode  the button that will be checked if its press. Uses an integer value
     * @return          True if the mouse button is currently held down false otherwise
     * @see         util.io.ML
     */
    public boolean isPressed(int buttonCode) {

        try{
            return buttonPressedDictionary.get(buttonCode);

        }catch (Exception e){
            return  false;
        }
    }

}
