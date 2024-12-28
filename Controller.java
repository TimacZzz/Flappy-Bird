import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Controller extends JPanel implements ActionListener, MouseListener{
    private final int boardWidth;
    private final int boardHeight;
    private int counter;
    private int score;
    private boolean gameOver;
    private boolean isInMenu;
    private boolean startedGame;
    private boolean registeredListener;

    // BK Image
    private final Image backgroundImg;

    // Title
    private final Image title;

    // Bird Image
    private Image bird;

    // Top Pipe Image
    private Image topPipe;

    // Medal Images
    private Image bronzeMedal;
    private Image silverMedal;
    private Image goldMedal;

    // Ready Image
    private Image getReady;
    private Image instruction;

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

    // Files for sound effects
    private File scoreSoundEffect;
    private File hitGroundSoundEffect;

    // File for storing best score
    private File scoreFile;

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
        registeredListener = false;

        // load images
        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/bk.png"))).getImage();
        title = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/title.png"))).getImage();
        bird = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/birdMid.png"))).getImage();
        gameOverImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/gameOver.png"))).getImage();
        medalPanel = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/medalPanel.png"))).getImage();
        topPipe = new ImageIcon(Objects.requireNonNull(Pipe.class.getResource("./Sprites/upPipe.png"))).getImage();

        // Medals
        bronzeMedal = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/bronze.png"))).getImage();
        silverMedal = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/silver.png"))).getImage();
        goldMedal = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/gold.png"))).getImage();

        // Get Ready Phase
        getReady = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/getReady.png"))).getImage();
        instruction = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/instruction.png"))).getImage();

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

        // Sound Effects
        scoreSoundEffect = new File("./src/Sound Effects/score.wav");
        hitGroundSoundEffect = new File("./src/Sound Effects/hitGround.wav");

        // Score File
        scoreFile = new File("./src/score.txt");
        if (!scoreFile.exists()) {
            try {
                scoreFile.createNewFile();
                FileWriter writer = new FileWriter(scoreFile);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write("0");
                bufferedWriter.flush();
                bufferedWriter.close();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
//        setFocusable(true);
//        addMouseListener(this);
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

            //setFocusable(true);
            if (!registeredListener){
                addMouseListener(this);
                registeredListener = true;
            }

            if(!startedGame){
                drawGame(g);
                drawReady(g);
                repaint();
            }
            else{
                drawGame(g);
                repaint();
            }

        }
    }

    public void drawReady(Graphics g){
        // Draw the bird
        g.drawImage(flappyBird.getCurrentImg(), flappyBird.getInitialX(), flappyBird.getCurrentY(),
                flappyBird.getWidth(), flappyBird.getHeight(), null);

        g.drawImage(getReady, 57, 100, 174, 44, null);

        g.drawImage(instruction, 125, 170, 78, 98, null);
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
            drawBestScore(g);
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
        int value = score;
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

    // Draw Score & Medals on medal board
    public void drawBestScore(Graphics g){
        try{
            FileReader reader = new FileReader(scoreFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            int value = Integer.parseInt(bufferedReader.readLine());

            Image imageOnes;
            Image imageTens;
            Image imageHundreds;

            // Check how many digits score has:\
            if (value > 999){
                imageOnes = findNumber(9);
                imageTens = findNumber(9);
                imageHundreds = findNumber(9);
                g.drawImage(imageHundreds, 201, 225, 11, 15, null);
                g.drawImage(imageTens, 213, 225, 11, 15, null);
                g.drawImage(imageOnes, 225, 225, 11, 15, null);
            }
            else if (value >= 100){
                int hundreds = value / 100;
                int tens = (value - hundreds * 100) / 10;
                int ones = (value - hundreds * 100 - tens * 10);
                imageOnes = findNumber(ones);
                imageTens = findNumber(tens);
                imageHundreds = findNumber(hundreds);
                g.drawImage(imageHundreds, 201, 225, 11, 15, null);
                g.drawImage(imageTens, 213, 225, 11, 15, null);
                g.drawImage(imageOnes, 225, 225, 11, 15, null);
            }
            else if (value >= 10){
                int tens = value / 10;
                int ones = (value - tens * 10);
                imageOnes = findNumber(ones);
                imageTens = findNumber(tens);
                g.drawImage(imageTens, 213, 225, 11, 15, null);
                g.drawImage(imageOnes, 225, 225, 11, 15, null);
            }
            else{
                imageOnes = findNumber(value);
                g.drawImage(imageOnes, 225, 225, 11, 15, null);
            }
        }
        catch(Exception e){
            e.printStackTrace();
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
            if (p.getImg() == topPipe){
                if (!p.isPassed() && flappyBird.getInitialX() > p.getX() + p.getWidth()){
                    p.setPassed(true);
                    score += 1;
                    try {
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(scoreSoundEffect);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //If collision happens, game is over
            if (collision(flappyBird, p)){
                gameOver = true;
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(hitGroundSoundEffect);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(hitGroundSoundEffect);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            removeMouseListener(this);
            try{
                FileReader reader = new FileReader(scoreFile);
                BufferedReader bufferedReader = new BufferedReader(reader);
                int previousBestScore = Integer.parseInt(bufferedReader.readLine());
                if (score > previousBestScore){
                    FileWriter writer = new FileWriter(scoreFile);
                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                    bufferedWriter.write(Integer.toString(score));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    writer.close();
                }
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
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
        registeredListener = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!startedGame){
            startGame();
            startedGame = true;
        }

        flappyBird.hop();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
