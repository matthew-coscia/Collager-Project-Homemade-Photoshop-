package classes;

import java.util.ArrayList;

import controller.CollagerController;
import model.CollagerModel;
import state.CollagerState;
import utils.ImageUtil;
import utils.Utils;
import view.TextView;

/**
 * A class representation of a Project. A project is what a user can open
 * or close, and can contain multiple layers.
 */
public class Project {

  private String name;
  private int height;
  private int width;
  private int maxValue;
  private ArrayList<Layer> layers;
  private CollagerState state;
  private Utils utils;
  private CollagerModel model;
  private CollagerController controller;
  private ArrayList<ArrayList<ArrayList<PixelRGB>>> layeredPixels;
  private TextView view;
  public boolean forPreview;

  /**
   * First constructor for the Project class. This is used for creating a
   * new project.
   * @param name represents the title given to a project.
   * @param height represents the height of a frame for the project.
   * @param width represents the width of a frame for the project.
   * @param state represents the current state of the game.
   * @param model represents the controller class that run methods for the main.
   * @param controller represents the user interaction with the program.
   * @param view represents how the user receives output from the program.
   */
  public Project(String name, int height, int width, CollagerState state,
                 CollagerModel model, CollagerController controller, TextView view) {
    this.state = state;
    this.model = model;
    this.controller = controller;
    this.view = view;
    this.utils = new Utils(this.view);
    this.name = name;
    this.height = height;
    this.width = width;
    this.maxValue = utils.maxValue;
    this.layers = new ArrayList<Layer>();
    this.layeredPixels = new ArrayList<ArrayList<ArrayList<PixelRGB>>>();
    this.forPreview = false;
  }

  /**
   * Second constructor for the Project class. This constructor is used for
   * loading an existing project in.
   * @param name represents the title given to a project.
   * @param height represents the height of a frame for the project.
   * @param width represents the width of a frame for the project.
   * @param maxValue represents the max value of a PixelRGB.
   * @param layers represents the layer(s) in a project.
   * @param state represents the current state of the game.
   * @param model represents the controller class that run methods for the main.
   * @param controller represents the user interaction with the program.
   * @param view represents how the user receives output from the program.
   */
  public Project(String name, int height, int width, int maxValue,
                 ArrayList<Layer> layers, CollagerState state,
                 CollagerModel model, CollagerController controller, TextView view) {
    this.state = state;
    this.model = model;
    this.controller = controller;
    this.utils = new Utils(this.view);
    this.name = name;
    this.height = height;
    this.width = width;
    this.maxValue = maxValue;
    this.view = view;
    this.layers = layers;
    this.layeredPixels = new ArrayList<ArrayList<ArrayList<PixelRGB>>>();
    this.forPreview = false;
  }

  /**
   * A method that converts the project into a string by returning
   * the project name.
   * @return the name of the project.
   */
  public String toString() {
    return this.name;
  }

  /**
   * A method that creates the first layer in a project. This consists of
   * a white background with a unique height and width.
   */
  public void addInitialLayer() {
    ArrayList<ArrayList<PixelRGB>> pixels = new ArrayList<ArrayList<PixelRGB>>();
    for (int i = 0; i < this.height; i++) {
      pixels.add(new ArrayList<PixelRGB>());
      for (int k = 0; k < this.width; k++) {
        pixels.get(i).add(new PixelRGB(
                255, 255, 255, 255,
                this.state, this.model, this.controller, this.view));
      }
    }
    this.layers.add(new Layer(
            pixels, "initial-layer", this.state, this.controller, this.model, this.view));
  }

  /**
   * A method that creates a new layer to an existing project.
   * @param name represents the title of the new layer.
   */
  public void addLayer(String name) {
    ArrayList<ArrayList<PixelRGB>> pixels = new ArrayList<ArrayList<PixelRGB>>();
    for (int i = 0; i < this.height; i++) {
      pixels.add(new ArrayList<PixelRGB>());
      for (int k = 0; k < this.width; k++) {
        pixels.get(i).add(new PixelRGB(
                255, 255, 255, 0,
                this.state, this.model, this.controller, this.view));
      }
    }
    this.layers.add(0, new Layer(pixels, name, this.state, this.controller, this.model, this.view));
  }

