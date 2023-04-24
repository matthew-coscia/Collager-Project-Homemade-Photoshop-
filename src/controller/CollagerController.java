package controller;

import java.util.Scanner;

import interfaces.ModelPrivacy;
import interfaces.StatePrivacy;
import interfaces.ViewPrivacy;
import model.CollagerModel;

/**
 * A class representation of the controller for the Collager. This is used
 * to allow the user to communicate with the program.
 */
public class CollagerController {

  private ModelPrivacy model;
  private StatePrivacy state;
  private ViewPrivacy view;

  /**
   * First constructor for the CollagerController class. Mainly used for making a
   * controller in the Main class.
   * @param state represents the current state of the game.
   * @param view represents how the user receives output in the program.
   */
  public CollagerController(StatePrivacy state, ViewPrivacy view, Scanner sc) {
    this.state = state;
    this.view = view;
    this.model = new CollagerModel(this.state, this, this.view, sc);
  }

  /**
   * A method that takes in the user response and redirects
   * them to the correct method in the controller.
   * @param response represents the user input as a string.
   */
  public void possibleOptions(String response) {
    boolean throwMessage = true;
    String[] splited = response.split(" ");
    if (splited[0].equals("new-project")) {
      this.model.makeNewProject(splited);
      throwMessage = false;
    }
    if (splited[0].equals("load-project")) {
      this.model.loadProject(splited);
      throwMessage = false;
    }
    if (splited[0].equals("quit")) {
      throwMessage = false;
    }
    if (this.state.getActiveState()) {
      if (splited[0].equals("save-project")) {
        this.model.saveProject(splited);
      }
      if (splited[0].equals("add-layer")) {
        this.model.addLayer(splited);
      }
      if (splited[0].equals("save-image")) {
        this.model.saveImage(splited);
      }
      if (splited[0].equals("add-image-to-layer")) {
        this.model.addImageToLayer(splited);
      }
      if (splited[0].equals("set-filter")) {
        this.model.setFilter(splited);
      }
    }
    if (!this.state.getActiveState() && throwMessage) {
      if (splited[0].equals("save-project") || splited[0].equals("add-layer")
              || splited[0].equals("save-image") || splited[0].equals("add-image-to-layer")) {
        this.view.communicate("Cannot do command without "
                + "importing or making a project." + "\n");
      } else {
        this.view.communicate("Unknown Command. Re-Type" + "\n");
      }
    }
  }

  /**
   * A method that is used for the command line view. It checks
   * if there has been an error within the program that would cause
   * the 'input command' to output twice.
   * @return the value that stops messages from sending twice.
   */
  public boolean getCommandStopper() {
    return this.model.getCommandStopper();
  }

  /**
   * A method that is called only if the command stopper is true, and
   * returns the command stopper to false.
   */
  public void changeCommandStopper() {
    this.model.changeCommandStopper();
  }
}
