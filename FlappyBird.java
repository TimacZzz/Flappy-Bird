import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class FlappyBird {
    // Final fields, won't change!
    private final int initialX = 80;
    private final int initialY = 171;
    private final int width = 34;
    private final int height = 24;
    private final int gravity = 1;
    private final int hopVelocity = -9;
    private final Image bird1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/birdUp.png"))).getImage();
    private final Image bird2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/birdMid.png"))).getImage();
    private final Image bird3 = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/birdDown.png"))).getImage();
    private final File hopSoundEffect = new File("./src/Sound Effects/hop.wav");

    private Image currentImg;
    private int counter;
    private int currentY;
    private int velocityY;

    FlappyBird(){
        currentY = initialY;
        velocityY = 0;
        currentImg = bird3;
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
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(hopSoundEffect);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
