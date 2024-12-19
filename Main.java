import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Configure from Event Dispatch Thread
        EventQueue.invokeLater(() -> {
            var game = new FlappyBirdGameFrame();
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.setVisible(true);
        });
    }
}