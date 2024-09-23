package game.snake;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GenerateImages {

    public static void main(String[] args) {
        generateDotImage("src/resources/dot.png");
        generateAppleImage("src/resources/apple.png");
        generateHeadImage("src/resources/head.png");
    }

    private static void generateDotImage(String path) {
        int size = 10;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.GREEN);
        g2d.fillOval(0, 0, size, size);

        g2d.dispose();
        saveImage(image, path);
    }

    private static void generateAppleImage(String path) {
        int size = 10;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.RED);
        g2d.fillOval(0, 0, size, size);

        g2d.dispose();
        saveImage(image, path);
    }

    private static void generateHeadImage(String path) {
        int size = 10;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.BLUE);
        g2d.fillOval(0, 0, size, size);

        g2d.dispose();
        saveImage(image, path);
    }

    private static void saveImage(BufferedImage image, String path) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs(); // Create directories if they don't exist
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