  /**
   * A method that saves the pixels to an ArrayList with the purpose of
   * saving to a different list. This creates a backup for the pixels.
   */
  public void populateSavePixels() {
    for (int i = 0; i < this.layers.size(); i++) {
      this.layers.get(i).resetSaveImagePixels();
      for (int k = 0; k < this.layers.get(i).getPixels().size(); k++) {
        this.layers.get(i).getSaveImagePixels().add(new ArrayList<PixelRGB>());
        for (int j = 0; j < this.layers.get(i).getPixels().get(k).size(); j++) {
          PixelRGB newPixel = new PixelRGB(this.layers.get(i).getPixels()
                  .get(k).get(j).getColorInt("Red"),
                  this.layers.get(i).getPixels().get(k).get(j).getColorInt("Green"),
                  this.layers.get(i).getPixels().get(k).get(j).getColorInt("Blue"),
                  this.layers.get(i).getPixels().get(k).get(j).getColorInt("Alpha"),
                  this.state, this.model, this.controller, this.view);
          this.layers.get(i).getSaveImagePixels().get(k).add(newPixel);
        }
      }
    }
  }

  /**
   * A method that saves an image as a PPM.
   * @param input represents the name of the
   *              file given by the user.
   */
  public void saveImage(String[] input) {
    this.populateSavePixels();
    for (int d = 0; d < this.layers.size(); d++) {
      this.setFilterOnSave(this.layers.get(d).toString(), this.layers.get(d).getCurrentFilter());
    }
    String name = input[1];
    if (this.layers.size() == 1 && !this.forPreview) {
      this.utils.saveImageToFile(this.height, this.width, this.maxValue,
              this.layers.get(0).getSaveImagePixels(), name);
      return;
    }
    for (int i = 0; i < this.layers.get(0).getSaveImagePixels().size(); i++) {
      this.layeredPixels.add(new ArrayList<ArrayList<PixelRGB>>());
      for (int k = 0; k < this.layers.get(0).getSaveImagePixels().get(i).size(); k++) {
        this.layeredPixels.get(i).add(new ArrayList<PixelRGB>());
        for (int j = 0; j < this.layers.size(); j++) {
          this.layeredPixels.get(i).get(k).add(this.layers
                  .get(j).getSaveImagePixels().get(i).get(k));
        }
      }
    }
    ArrayList<PixelRGB> fillRow = new ArrayList<PixelRGB>();
    ArrayList<ArrayList<PixelRGB>> finalArray = new ArrayList<ArrayList<PixelRGB>>();
    PixelRGB pixPrime = new PixelRGB(this.state, this.controller, this.model);
    for (int i = 0; i < this.layeredPixels.size(); i++) {
      for (int k = 0; k < this.layeredPixels.get(i).size(); k++) {
        for (int j = 0; j < this.layeredPixels.get(i).get(k).size() - 1; j++) {
          if (j == 0) {
            pixPrime = this.formula(this.layeredPixels.get(i).get(k).get(j),
                    this.layeredPixels.get(i).get(k).get(j + 1));
          }
          if (j > 0) {
            pixPrime = this.formula(pixPrime, this.layeredPixels.get(i).get(k).get(j + 1));
          }
        }
        fillRow.add(pixPrime);
      }
      finalArray.add(fillRow);
      fillRow = new ArrayList<PixelRGB>();
    }
    this.layeredPixels = new ArrayList<ArrayList<ArrayList<PixelRGB>>>();
    if (!this.forPreview) {
      this.utils.saveImageToFile(this.height, this.width, this.maxValue, finalArray, name);
    }
    if (this.forPreview) {
      if (this.layers.size() == 1) {
        this.state.previewPixels = this.layers.get(0).getSaveImagePixels();
      }
      else {
        this.state.previewPixels = finalArray;
      }
    }
  }

