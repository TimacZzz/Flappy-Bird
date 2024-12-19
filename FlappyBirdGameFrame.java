import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FlappyBirdGameFrame extends JFrame {
    private static final int BOARD_WIDTH = 360;
    private static final int BOARD_HEIGHT = 640;

    public FlappyBirdGameFrame(){
        // Basic Setting of the Frame
        setTitle("Flappy Bird");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(Objects.requireNonNull(FlappyBirdGameFrame.class.getResource("./Images/flappybird.png"))).getImage());

        // Add the Panel to the Frame
        Controller controller = new Controller(BOARD_WIDTH, BOARD_HEIGHT);
        add(controller, BorderLayout.CENTER);
        pack();

        controller.requestFocus();

        // Start the game
        controller.startGame();
    }
}
