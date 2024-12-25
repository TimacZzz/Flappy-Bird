import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Pipe {
    private final int width = 78;
    private final int height = 412;
    private final int velocityX = -4;

    private static final int openingSpace = 120;

    private int x;
    private int y;
    private Image img;
    private boolean passed = false;

    Pipe(Image img){
        x = 360;
        y = 0;
        this.img = img;
    }

    public static void placePipes(ArrayList<Pipe> pipes){
        int randomPipeY = (int) (0 - 172 - Math.random() * 180);

        Image topPipeImg = new ImageIcon(Objects.requireNonNull(Pipe.class.getResource("./Sprites/upPipe.png"))).getImage();
        Image bottomPipeImg = new ImageIcon(Objects.requireNonNull(Pipe.class.getResource("./Sprites/downPipe.png"))).getImage();

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + 412 + openingSpace;
        pipes.add(bottomPipe);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getX() {
        return x;
    }

    public void changeX(int value){
        x += value;
    }

    public int getY(){
        return y;
    }

    public Image getImg() {
        return img;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
