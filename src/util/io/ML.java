package util.io;

import util.Rect;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Dictionary;
import java.util.Hashtable;

public class ML extends MouseAdapter implements MouseMotionListener {


    private static ML mouseListener = new ML();
    private Dictionary<Integer,Boolean> buttonPressedDictionary = new Hashtable<>();

    private boolean mouseDragging = false;


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


    /**
     * <p>
     *sets the entry of the MouseEvent in the dictionary to true when the button is pressed
     * </p>
     */
    @Override
    public void mousePressed(MouseEvent e){
        mouseMoved(e);
        buttonPressedDictionary.put(e.getButton(),true);

    }

    /**
     * <p>
     * sets the entry of the MouseEvent in the dictionary to false when the button is released
     * </p>
     */
    @Override
    public void mouseReleased(MouseEvent e){
        buttonPressedDictionary.put(e.getButton(),false);
        mouseDragging = false;
        mouseMoved(e);

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
    }

    /**
     * <p>
     * Records the mouse Position when the mouse is Moved and sets MouseDragged to true
     * </p>
     */
    @Override
    public void mouseDragged(MouseEvent e){
        mouseMoved(e);
        mouseDragging = true;
    }

    /**
     * <p>
     * Compares the position of the mouse to the bounds of the rectangle
     * </p>
     * @return Boolean
     */
    public boolean isMouseInsideRect(Rect rect){


        boolean  leftEdge  = mouseListener.x >= rect.x;
        boolean  RightEdge = mouseListener.x <= rect.x+ rect.w;
        boolean  UpperEdge = mouseListener.y >= rect.y;
        boolean  LowerEdge = mouseListener.y <= rect.y+rect.h;

        return (leftEdge && RightEdge && UpperEdge &&  LowerEdge );
    }


    /**
     * <p>
     *  Compares the position of the mouse to the bounds of the rectangle
     * </p>
     * @return Boolean
     */
    public boolean isMouseInsideRect(double x, double y, double w, double h){


        boolean  leftEdge  = mouseListener.x >= x;
        boolean  RightEdge = mouseListener.x <= x + w;
        boolean  UpperEdge = mouseListener.y >= y;
        boolean  LowerEdge = mouseListener.y <= y + h;

        return (leftEdge && RightEdge && UpperEdge &&  LowerEdge );
    }

    /**
     * <p>
     *  Returns the x position of the mouse
     * </p>
     * @return double
     */
    public double getX() {
        return x;
    }

    /**
     * <p>
     *  Returns the y position of the mouse
     * </p>
     * @return double
     */
    public double getY() {
        return y;
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

    /**
     * <p>
     *  Returns a the value of the variable mouseDragging.
     * </p>
     * @return boolean
     */
    public boolean isMouseDragging(){return mouseDragging;}
}
