import java.util.Random;

public class Tetromino {
    private int[][] shape;
    private int x, y;

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    private int color;
    private static final int[][][] SHAPES = {
        {{1,1,1,1}}, // I
        {{1,1},{1,1}}, // O
        {{1,1,1},{0,1,0}}, // T
        {{1,1,1},{1,0,0}}, // L
        {{1,1,1},{0,0,1}}, // J
        {{1,1,0},{0,1,1}}, // S
        {{0,1,1},{1,1,0}}  // Z
    };
    private static final int[] COLORS = {
        0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00,
        0xFF00FF, 0x00FFFF, 0xFFA500
    };

    public Tetromino() {
        Random rand = new Random();
        int type = rand.nextInt(7);
        shape = SHAPES[type];
        color = COLORS[type];
        x = 4 - shape[0].length/2;
        y = -shape.length;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getColor() {
        return color;
    }

    public void rotate() {
        int[][] newShape = new int[shape[0].length][shape.length];
        for(int i = 0; i < shape.length; i++) {
            for(int j = 0; j < shape[0].length; j++) {
                newShape[j][shape.length-1-i] = shape[i][j];
            }
        }
        shape = newShape;
    }

    public void rotateBack() {
        rotate();
        rotate();
        rotate();
    }
}