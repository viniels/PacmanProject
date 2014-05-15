/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.pacman;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.*;
import project.pacman.Graphics.*;

/**
 *
 * @author Vincent
 */
public class Game extends Canvas implements Runnable {

    public static final long serialVersionUID = 1L;
    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;
    private boolean running = false;
    public Timer timer;
    private Thread thread;
    private JFrame frame;
    private Screen screen;
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);

        screen = new Screen(width, height);
        frame = new JFrame();
    }

    //starting the game
    public synchronized void start() {
        running = true;
        thread = new Thread(this, "display");
        thread.start();
    }
    //stopping the game

    public synchronized void stop() {

        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    // the game loop
    @Override
    public void run() {
        while (running) {
            update();
            render();

        }

    }

    public void update() {
    }

    public void render() {

        // updating the screen via triple buffering
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        
        screen.clear();
        screen.render();
        
        for(int i =0 ; i < pixels.length; i++){
            pixels[i] = screen.pixels[i];
        }
        
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image,0,0, getWidth(), getHeight(),null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        // create game instancd
        Game game = new Game();

        // setting up the frame
        game.frame.setResizable(false);
        game.frame.setTitle("Pacman");
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        // intitialize game
        game.start();



    }
}
