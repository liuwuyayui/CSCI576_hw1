import java.awt.image.BufferedImage;

public class Main {
  private static final int WIDTH = 352;
  private static final int HEIGHT = 288;
  private static final int Q_LOWER_BOUND = 0;
  private static final int Q_UPPER_BOUND = 255;
  
  public static void main(String[] args) {
    String originalFile = args[0];
    int ySubRatio = Integer.parseInt(args[1]);
    int uSubRatio = Integer.parseInt(args[2]);
    int vSubRatio = Integer.parseInt(args[3]);
    int qLevel = Integer.parseInt(args[4]);
    
    RGB_Data data = new RGB_Data(WIDTH, HEIGHT);
    
    
    BufferedImage originalImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    // Create a empty RGB format data set
    RGB_Data rgb_data = new RGB_Data(WIDTH, HEIGHT);
    // Read the given file into "rgb_data"
    Utils.readImageRGB(WIDTH, HEIGHT, originalFile, originalImg, rgb_data);
    // Show the original Image
    Utils.showIms(originalImg);
    
    // RGB -> YUV -> Subsampling -> Adjust -> RGB -> Quantization
    BufferedImage restoredImg = rgb_data.toYuvData()
                                        .subSampleAndAdjust(ySubRatio, uSubRatio, vSubRatio)
                                        .toRgbData()
                                        .quantize(Q_LOWER_BOUND, Q_UPPER_BOUND, qLevel)
                                        .toBufferedImage();
    Utils.showIms(restoredImg);
  }
}
