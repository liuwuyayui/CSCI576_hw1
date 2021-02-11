public class YUV_Data {
  public static final double[][] TRANSFER_COEFFICIENTS = {
          {1.000, 0.956, 0.621},
          {1.000, -0.272, -0.647},
          {1.000, -1.106, 1.703}
  };
  
  static class Pixel {
    double Y;
    double U;
    double V;
    
    public Pixel() {
    }
    
    public Pixel(double[] yuv) {
      Y = yuv[0];
      U = yuv[1];
      V = yuv[2];
    }
    
    public double[] getYuvArray() {
      double[] result = new double[3];
      result[0] = Y;
      result[1] = U;
      result[2] = V;
      return result;
    }
  }
  
  private final Pixel[][] data;
  private final int width;
  private final int height;
  
  public YUV_Data(int width, int height) {
    this.width = width;
    this.height = height;
    data = new Pixel[height][width];
  }
  
  public void setYUV(int y, int x, double[] yuv) {
    data[y][x] = new Pixel(yuv);
  }
  
  public YUV_Data subSampleAndAdjust(int yRatio, int uRatio, int vRatio) {
    YUV_Data result = new YUV_Data(width, height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int yOffset = x % yRatio;
        int uOffset = x % uRatio;
        int vOffset = x % vRatio;
        result.data[y][x] = new Pixel();
        if (yOffset == 0) {
          result.data[y][x].Y = data[y][x].Y;
        } else {
          result.data[y][x].Y = data[y][x - yOffset].Y;
        }
        if (uOffset == 0) {
          result.data[y][x].U = data[y][x].U;
        } else {
          result.data[y][x].U = data[y][x - uOffset].U;
        }
        if (vOffset == 0) {
          result.data[y][x].V = data[y][x].V;
        } else {
          result.data[y][x].V = data[y][x - vOffset].V;
        }
      }
    }
    return result;
  }
  
  public RGB_Data toRgbData() {
    RGB_Data result = new RGB_Data(width, height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        result.setRGB(y, x, Utils.dotProduct(TRANSFER_COEFFICIENTS, data[y][x].getYuvArray()));
      }
    }
    return result;
  }
}
