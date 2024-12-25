import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MovingGround {
    private final int width = 462;
    private final int height = 168;
    private final int initialX = -20;
    private final int initialY = 420;
    private final int velocityX = -4;
    private final Image img = new ImageIcon(getClass().getResource("./Sprites/ground.png")).getImage();

    private int currentX;

    MovingGround(){
        currentX = initialX;
    }

    public static void placeGround(ArrayList<MovingGround> arrayList){
        MovingGround newGround = new MovingGround();
        newGround.setCurrentX(256);
        arrayList.add(newGround);
    }

    public void moveGround(){
        // Move the ground to the left
        currentX += velocityX;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getInitialX(){
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public Image getImg() {
        return img;
    }

    public int getCurrentX(){
        return currentX;
    }

    public void setCurrentX(int value){
        currentX = value;
    }

}
