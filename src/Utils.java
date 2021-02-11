import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Utils {
  
  /**
   * Read Image RGB
   * Reads the image of given width and height at the given imgPath into the provided BufferedImage.
   */
  public static void readImageRGB(int width, int height, String imgPath, BufferedImage img, RGB_Data rgb_data) {
    try {
      int frameLength = width * height * 3;
      
      File file = new File(imgPath);
      RandomAccessFile raf = new RandomAccessFile(file, "r");
      raf.seek(0);
      
      byte[] bytes = new byte[frameLength];
      
      raf.read(bytes);
      
      int ind = 0;
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          byte r = bytes[ind];
          byte g = bytes[ind + height * width];
          byte b = bytes[ind + height * width * 2];
          
          int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
          //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
          img.setRGB(x, y, pix);
          rgb_data.setRGB(x, y, pix);
          ind++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static void showIms(BufferedImage imgOne) {
    // Use label to display the image
    JFrame frame = new JFrame();
    GridBagLayout gLayout = new GridBagLayout();
    frame.getContentPane().setLayout(gLayout);
    
    JLabel lbIm1 = new JLabel(new ImageIcon(imgOne));
    
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;
    c.weightx = 0.5;
    c.gridx = 0;
    c.gridy = 1;
    
    frame.getContentPane().add(lbIm1, c);
    
    frame.pack();
    frame.setVisible(true);
  }
  
  public static double[] dotProduct(double[][] coefficients, double[] original) {
    double[] result = new double[3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        result[i] += coefficients[i][j] * original[j];
      }
    }
    return result;
  }
}
