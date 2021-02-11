import java.awt.image.BufferedImage;

public class RGB_Data {
  public static final double[][] TRANSFER_COEFFICIENTS = {
          {0.299, 0.587, 0.114},
          {0.596, -0.274, -0.322},
          {0.211, -0.523, 0.312}
  };
  
  static class Pixel {
    double r;
    double g;
    double b;
    
    Pixel(int rgb) {
      this.r = (rgb >> 16) & 0xff;
      this.g = (rgb >> 8) & 0xff;
      this.b = rgb & 0xff;
    }
    
    Pixel(double r, double g, double b) {
      this.r = r;
      this.g = g;
      this.b = b;
    }
    
    public double[] getRgbArray() {
      double[] result = new double[3];
      result[0] = r;
      result[1] = g;
      result[2] = b;
      return result;
    }
  }
  
  private final Pixel[][] data;
  private final int width;
  private final int height;
  
  public RGB_Data(int width, int height) {
    this.width = width;
    this.height = height;
    data = new Pixel[height][width];
  }
  
  public void setRGB(int x, int y, int rgb) {
    data[y][x] = new Pixel(rgb);
  }
  
  public void setRGB(int y, int x, double[] rgb) {
    data[y][x] = new Pixel(rgb[0], rgb[1], rgb[2]);
  }
  
  public YUV_Data toYuvData() {
    YUV_Data result = new YUV_Data(width, height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        double[] rgbArray = data[y][x].getRgbArray();
        double[] yuvArray = rgbToYuv(rgbArray);
        result.setYUV(y, x, yuvArray);
      }
    }
    return result;
  }
  
  private double[] rgbToYuv(double[] rgb) {
    return Utils.dotProduct(TRANSFER_COEFFICIENTS, rgb);
  }
  
  public RGB_Data quantize(int lowerBound, int upperBound, int levels) {
    RGB_Data result = new RGB_Data(width, height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Pixel p = data[y][x];
        double[] rgb = p.getRgbArray();
        double[] quantizedRgb = getQuantizedRgb(rgb, lowerBound, upperBound, levels);
        result.setRGB(y, x, quantizedRgb);
      }
    }
    return result;
  }
  
  private double[] getQuantizedRgb(double[] rgb, int lowerBound, int upperBound, int Q_level) {
    int interval = (upperBound - lowerBound + 1) / Q_level;
    double halfInterval = interval / 2.0;
    double[] quantizedRgb = new double[rgb.length];
    for (int i = 0; i < rgb.length; i++) {
      if (rgb[i] <= lowerBound + halfInterval) {
        quantizedRgb[i] = lowerBound + halfInterval;
        continue;
      } else if (rgb[i] >= upperBound - halfInterval) {
        quantizedRgb[i] = upperBound - halfInterval;
        continue;
      }
      double roundedLevels = Math.round((rgb[i] - halfInterval) / interval);
      quantizedRgb[i] = roundedLevels * interval + halfInterval;
    }
    return quantizedRgb;
  }
  
  public BufferedImage toBufferedImage() {
    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = (int) data[y][x].r;
        int g = (int) data[y][x].g;
        int b = (int) data[y][x].b;
        
        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
        //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
        result.setRGB(x, y, pix);
      }
    }
    return result;
  }
}
