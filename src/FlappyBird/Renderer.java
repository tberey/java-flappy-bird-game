package FlappyBird;

import java.awt.Graphics;
import javax.swing.JPanel;

public class Renderer extends JPanel {
    
    private static final long serialVersionUID = 1L; // For warning.

    // Recursive, to continue rendering itself, when looped.
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g); // Calls method from parent class (JPanel, which is extended to this class).
        FlappyBird.flappybird.repaint(g);
    }
}