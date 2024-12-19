import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Controller extends JPanel implements ActionListener, KeyListener{
    private final int boardWidth;
    private final int boardHeight;
    private int counter;
    private double score;
    private boolean gameOver;

    // BK Image
    private Image backgroundImg;

    // Flappy Bird
    private FlappyBird flappyBird;

    // List of pipes
    private ArrayList<Pipe> pipes;

    // Timers
    private Timer gameLoop;
    private Timer placePipeTimer;

    Controller(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        counter = 0;
        score = 0;
        gameOver = false;

        // load images
        backgroundImg = new ImageIcon(getClass().getResource("./Images/flappybirdbg.png")).getImage();

        flappyBird = new FlappyBird();
        pipes = new ArrayList<>();

        // game timer
        gameLoop = new Timer(1000 / 60, this);
        placePipeTimer = new Timer(1500, e -> Pipe.placePipes(pipes));

        setPreferredSize(new Dimension(boardWidth, boardHeight));
    }

    public void homePage(){
        
    }

    public void startGame(){
        gameLoop.start();
        setFocusable(true);
        addKeyListener(this);
        placePipeTimer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Draw Background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //Draw FlappyBird
        g.drawImage(flappyBird.getCurrentImg(), flappyBird.getInitialX(), flappyBird.getCurrentY(),
                    flappyBird.getWidth(), flappyBird.getHeight(), null);

        //Draw Pipes
        for (Pipe p : pipes){
            g.drawImage(p.getImg(), p.getX(), p.getY(), p.getWidth(), p.getHeight(), null);
        }

        //Draw Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver){
            g.drawString("Game Over: " +  String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        //Move the FlappyBird
        flappyBird.mimicGravity();
        if (counter == 0){
            flappyBird.setImg();
            counter++;
        }
        else{
            counter = 0;
        }

        //Move the Pipes
        for (Pipe p : pipes){
            //Move pipes to the left
            p.changeX(p.getVelocityX());

            //If a pipe been passed, add score
            if (!p.isPassed() && flappyBird.getInitialX() > p.getX() + p.getWidth()){
                p.setPassed(true);
                score += 0.5;
            }

            //If collision happens, game is over
            if (collision(flappyBird, p)){
                gameOver = true;
            }
        }

        //Fall below the map
        if (flappyBird.getCurrentY() > boardHeight) {
            gameOver = true;
        }
    }

    //Method for detect collision
    public boolean collision(FlappyBird a, Pipe b){
        return a.getInitialX() < b.getX() + b.getWidth() &&
                a.getInitialX() +a.getWidth() > b.getX() &&
                a.getCurrentY() < b.getY() + b.getHeight() &&
                a.getCurrentY() + a.getHeight() > b.getY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Space is pressed
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            flappyBird.hop();
            if (gameOver){
                //restart the game by resetting the conditions
                flappyBird.setCurrentY(flappyBird.getInitialY());
                flappyBird.setVelocityY(0);
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        return;
    }
}
