package game;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


public class Tetris extends JPanel implements ActionListener {
    public static final int SCALE = 25;
    public static final int WIDTH = 15;
    public static final int HEIGHT = 25;
    public static final int SPEED = 4;
    public static int[][] matrix = new int[HEIGHT][WIDTH];
    public  Queue<KeyEvent> keyEvents = new ArrayBlockingQueue<KeyEvent>(100);




    Figure figure = null;
    Timer t = new Timer(1000/SPEED, this);
    public boolean isGameOver;
    private static int score = 0;
    private static int scoreOnStep = 0;
    Image img = new ImageIcon(getClass().getResource("/cell.jpg")).getImage();


    public Tetris(){
        t.start();
        addKeyListener(new Keyboard());
        setFocusable(true);
        isGameOver = false;
        figure = FigureFactory.createRandomFigure(WIDTH/2 - 1, 0);
    }


    public int[][] getMatrix(){
        return matrix;
    }
    public static Integer getValue(int x, int y){
        if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT)
            return matrix[y][x];
        return null;
    }
    public static void setValue(int x, int y, int value){
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT)
            matrix[y][x] = value;
    }
    public static void removeFullLines(){
        ArrayList<int[]> lines = new ArrayList<int[]>();
        for (int i = 0; i < HEIGHT; i++){
            int count = 0;
            for  (int j = 0; j < WIDTH; j++){
                count += matrix[i][j];
            }
            if (count != WIDTH){
                lines.add(matrix[i]);
            }
        }
        while(lines.size() < HEIGHT){
            lines.add(0, new int[WIDTH]);
            scoreOnStep += 100;
        }
        matrix = lines.toArray(new int[HEIGHT][WIDTH]);
    }
    public String filePath(String s){
        String result = getClass().getResource(s).getFile();
        result = result.substring(result.indexOf("/"));
        return result;
    }


    public void paint(Graphics g) {
        g.setColor(color(99, 99, 99));
        g.fillRect(1, 0, WIDTH * SCALE - 1, HEIGHT * SCALE);
        g.setColor(color(110, 110, 99));
        for(int xx = 0; xx <= WIDTH*SCALE; xx += SCALE){
            g.drawLine(xx, 0, xx, HEIGHT*SCALE);
        }
        for(int yy = 0; yy <= HEIGHT*SCALE; yy += SCALE){
            g.drawLine(0, yy, WIDTH*SCALE, yy);
        }
        g.setColor(color(33, 00, 99));
        int[][] canvas = new int[HEIGHT][WIDTH];
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){
                canvas[i][j] = matrix[i][j];
            }
        }
        int left = figure.getPosX();
        int top = figure.getPosY();
        int[][] brickMatrix = figure.getMatrix();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (top + i >= HEIGHT || left + j >= WIDTH) continue;
                if (brickMatrix[i][j] == 1)
                    canvas[top + i][left + j] = 2;
            }
        }
        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){
                int index = canvas[i][j];
                if (index == 1)
                    g.drawImage(img, j * SCALE, i*SCALE, null);
                else if (index == 2)
                    g.drawImage(img, j * SCALE, i*SCALE, null);
            }
        }
        g.setColor(color(33, 66, 00));
        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);
        g.drawString("YOUR SCORE: " + score, 10, 30);
    }

    public Color color(int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tetris v1.0 beta");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(WIDTH * SCALE + 7, HEIGHT * SCALE + 30);
        f.setLocationRelativeTo(null);
        f.add(new Tetris());
        f.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        figure.down();
        if (!figure.isCurrentPositionAvailable()){
            figure.up();
            figure.landed();
            isGameOver = figure.getPosY() <= 1;
            removeFullLines();
            score+=scoreOnStep;
            scoreOnStep = 0;
            figure = FigureFactory.createRandomFigure(WIDTH/2 - 1, 0);
            keyEvents.clear();
        }
        if (!keyEvents.isEmpty()){
            int kEvt = keyEvents.poll().getKeyCode();
            action(kEvt);
        }
        if(isGameOver){
            t.stop();
            JOptionPane.showMessageDialog(null, "Game Over!");
        }
        repaint();
    }

    private void action (int key){
        if (key == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if (key == KeyEvent.VK_RIGHT){
            figure.right();
            Sound.playSound(getClass().getResource("/SFX_Move.wav")).join();
        }
        if (key == KeyEvent.VK_LEFT){
            figure.left();
            Sound.playSound(getClass().getResource("/SFX_Move.wav")).join();
        }
        if (key == KeyEvent.VK_SPACE) {
            figure.rotate();
            Sound.playSound(getClass().getResource("/SFX_Rotate.wav")).join();
        }
        if (key == KeyEvent.VK_DOWN){
            figure.downMaximum();
            Sound.playSound(getClass().getResource("/SFX_Drop.wav")).join();
        }
    }

    private class Keyboard extends KeyAdapter{

        public void keyPressed(KeyEvent kEvt){
            keyEvents.add(kEvt);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(!keyEvents.isEmpty()) {
                keyEvents.clear();
                keyEvents.add(e);
            }
        }
    }
}
