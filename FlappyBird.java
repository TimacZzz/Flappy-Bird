import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FlappyBird {
    // Final fields, won't change!
    private final int initialX = 100;
    private final int initialY = 320;
    private final int width = 34;
    private final int height = 24;
    private final int gravity = 1;
    private final int hopVelocity = -9;
    private final Image bird1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Images/bird1.png"))).getImage();
    private final Image bird2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Images/bird2.png"))).getImage();
    private final Image bird3 = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Images/bird3.png"))).getImage();

    private Image currentImg;
    private int counter;
    private int currentY;
    private int velocityY;

    FlappyBird(){
        currentY = 320;
        velocityY = 0;
        currentImg = bird2;
        counter = 2;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getCurrentImg() {
        return currentImg;
    }

    public void setImg(){
        switch (counter){
            case 1:
                currentImg = bird2;
                counter++;
                break;
            case 2:
                currentImg = bird3;
                counter++;
                break;
            case 3:
                currentImg = bird1;
                counter = 1;
                break;
        }
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int value){
        currentY = value;
    }

    public void setVelocityY(int value){
        velocityY = value;
    }

    public void hop(){
        velocityY = hopVelocity;
    }

    // Maybe Later Change how the gravity works
    public void mimicGravity(){
        velocityY += gravity;
        currentY += velocityY;
        if (currentY < 0){
            currentY = 0;
        }
    }
}
