package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.DefaultListModel;

import controller.CollagerController;
import interfaces.StatePrivacy;
import interfaces.ViewPrivacy;
import state.CollagerState;
import utils.Utils;
import view.CollagerGUIBuffer;
import view.TextView;

/**
 * Class representation of the main. This is what runs the collager
 * program.
 */
public class Main {
  /**
   * A method that is used to run the entirety of the program.
   * @param args represents arguments input by the user.
   * @throws IOException if there is nothing in args.
   */
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      System.out.println("Please put in Arguments.");
      System.out.println("Put user if you'd like to input commands manually.");
      System.out.println("Put commands if you'd like "
              + "the commands to be input automatically");
    } else if (args[0].equals("-commands") && args.length == 2) {
      StatePrivacy commandState = new CollagerState();
      ViewPrivacy commandView = new TextView();
      Scanner sc = new Scanner(new FileInputStream(args[1]));
      CollagerController collagerController = new CollagerController(commandState, commandView, sc);
      String response = "";
      do {
        response = sc.nextLine();
        collagerController.possibleOptions(response);
      }
      while (!response.equalsIgnoreCase("quit"));
    } else if (args[0].equals("-user")) {
      Scanner sc = new Scanner(System.in);
      if (args.length == 2) {
        if (args[1].equals("-GUI")) {
          StatePrivacy guiState = new CollagerState();
          DefaultListModel<String> outputList = new DefaultListModel<>();
          CollagerGUIBuffer guiBuffer = new CollagerGUIBuffer(outputList);
          ViewPrivacy guiTextView = new TextView(guiBuffer);
          CollagerController guiController = new CollagerController(guiState, guiTextView, sc);
          Utils guiUtils = new Utils(guiTextView);
          try {
            guiUtils.startGUI((CollagerState) guiState,
                    guiController, (TextView) guiTextView, outputList);
          }
          catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
          }
          return;
        }
      }
      if (args.length == 1) {
        StatePrivacy textState = new CollagerState();
        ViewPrivacy textView = new TextView();
        CollagerController textController = new CollagerController(textState, textView, sc);
        String response = "";
        do {
          if (textController.getCommandStopper()) {
            textController.changeCommandStopper();
          } else {
            textView.communicate("Enter Command \n");
          }
          response = sc.nextLine();
          textController.possibleOptions(response);
        }
        while (!response.equalsIgnoreCase("quit"));
      }
    } else {
      System.out.println("Please change Arguments.");
      System.out.println("Put user if you'd like to input commands manually.");
      System.out.println("Put commands followed by the file path if you'd like \" +\n"
              + "\"the commands to be input automatically");
    }
  }
}