  /**
   * A method that computes prime values from RGBA values.
   * This is done with a formula provided.
   * @param top    represents the layer above the bottom layer.
   * @param bottom represents the layer below the top layer.
   * @return a new PixelRGB that represents the RGBA value with the alpha
   *         formula applied.
   */
  public PixelRGB formula(PixelRGB top, PixelRGB bottom) {
    if (top.getColorDouble("Alpha") == 0 && bottom.getColorDouble("Alpha") > 0) {
      return bottom;
    }
    if (bottom.getColorDouble("Alpha") == 0 && top.getColorDouble("Alpha") > 0) {
      return top;
    }
    if (top.getColorDouble("Alpha") == 0 && bottom.getColorDouble("Alpha") == 0) {
      return top;
    }
    if (top.getColorDouble("Alpha") == 255 && bottom.getColorDouble("Alpha") == 255) {
      return top;
    }
    double topAlpha = top.getColorDouble("Alpha");
    double bottomAlpha = bottom.getColorDouble("Alpha");
    double aDoublePrime = (topAlpha / 255 + bottomAlpha / 255 * (1 - (topAlpha / 255)));
    double aPrime = aDoublePrime * 255;
    double topRed = top.getColorDouble("Red");
    double bottomRed = bottom.getColorDouble("Red");
    double topGreen = top.getColorDouble("Green");
    double bottomGreen = bottom.getColorDouble("Green");
    double topBlue = top.getColorDouble("Blue");
    double bottomBlue = bottom.getColorDouble("Blue");
    double rPrime = (topAlpha / 255 * topRed
            + bottomRed * (bottomAlpha / 255)
            * (1 - topAlpha / 255)) * (1 / aDoublePrime);
    double gPrime = (topAlpha / 255 * topGreen
            + bottomGreen * (bottomAlpha / 255)
            * (1 - topAlpha / 255)) * (1 / aDoublePrime);
    double bPrime = (topAlpha / 255 * topBlue
            + bottomBlue * (bottomAlpha / 255)
            * (1 - topAlpha / 255)) * (1 / aDoublePrime);
    int rPrimeInt = (int) rPrime;
    int gPrimeInt = (int) gPrime;
    int bPrimeInt = (int) bPrime;
    int aPrimeInt = (int) aPrime;

    return new PixelRGB(rPrimeInt, gPrimeInt, bPrimeInt, aPrimeInt,
            this.state, this.model, this.controller, this.view);
  }

  /**
   * A method that allows a user to add a PPM image to a given layer.
   * @param layerName represents the layer that the user wants to add
   *                  and image to.
   * @param imageName represents the file path to the image.
   * @param xPosition represents the desired x-position of the image.
   *                  This begins in the top left corner.
   * @param yPosition represents the desired y-position of the image.
   *                  This begins in the top left corner, and then moves
   *                  down.
   */
  public void addImageToLayer(String layerName, String imageName, int xPosition, int yPosition) {
    ImageUtil imageUtil = new ImageUtil(this.state, this.model, this.controller, this.view);
    if (xPosition < 0 || yPosition < 0 || xPosition > this.width || yPosition > this.height) {
      this.view.communicate("X/Y Values out of bounds." + "\n");
      return;
    }
    int layerPos = -1;
    for (int i = 0; i < this.layers.size(); i++) {
      if (layerName.equals(this.layers.get(i).toString())) {
        layerPos = i;
      }
    }
    if (layerPos == -1) {
      this.view.communicate("Given Layer not found. Re-Enter command." + "\n");
      return;
    }
    if (imageName.split("\\.")[1].equals("ppm")) {
      imageUtil.readPPM(imageName);
    }
    else {
      imageUtil.readImage(imageName);
    }
    if (this.state.imageToBeAdded.equals(new ArrayList<ArrayList<PixelRGB>>())) {
      this.view.communicate("Image can not be found. Re-Enter command." + "\n");
      return;
    }
    ArrayList<ArrayList<PixelRGB>> newPixels = this.layers.get(layerPos).getPixels();
    ArrayList<ArrayList<PixelRGB>> newLayer = new ArrayList<ArrayList<PixelRGB>>();
    int placeCounterA = 0;
    int placeCounterB = 0;
    for (int a = 0; a < newPixels.size(); a++) {
      placeCounterB = 0;
      newLayer.add(new ArrayList<PixelRGB>());
      for (int b = 0; b < newPixels.get(a).size(); b++) {
        if (a >= yPosition && b >= xPosition
                && this.state.imageToBeAdded.size() > placeCounterA
                && this.state.imageToBeAdded.get(placeCounterA).size() > placeCounterB) {
          newLayer.get(a).add(this.state.imageToBeAdded.get(placeCounterA).get(placeCounterB));
          placeCounterB = placeCounterB + 1;
        } else {
          newLayer.get(a).add(newPixels.get(a).get(b));
        }
      }
      if (a > yPosition) {
        placeCounterA = placeCounterA + 1;
      }
    }
    this.layers.get(layerPos).assignPixels(newLayer);
    this.view.communicate("Image added to layer!" + "\n");
  }

