import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoard extends JPanel implements KeyListener {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int BLOCK_SIZE = 30;
    private int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];
    private Tetromino currentPiece;
    private Timer gameTimer;
    private int score = 0;

    public GameBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        
        gameTimer = new Timer(1000, e -> {
            if(!movePieceDown()) {
                mergePieceToBoard();
                checkLines();
                currentPiece = new Tetromino();
            }
            repaint();
        });
        gameTimer.start();
        currentPiece = new Tetromino();
    }

    private boolean movePieceDown() {
        if(collisionCheck(0, 1)) {
            currentPiece.setY(currentPiece.getY() + 1);
            return true;
        }
        return false;
    }

    private void mergePieceToBoard() {
        int[][] shape = currentPiece.getShape();
        for(int y = 0; y < shape.length; y++) {
            for(int x = 0; x < shape[y].length; x++) {
                if(shape[y][x] != 0) {
                    int boardY = currentPiece.getY() + y;

                    board[boardY][currentPiece.getX() + x] = shape[y][x];
                    System.out.println("合并方块到 ("+(currentPiece.getX() + x)+","+boardY+") 值="+shape[y][x]);
                }
            }
        }
    }

    private void checkLines() {
        for(int y = BOARD_HEIGHT-1; y >= 0; y--) {
            if(isLineFull(y)) {
                removeLine(y);
                score += 100;
                y++;
            }
        }
    }

    private boolean isLineFull(int y) {
        for(int x = 0; x < BOARD_WIDTH; x++) {
            if(board[y][x] == 0) return false;
        }
        return true;
    }

    private void removeLine(int line) {
        for(int y = line; y > 0; y--) {
            System.arraycopy(board[y-1], 0, board[y], 0, BOARD_WIDTH);
        }
    }

    private boolean collisionCheck(int xMove, int yMove) {
        int[][] shape = currentPiece.getShape();
        for(int y = 0; y < shape.length; y++) {
            for(int x = 0; x < shape[y].length; x++) {
                if(shape[y][x] != 0) {
                    int newX = currentPiece.getX() + x + xMove;
                    int newY = currentPiece.getY() + y + yMove;
                    if(newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT) return false;
                    if(newY >= 0 && board[newY][newX] != 0) return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawCurrentPiece(g);
    }

    private void drawBoard(Graphics g) {
        for(int y = 0; y < BOARD_HEIGHT; y++) {
            for(int x = 0; x < BOARD_WIDTH; x++) {
                if(board[y][x] != 0) {
                    g.setColor(new Color(board[y][x]));
                    g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE-1, BLOCK_SIZE-1);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics g) {
        int[][] shape = currentPiece.getShape();
        g.setColor(new Color(currentPiece.getColor()));
        for(int y = 0; y < shape.length; y++) {
            for(int x = 0; x < shape[y].length; x++) {
                if(shape[y][x] != 0) {
                    g.fillRect((currentPiece.getX() + x) * BLOCK_SIZE, 
                              (currentPiece.getY() + y) * BLOCK_SIZE, 
                              BLOCK_SIZE-1, BLOCK_SIZE-1);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(collisionCheck(-1, 0)) currentPiece.setX(currentPiece.getX() - 1);
                break;
            case KeyEvent.VK_RIGHT:
                if(collisionCheck(1, 0)) currentPiece.setX(currentPiece.getX() + 1);
                break;
            case KeyEvent.VK_DOWN:
                if(collisionCheck(0, 1)) currentPiece.setY(currentPiece.getY() + 1);
                break;
            case KeyEvent.VK_UP:
                currentPiece.rotate();
                if(!collisionCheck(0, 0)) currentPiece.rotateBack();
                break;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}