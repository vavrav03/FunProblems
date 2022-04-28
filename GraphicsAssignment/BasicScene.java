package kurz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class BasicScene extends JComponent {

    private final BufferedImage background;
    private final Dimension dimension;
    private static final double WIDTH_FRONT = 1275;
    private static final double WIDTH_BACK = 555;
    private static final double HEIGHT = 95;
    private static final double Y_OFFSET = 707;

    public BasicScene() {
        background = readImage("sprites" + File.separator + "sky_background.png");
        System.out.println(background);
        System.out.println(new File("sprites" + File.separator + "sky_background.png").getAbsolutePath());
        dimension = background == null ? null : new Dimension(background.getWidth(null), background.getHeight(null));
        setBackground(Color.BLACK);
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension == null ? super.getPreferredSize() : dimension;
    }

    @Override
    public Dimension getMinimumSize() {
        return dimension == null ? super.getMinimumSize() : dimension;
    }

    @Override
    public Dimension getMaximumSize() {
        return dimension == null ? super.getMaximumSize() : dimension;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, null);
        }
    }

    public static double computeReduction(double depth) {
        return 1 - (1 - (WIDTH_BACK / WIDTH_FRONT * depth));
    }

    public static int computeYPostion(int reducedHeight, double depth) {
        return (int) (Y_OFFSET - HEIGHT * depth - reducedHeight);
    }

    protected static BufferedImage readImage(String fileName) {
        return readImage(new File("sprites" + File.separator + fileName));
    }

    protected static BufferedImage readImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException ex) {
            return null;
        }
    }

    protected static BufferedImage createMirroredImage(BufferedImage source) {
        if (source == null) {
            return null;
        }
        int width = source.getWidth(null);
        int height = source.getHeight(null);
        BufferedImage destination = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                destination.setRGB(width - x - 1, y, source.getRGB(x, y));
            }
        }
        return destination;
    }

    protected static BufferedImage createReducedImage(BufferedImage source, double ratio) {
        if (source == null) {
            return null;
        }
        int sourceWidth = source.getWidth(null);
        int sourceHeight = source.getHeight(null);
        int destinationWidth = (int) (sourceWidth * ratio);
        int destinationHeight = (int) (sourceHeight * ratio);
        BufferedImage destination = new BufferedImage(destinationWidth, destinationHeight, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < destinationWidth; x++) {
            for (int y = 0; y < destinationHeight; y++) {
                destination.setRGB(x, y, source.getRGB((int) (x / ratio), (int) (y / ratio)));
            }
        }
        return destination;
    }

    protected static JFrame createAndShowFrame(BasicScene scene) {
        JFrame frame = new JFrame(scene.getClass().getSimpleName());
        frame.getContentPane().setBackground(Color.BLACK);
        frame.getContentPane().add(scene);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        return frame;
    }

    public static void main(String[] args) {
        createAndShowFrame(new BasicScene());
    }
}
