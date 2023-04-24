package view;

import interfaces.ViewPrivacy;

/**
 * This is a class that represents the TextView, which allows the user to
 * read output.
 */
public class TextView implements ViewPrivacy {

  private Appendable destination;

  /**
   * This is the constructor for the TextView class, which allows
   * a user to see output and allows for input.
   */
  public TextView() {
    this.destination = System.out;
  }

  /**
   * This is the second constructor for the TextView class which is used for
   * the GUI.
   * @param collagerGUIBuffer represents the buffer that takes the program
   *                          output and sends it to the GUI text.
   */
  public TextView(CollagerGUIBuffer collagerGUIBuffer) {
    this.destination = collagerGUIBuffer;
  }

  /**
   * A method that sends a message to the user through the view.
   * @param msg represents the desired message to be sent to the user.
   */
  public void communicate(String msg) {
    try {
      this.destination.append(msg);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
