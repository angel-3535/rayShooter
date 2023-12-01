package util.io;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Dictionary;
import java.util.Hashtable;

public class KL extends KeyAdapter implements KeyListener
{
    /**stores the state of keys being pressed*/
    private Dictionary<Integer,Boolean> keyPressedDictionary = new Hashtable<>();

    private static KL keyListener = null;


    /**
     * <p>
     * Creates singleton Instance of the util.io.KL class to be used across the entire program
     *</p>
     * @return      The Singleton instance
     */
    public static KL getKeyListener(){
        if(KL.keyListener == null){
            KL.keyListener = new KL();
        }
        return KL.keyListener;
    }

    /**
     * <p>
     *sets the entry of the keyEvent in the dictionary to true when the key is pressed
     *</p>
    */
    @Override
    public void keyPressed(KeyEvent e){

        keyPressedDictionary.put(e.getKeyCode(),true);

    }

    /**
     * <p>
     * sets the entry of the keyEvent in the dictionary to false when the key is released
     * </p>
    */
    @Override
    public void keyReleased(KeyEvent e) {
        keyPressedDictionary.put(e.getKeyCode(),false);
    }


    /**
     * <p>
     * Returns the current state of the specified key. The Code must be obtained from java.awt.event.KeyEvent
     * <br>
     * <br>
     * Returns false even if the key was never recorded
     *</p>
     * @param  keyCode  the key that will be checked if its press
     * @return          True if the key is currently held down false otherwise
     * @see         KL
     */
    public boolean isKeyDown(int keyCode){

        try{
           return keyPressedDictionary.get(keyCode);
        }catch (Exception e){
            return  false;
        }
    }
}
