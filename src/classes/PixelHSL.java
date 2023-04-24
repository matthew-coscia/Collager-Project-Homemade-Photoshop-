package classes;

import model.CollagerModel;
import interfaces.PixelType;
import state.CollagerState;
import view.TextView;

/**
 * Class representation of an HSL Pixel, which consists of
 * a hue value, a saturation value, and a lightness value.
 */
public class PixelHSL implements PixelType {

  private double hue;
  private double saturation;
  private double lightness;
  private int alpha;
  private CollagerState state;
  private CollagerModel model;
  private TextView view;

  /**
   * The constructor for the PixelHSL class.
   * @param hue represents the tone of color (i.e. red,
   *            green, etc.)
   * @param saturation represents the intensity of a color
   *                   in a given pixel.
   * @param lightness represents how bright a pixel is.
   * @param alpha represents the transparency value of a pixel.
   * @param state represents the current state of the application.
   * @param model represents a place where all processes for the collager
   *              application go.
   * @param view represents how the user receives output for the program.
   */
  public PixelHSL(double hue, double saturation, double lightness, int alpha,
                  CollagerState state, CollagerModel model, TextView view) {
    this.hue = hue;
    this.saturation = saturation;
    this.lightness = lightness;
    this.alpha = alpha;
    this.state = state;
    this.model = model;
    this.view = view;

    if (hue > 360 || hue < 0) {
      throw new IllegalArgumentException("hue value is not within bounds.");
    }
    if (saturation > 1 || saturation < 0) {
      throw new IllegalArgumentException("saturation value is not within bounds.");
    }
    if (lightness > 1 || lightness < 0) {
      throw new IllegalArgumentException("lightness value is not within bounds");
    }
  }

  /**
   * Constructor for the PixelHSL class that converts an RGB Pixel.
   * @param pixelRGB the pixel in RGB format that will be converted.
   * @param state represents the current state of the collager.
   * @param model represents the controller class that run methods for the main.
   * @param view represents how the user receives output from the program.
   */
  public PixelHSL(PixelRGB pixelRGB, CollagerState state,
                  CollagerModel model, TextView view) {
    this.state = state;
    this.alpha = pixelRGB.getColorInt("Alpha");
    this.convertRGBtoHSL(pixelRGB);
    this.model = model;
    this.view = view;
    if (this.hue > 360) {
      this.hue = 360;
    }
    if (this.saturation > 1) {
      this.saturation = 1;
    }
    if (this.lightness > 1) {
      this.lightness = 1;
    }
    if (this.hue < 0) {
      this.hue = 0;
    }
    if (this.saturation < 0) {
      this.saturation = 0;
    }
    if (this.lightness < 0) {
      this.lightness = 0;
    }
  }

  /**
   * A method that gets the HSL color value based on a string.
   * @param color represents one of the HSL values.
   * @return a singular H, S, or L value.
   */
  public double getHSLColorDouble(String color) {
    boolean validColor = true;
    if (!color.equals("Hue") && !color.equals("Saturation") && !color.equals("Lightness")) {
      validColor = false;
    }
    double c = 0;
    if (color.equals("Hue")) {
      c = this.hue;
    }
    if (color.equals("Saturation")) {
      c = this.saturation;
    }
    if (color.equals("Lightness")) {
      c = this.lightness;
    }
    if (!validColor) {
      throw new IllegalStateException("Invalid color called for.");
    }
    return c;
  }

  /**
   * A method that is used to store the alpha value. This is
   * used for calling in other classes.
   * @return the alpha value of a Pixel.
   */
  public int getAlpha() {
    return this.alpha;
  }

  /**
   * A method which converts an RGB value into an HSL value.
   * @param initRGB represents a given RGB pixel value.
   */
  public void convertRGBtoHSL(PixelRGB initRGB) {
    double r = initRGB.getColorInt("Red") / 255.0;
    double g = initRGB.getColorInt("Green") / 255.0;
    double b = initRGB.getColorInt("Blue") / 255.0;
    double componentMax = Math.max(r, Math.max(g, b));
    double componentMin = Math.min(r, Math.min(g, b));
    double delta = componentMax - componentMin;

    double lightness = (componentMax + componentMin) / 2;
    double hue;
    double saturation;
    if (delta == 0) {
      hue = 0;
      saturation = 0;
    } else {
      saturation = delta / (1 - Math.abs(2 * lightness - 1));
      hue = 0;
      if (componentMax == r) {
        hue = (g - b) / delta;
        hue = hue % 6;
      } else if (componentMax == g) {
        hue = (b - r) / delta;
        hue += 2;
      } else if (componentMax == b) {
        hue = (r - g) / delta;
        hue += 4;
      }
      hue = hue * 60;
    }
    this.hue = hue;
    this.lightness = lightness;
    this.saturation = saturation;
  }

  /**
   * A method that darkens a given pixel by multiplying together our
   * darker colors.
   * @param layerPos represents the position of the layer we are currently working on.
   * @param row      represents the x position of a pixel.
   * @param col      represents the y position of a pixel.
   */
  public void blendPixelMultiply(int layerPos, int row, int col) {
    int firstLayerNotTransparent = -1;
    for (int i = 1; i < this.state.currentProject.getLayers().size() - layerPos; i++) {
      if (this.state.currentProject.getLayers().get(layerPos + i)
              .getPixels().get(row).get(col).getColorDouble("Alpha") > 0) {
        firstLayerNotTransparent = layerPos + i;
        break;
      }
    }
    if (firstLayerNotTransparent == -1) {
      return;
    }
    PixelHSL convertedPixel =
            new PixelHSL(this.state.currentProject.getLayers().
                    get(firstLayerNotTransparent).getPixels().get(row).get(col),
                    this.state, this.model, this.view);
    this.lightness = this.lightness * convertedPixel.lightness;
  }

  /**
   * A method that lightens a pixel based on the layers underneath
   * the desired filtering layer.
   * @param layerPos represents the layer to be lightened.
   * @param row      represents the x position of a pixel.
   * @param col      represents the y position of a pixel.
   */
  public void blendPixelScreen(int layerPos, int row, int col) {
    int firstLayerNotTransparent = -1;
    for (int i = 1; i < this.state.currentProject.getLayers().size() - layerPos; i++) {
      if (this.state.currentProject.getLayers().get(layerPos + i)
              .getPixels().get(row).get(col).getColorDouble("Alpha") > 0) {
        firstLayerNotTransparent = layerPos + i;
        break;
      }
    }
    if (firstLayerNotTransparent == -1) {
      return;
    }
    PixelHSL convertedPixel =
            new PixelHSL(this.state.currentProject.getLayers().
                    get(firstLayerNotTransparent).getPixels().get(row).get(col),
                    this.state, this.model, this.view);
    this.lightness = (1 - ((1 - this.lightness) * (1 - convertedPixel.lightness)));
  }
}
