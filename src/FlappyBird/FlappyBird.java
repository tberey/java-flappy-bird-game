package FlappyBird;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static void main(String[] args) throws Exception {

        flappybird = new FlappyBird();
    }

    public static FlappyBird flappybird;
    public Renderer renderer;
    public Rectangle bird;
    public int ticks, yMotion, score;
    public ArrayList<Rectangle> columns;
    public Random rand;
    public boolean gameOver, started;

    public final int WIDTH = 800, HEIGHT = 800;

    public FlappyBird() {

        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        rand = new Random();

        jframe.add(renderer);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setTitle("Flappy Bird");
        jframe.setVisible(true);

        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean startGame) {

        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300); // Min height 50, max height 300.

        if (startGame) {

            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height)); // Place
                                                                                                                    // a
                                                                                                                    // column/pipe.
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space)); // Place
                                                                                                                       // a
                                                                                                                       // column/pipe.
        } else {

            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height)); // Place
                                                                                                                       // a
                                                                                                                       // column/pipe.
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space)); // Place a
                                                                                                              // column/pipe.
        }

    }

    public void paintColumn(Graphics g, Rectangle column) {

        g.setColor(Color.green.darker().darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump() {

        if (gameOver) {

            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }

        if (!started) {

            started = true;
        } else if (!gameOver) {

            if (yMotion > 0) {
                yMotion = 0;
            }

            yMotion -= 10;
        }
    }

    // To render multiple times, use our Renderer.
    public void actionPerformed(ActionEvent arg0) {

        int speed = 10;

        ticks++;

        if (started) {

            for (int i = 0; i < columns.size(); i++) {

                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) {

                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {

                Rectangle column = columns.get(i);

                if (column.x + column.width < 0) {

                    columns.remove(column);

                    if (column.y == 0) {

                        addColumn(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle column : columns) {

                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {

                    score++;
                }

                if (column.intersects(bird)) {

                    gameOver = true;

                    if (bird.x <= column.x) {

                        bird.x = column.x - bird.width;

                    } else {
                        
                        if (column.y != 0) {
                            
                            bird.y = column.y - bird.height;

                        } else if (bird.y < column.height) {
                            
                            bird.y = column.height;
                        }
                    }
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) {

                gameOver = true;
            }

            if (bird.y + yMotion >= HEIGHT - 120) {

                bird.y = HEIGHT - 120 - bird.height;
            }
        }
        renderer.repaint();
    }

    public void repaint(Graphics g) {
        // Background Color.
        g.setColor(Color.cyan);
        g.fillRect(0, 0, HEIGHT, WIDTH);

        // Add Ground.
        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        // Add Grass.
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        // Bird (player) icon.
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.height, bird.width);

        for (Rectangle column : columns) {

            paintColumn(g, column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 80));

        if (!started) {

            g.drawString("Click to Begin", 75, HEIGHT / 2 - 50);
        }

        if (gameOver) {

            g.drawString("Game Over", 100, HEIGHT / 2 - 50);
        }

        if (!gameOver && started) {

            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }

    public void mouseClicked(MouseEvent e) {

        jump();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() ==  KeyEvent.VK_SPACE) {
            jump();
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void keyReleased(KeyEvent e) {}
}