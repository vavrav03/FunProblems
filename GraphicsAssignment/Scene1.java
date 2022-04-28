package kurz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.io.File;

public class Scene1 extends BasicScene {

    protected class House {

        private final Point[] points = {new Point(1, 3), new Point(1, 1), new Point(3, 1), new Point(3, 3), new Point(2, 4)};
        private final int[] path = {1, 2, 0, 3, 4, 0, 1, 3, 2};
        private final int[] xPoints = new int[path.length];
        private final int[] yPoints = new int[path.length];
        private Color color = Color.BLACK;

        public House() {
            recalculate();
        }

        private void recalculate() {
            for (int i = 0; i < path.length; i++) {
                xPoints[i] = pixelX(path[i]);
                yPoints[i] = pixelY(path[i]);
            }
        }

        public void paint(Graphics g) {
            g.setColor(color);
            g.drawPolyline(xPoints, yPoints, path.length);
        }

        private int pixelX(int i) {
            return 200 + (int) (20 * points[i].x);
        }

        private int pixelY(int i) {
            return 500 - (int) (20 * points[i].y);
        }

        public void transform(Matrix m) {
            for (Point p : points) {
                p.transform(m);
            }
            recalculate();
        }
    }
    protected final BufferedImage left, right;
    protected final House[] houses = new House[12];
    protected volatile int houseIndex = 0;
    protected final Timer timer = new Timer(50, e -> {
        houseIndex = (houseIndex + 1) % houses.length;
        repaint();
    });

    public Scene1() {
        super();
        left = readImage("sprites" + File.separator + "boar_ER.png");
        right = readImage("sprites" + File.separator + "gryphon_PE.png");
        Color[] colors = {Color.BLUE, Color.GREEN, Color.MAGENTA, Color.RED, Color.YELLOW, Color.ORANGE};
        for (int i = 0; i < houses.length; i++) {
            House house = houses[i] = new House();
            house.color = colors[i % colors.length];
            Matrix move = Matrix.createTranslationTransform(-2, -2);
            Matrix back = Matrix.createTranslationTransform(2, 2);
            Matrix scale = Matrix.createScaleTransform(i + 1);
            Matrix rotate = Matrix.createRotateTransform(i * Math.PI / 36);
            Matrix result = back.multiply(rotate).multiply(scale).multiply(move);
            house.transform(result);
        }
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintLeft(g);
        paintRight(g);
        paintHouse(g);
        g.fillOval(195, 495, 10, 10);
    }

    protected void paintLeft(Graphics g) {
        if (left != null) {
            g.drawImage(left, 250, 200, null);
        }
    }

    protected void paintRight(Graphics g) {
        if (right != null) {
            g.drawImage(right, 600, 105, 100, 100, null);
        }
    }

    protected void paintHouse(Graphics g) {
        houses[houseIndex].paint(g);
    }

    public static void main(String[] args) {
        createAndShowFrame(new Scene1());
    }
}
