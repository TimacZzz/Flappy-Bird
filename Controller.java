import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

public class Controller extends JPanel implements ActionListener, KeyListener{
    private final int boardWidth;
    private final int boardHeight;
    private int counter;
    private double score;
    private boolean gameOver;
    private boolean isInMenu;
    private boolean startedGame;

    // BK Image
    private final Image backgroundImg;

    // Title
    private final Image title;

    // Bird Image
    private Image bird;

    // Medal Images
    private Image bronzeMedal;
    private Image silverMedal;
    private Image goldMedal;

    // Score Number Image
    private Image zero;
    private Image one;
    private Image two;
    private Image three;
    private Image four;
    private Image five;
    private Image six;
    private Image seven;
    private Image eight;
    private Image nine;

    // GamwOver
    private Image gameOverImage;
    private Image medalPanel;

    // Moving Ground
    private MovingGround ground;

    // Flappy Bird
    private FlappyBird flappyBird;

    // List of pipes & grounds
    private ArrayList<Pipe> pipes;
    private ArrayList<MovingGround> grounds;

    // Timers
    private Timer gameLoop;
    private Timer placePipeTimer;
    private Timer groundTimer;

    // Buttons
    private JButton startButton;
    private JButton scoreButton;
    private JButton menuButton;


    Controller(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        counter = 0;
        score = 0;
        gameOver = false;
        isInMenu = true;
        startedGame = false;

        // load images
        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/bk.png"))).getImage();
        title = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/title.png"))).getImage();
        bird = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/birdMid.png"))).getImage();
        gameOverImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/gameOver.png"))).getImage();
        medalPanel = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/medalPanel.png"))).getImage();

        // Medals
        bronzeMedal = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/bronze.png"))).getImage();
        silverMedal = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/silver.png"))).getImage();
        goldMedal = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/gold.png"))).getImage();

        zero = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/0.png"))).getImage();
        one = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/1.png"))).getImage();
        two = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/2.png"))).getImage();
        three = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/3.png"))).getImage();
        four = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/4.png"))).getImage();
        five = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/5.png"))).getImage();
        six = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/6.png"))).getImage();
        seven = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/7.png"))).getImage();
        eight = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/8.png"))).getImage();
        nine = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/9.png"))).getImage();

        ground = new MovingGround();
        flappyBird = new FlappyBird();
        pipes = new ArrayList<>();
        grounds = new ArrayList<>();

        // game timer
        gameLoop = new Timer(1000 / 60, this);
        placePipeTimer = new Timer(1500, e -> Pipe.placePipes(pipes));
        groundTimer = new Timer(1100, e -> MovingGround.placeGround(grounds));

        // Set the size of the Panel
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        // buttons
        startButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/start.png"))));
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                startButton.setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                isInMenu = false;
            }
        });

        scoreButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/score.png"))));
        scoreButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scoreButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                scoreButton.setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Load the online score
            }
        });

        menuButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/menu.png"))));
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                menuButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                menuButton.setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Back to menu interface
                restoreSetting();
                isInMenu = true;
                repaint();
            }
        });

        customButton(startButton);
        customButton(scoreButton);
        customButton(menuButton);
        add(scoreButton);
        add(startButton);
        add(menuButton);

    }

    public void customButton(JButton button){
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusable(false);
    }

    // To-Do
    public void drawMenu(Graphics g){
        //Draw Background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //Draw the title
        g.drawImage(title, 30, 100, 192, 44, null);

        //Draw the bird beside the title
        g.drawImage(bird, 230, 107, 34, 24, null);

        //Draw the Moving Ground
        g.drawImage(ground.getImg(), ground.getCurrentX(), ground.getInitialY(), ground.getWidth(), ground.getHeight(), null);


        Dimension buttonSize = startButton.getPreferredSize();
        startButton.setBounds(104, 200, buttonSize.width, buttonSize.height);
        scoreButton.setBounds(104, 250, buttonSize.width, buttonSize.height); // Buttons are same size
    }

    public void startGame(){
        gameLoop.start();
        setFocusable(true);
        addKeyListener(this);
        placePipeTimer.start();
        groundTimer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        menuButton.setVisible(false);
        if (isInMenu){
            startButton.setVisible(true);
            scoreButton.setVisible(true);
            drawMenu(g);
        }
        else{
            startButton.setVisible(false);
            scoreButton.setVisible(false);
            drawGame(g);
            if(!startedGame){
                startGame();
                startedGame = true;
            }
        }
    }

    public void drawGame(Graphics g){
        //Draw Background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //Draw the Moving Ground
        g.drawImage(ground.getImg(), ground.getCurrentX(), ground.getInitialY(), ground.getWidth(), ground.getHeight(), null);

        //Draw FlappyBird
        g.drawImage(flappyBird.getCurrentImg(), flappyBird.getInitialX(), flappyBird.getCurrentY(),
                    flappyBird.getWidth(), flappyBird.getHeight(), null);

        //Draw Pipes
        for (Pipe p : pipes){
            g.drawImage(p.getImg(), p.getX(), p.getY(), p.getWidth(), p.getHeight(), null);
        }

        //Draw Grounds
        for (MovingGround gr: grounds){
            g.drawImage(gr.getImg(), gr.getCurrentX(), gr.getInitialY(), gr.getWidth(), gr.getHeight(), null);
        }

        if (gameOver){
            g.drawImage(gameOverImage, 50, 100, 188, 38, null);
            g.drawImage(medalPanel, 31, 150, 226, 116, null);
            drawContentOfMedalPanel(g);
            Dimension buttonSize = menuButton.getPreferredSize();
            menuButton.setBounds(104, 280, buttonSize.width, buttonSize.height);
            menuButton.setVisible(true);
        }
        else{
            drawScore(g);
        }
    }

    // Draw Score & Medals on medal board
    public void drawContentOfMedalPanel(Graphics g){
        int value = (int) score;
        Image imageOnes;
        Image imageTens;
        Image imageHundreds;

        // Check how many digits score has:\
        if (value > 999){
            imageOnes = findNumber(9);
            imageTens = findNumber(9);
            imageHundreds = findNumber(9);
            g.drawImage(imageHundreds, 201, 185, 11, 15, null);
            g.drawImage(imageTens, 213, 185, 11, 15, null);
            g.drawImage(imageOnes, 225, 185, 11, 15, null);
            g.drawImage(goldMedal, 58, 190, 48, 48, null);
        }
        else if (value >= 100){
            int hundreds = value / 100;
            int tens = (value - hundreds * 100) / 10;
            int ones = (value - hundreds * 100 - tens * 10);
            imageOnes = findNumber(ones);
            imageTens = findNumber(tens);
            imageHundreds = findNumber(hundreds);
            g.drawImage(imageHundreds, 201, 185, 11, 15, null);
            g.drawImage(imageTens, 213, 185, 11, 15, null);
            g.drawImage(imageOnes, 225, 185, 11, 15, null);
            g.drawImage(goldMedal, 58, 190, 48, 48, null);
        }
        else if (value >= 10){
            int tens = value / 10;
            int ones = (value - tens * 10);
            imageOnes = findNumber(ones);
            imageTens = findNumber(tens);
            g.drawImage(imageTens, 213, 185, 11, 15, null);
            g.drawImage(imageOnes, 225, 185, 11, 15, null);
            g.drawImage(silverMedal, 58, 190, 48, 48, null);
        }
        else{
            imageOnes = findNumber(value);
            g.drawImage(imageOnes, 225, 185, 11, 15, null);
            g.drawImage(bronzeMedal, 58, 190, 48, 48, null);
        }
    }

    // Draw Score
    public void drawScore(Graphics g){
        int value = (int) score;
        Image imageOnes;
        Image imageTens;
        Image imageHundreds;

        // Check how many digits score has:\
        if (value > 999){
            imageOnes = findNumber(9);
            imageTens = findNumber(9);
            imageHundreds = findNumber(9);
            g.drawImage(imageHundreds, 118, 50, 14, 20, null);
            g.drawImage(imageTens, 137, 50, 14, 20, null);
            g.drawImage(imageOnes, 156, 50, 14, 20, null);
        }
        else if (value >= 100){
            int hundreds = value / 100;
            int tens = (value - hundreds * 100) / 10;
            int ones = (value - hundreds * 100 - tens * 10);
            imageOnes = findNumber(ones);
            imageTens = findNumber(tens);
            imageHundreds = findNumber(hundreds);
            g.drawImage(imageHundreds, 120, 50, 14, 20, null);
            g.drawImage(imageTens, 137, 50, 14, 20, null);
            g.drawImage(imageOnes, 154, 50, 14, 20, null);
        }
        else if (value >= 10){
            int tens = value / 10;
            int ones = (value - tens * 10);
            imageOnes = findNumber(ones);
            imageTens = findNumber(tens);
            g.drawImage(imageTens, 129, 50, 14, 20, null);
            g.drawImage(imageOnes, 146, 50, 14, 20, null);
        }
        else{
            imageOnes = findNumber(value);
            g.drawImage(imageOnes, 137, 50, 14, 20, null);
        }
    }

    // Find correct number
    public Image findNumber(int number){
        return switch (number) {
            case 0 -> zero;
            case 1 -> one;
            case 2 -> two;
            case 3 -> three;
            case 4 -> four;
            case 5 -> five;
            case 6 -> six;
            case 7 -> seven;
            case 8 -> eight;
            case 9 -> nine;
            default -> zero;
        };
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

        // Move the ground
        ground.moveGround();

        for (MovingGround g : grounds){
            g.moveGround();
        }

        //Fall below the map
        if (flappyBird.getCurrentY() > ground.getInitialY() - flappyBird.getHeight()) {
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
            gameLoop.stop();
            placePipeTimer.stop();
        }
    }

    // Restore the setting before starting the game
    public void restoreSetting(){
        flappyBird.setCurrentY(flappyBird.getInitialY());
        flappyBird.setVelocityY(0);
        pipes.clear();
        ground.setCurrentX(ground.getInitialX());
        grounds.clear();
        score = 0;
        gameOver = false;
        startedGame = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Space is pressed
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            flappyBird.hop();
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