  /**
   * A method that gets the layers in a project.
   * @return an ArrayList of layers.
   */
  public ArrayList<Layer> getLayers() {
    return this.layers;
  }

  /**
   * A method that retrieves the height of the project.
   * @return the height of the initial layer.
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * A method retrieves the width of the project.
   * @return the width of the intial layer.
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * A method that retrieves the max value of the RGB values.
   * @return the max value of the RGB values.
   */
  public int getMaxValue() {
    return this.maxValue;
  }

  /**
   * A method that changes the name of a filter, so that when
   * it is applied, the filter can be saved on the layer.
   * @param layerName    represents the name of the layer.
   * @param filterOption represents which filter is being choosen.
   */
  public void markFilter(String layerName, String filterOption) {
    int layerPos = -1;
    for (int i = 0; i < this.layers.size(); i++) {
      if (layerName.equals(this.layers.get(i).toString())) {
        layerPos = i;
      }
    }
    if (layerPos == -1) {
      this.view.communicate("Given Layer not found. Re-Enter command." + "\n");
      return;
    }
    this.layers.get(layerPos).markFilter(filterOption);
  }

  /**
   * A method that applies a unique filter to a given layer. This is used for saveImage
   * @param layerName    represents the name of the layer.
   * @param filterOption represents which filter is being chosen to be applied.
   */
  public void setFilterOnSave(String layerName, String filterOption) {
    int layerPos = -1;
    for (int i = 0; i < this.layers.size(); i++) {
      if (layerName.equals(this.layers.get(i).toString())) {
        layerPos = i;
      }
    }
    if (layerPos == -1) {
      this.view.communicate("Given Layer not found. Re-Enter command." + "\n");
      return;
    }
    if (filterOption.equals("normal")) {
      this.layers.get(layerPos).assignCurrentFilterString("normal");
    } else if (filterOption.equals("red-component")) {
      this.layers.get(layerPos).changeComponent("Red");
    } else if (filterOption.equals("green-component")) {
      this.layers.get(layerPos).changeComponent("Green");
    } else if (filterOption.equals("blue-component")) {
      this.layers.get(layerPos).changeComponent("Blue");
    } else if (filterOption.equals("brighten-value")) {
      this.layers.get(layerPos).brightenValue();
    } else if (filterOption.equals("brighten-intensity")) {
      this.layers.get(layerPos).brightenIntensity();
    } else if (filterOption.equals("brighten-luma")) {
      this.layers.get(layerPos).brightenLuma();
    } else if (filterOption.equals("darken-value")) {
      this.layers.get(layerPos).darkenValue();
    } else if (filterOption.equals("darken-intensity")) {
      this.layers.get(layerPos).darkenIntensity();
    } else if (filterOption.equals("darken-luma")) {
      this.layers.get(layerPos).darkenLuma();
    } else if (filterOption.equalsIgnoreCase("blend-difference")) {
      this.layers.get(layerPos).blendDifference(layerPos);
    } else if (filterOption.equalsIgnoreCase("blend-multiply")) {
      this.layers.get(layerPos).blendMultiply(layerPos);
    } else if (filterOption.equalsIgnoreCase("blend-screen")) {
      this.layers.get(layerPos).blendScreen(layerPos);
    }
    else {
      this.view.communicate("Invalid Filter Option. Reverted layer to normal.");
      this.setFilterOnSave(layerName, this.layers.get(layerPos).getCurrentFilterString());
      this.layers.get(layerPos).assignCurrentFilterString("normal");
    }
  }
}
