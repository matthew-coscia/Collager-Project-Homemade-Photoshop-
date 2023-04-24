package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import classes.Layer;
import classes.PixelRGB;
import controller.CollagerController;
import state.CollagerState;
import utils.Utils;
import view.TextView;

/**
 * A class representation of the Java Swing frame. This class is used to
 * design the Collager GUI.
 */
public class CollagerFrame extends JFrame implements ActionListener {

  private PreviewPanel picturePanel;
  private JButton newProjectButton;
  private JButton loadProjectButton;
  private JButton saveProjectButton;
  private JButton addLayerButton;
  private JButton addImageToLayerButton;
  private JButton setFilterButton;
  private JButton saveImageButton;
  private JButton quitButton;
  private JButton layerButton;
  private JPanel commandPanel;
  private TextView view;
  private Utils utils;
  private CollagerState state;
  private CollagerController controller;

  /**
   * Constructor for the class CollagerFrame. Allows for initialization
   * of significant parameters for starting a GUI.
   *
   * @param state      represents the current state of the collager.
   * @param controller represents the controller class that run methods for the main.
   * @param utils      represents the utility class.
   * @param view       represents the current view of the collager.
   * @param outputList represents where the view sends its response for the GUI.
   */
  public CollagerFrame(CollagerState state, CollagerController controller,
                       Utils utils, TextView view, DefaultListModel<String> outputList) {
    super();
    // Starter setup
    this.state = state;
    this.controller = controller;
    this.utils = utils;
    this.view = view;
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(2, 2));

    // frame settings
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(1200, 950);
    this.setTitle("Collager");
    this.setVisible(true);
    this.setLayout(new BorderLayout(0, 0));

    // picture panel
    JPanel holderPanel = new JPanel();
    holderPanel.setPreferredSize(new Dimension(800, 800));
    holderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    holderPanel.setVisible(true);
    holderPanel.setBackground(Color.WHITE);

    // scroll box
    this.picturePanel = new PreviewPanel(this.state);
    mainPanel.add(this.picturePanel, BorderLayout.WEST);

    // commandPanel setup
    JPanel rightSidePanel = new JPanel();
    rightSidePanel.setLayout(new BorderLayout());
    this.commandPanel = new JPanel();
    this.commandPanel.setLayout(null);
    this.commandPanel.setSize(280, 280);
    this.commandPanel.setVisible(true);
    this.addCommandButtons();
    rightSidePanel.add(this.commandPanel);
    rightSidePanel.setVisible(true);
    rightSidePanel.setPreferredSize(new Dimension(280, 800));
    mainPanel.add(rightSidePanel, BorderLayout.EAST);

    // output setup
    DefaultListModel<String> outputListDefault = outputList;
    outputListDefault.addElement("---Collager Responses---");
    JPanel dialogBox = new JPanel();
    dialogBox = new JPanel();
    JList<String> listOfResponses = new JList<>(outputListDefault);
    dialogBox.add(listOfResponses);
    dialogBox.setPreferredSize(new Dimension(800, 100));
    dialogBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mainPanel.add(dialogBox, BorderLayout.SOUTH);
    this.add(mainPanel);
    this.getContentPane();
    this.pack();
  }

  /**
   * A method that is used to assign a task to each button, which allows
   * for proper use of the buttons displayed in the GUI.
   */
  public void addCommandButtons() {
    // newProject button
    this.newProjectButton = new JButton();
    this.newProjectButton.setBounds(0, 0, 140, 70);
    this.newProjectButton.setText("new-project");
    this.newProjectButton.addActionListener(this);
    this.commandPanel.add(newProjectButton);

    // loadProject button
    this.loadProjectButton = new JButton();
    this.loadProjectButton.setBounds(140, 0, 140, 70);
    this.loadProjectButton.setText("load-project");
    this.loadProjectButton.addActionListener(this);
    this.commandPanel.add(loadProjectButton);

    // saveProject button
    this.saveProjectButton = new JButton();
    this.saveProjectButton.setBounds(0, 70, 140, 70);
    this.saveProjectButton.setText("save-project");
    this.saveProjectButton.addActionListener(this);
    this.commandPanel.add(saveProjectButton);

    // addLayer button
    this.addLayerButton = new JButton();
    this.addLayerButton.setBounds(140, 70, 140, 70);
    this.addLayerButton.setText("add-layer");
    this.addLayerButton.addActionListener(this);
    this.commandPanel.add(addLayerButton);

    // addImageToLayer button
    this.addImageToLayerButton = new JButton();
    this.addImageToLayerButton.setBounds(0, 140, 140, 70);
    this.addImageToLayerButton.setText("add-image-to-layer");
    this.addImageToLayerButton.addActionListener(this);
    this.commandPanel.add(addImageToLayerButton);

    // setFilter button
    this.setFilterButton = new JButton();
    this.setFilterButton.setBounds(140, 140, 140, 70);
    this.setFilterButton.setText("set-filter");
    this.setFilterButton.addActionListener(this);
    this.commandPanel.add(setFilterButton);

    // saveImage button
    this.saveImageButton = new JButton();
    this.saveImageButton.setBounds(0, 210, 140, 70);
    this.saveImageButton.setText("save-image");
    this.saveImageButton.addActionListener(this);
    this.commandPanel.add(saveImageButton);

    // quit button
    this.quitButton = new JButton();
    this.quitButton.setBounds(140, 210, 140, 70);
    this.quitButton.setText("quit");
    this.quitButton.addActionListener(this);
    this.commandPanel.add(quitButton);

    // choose filter button
    this.layerButton = new JButton();
    this.layerButton.setBounds(0, 280, 280, 70);
    this.layerButton.setText("layers");
    this.layerButton.addActionListener(this);
    this.commandPanel.add(this.layerButton);
  }

  /**
   * Class that is used for creating the drop down menu that allows the user
   * to change the selected layer.
   */
  class ListSelectionHandler implements ListSelectionListener {

    private int selectedLayer;
    private CollagerState state;
    private CollagerController controller;
    private PreviewPanel picturePanel;
    private Utils utils;
    private JFrame aFrame;
    private CollagerFrame mainFrame;

    /**
     * Constructor for the ListSelectionHandler class in order to be called.
     *
     * @param state        represents the current state of the collager.
     * @param controller   represents the user interaction with the program.
     * @param picturePanel represents the panel that holds the preview image.
     * @param utils        represents the utilities class.
     * @param aFrame       represents the frame that the preview picture is placed in.
     */
    public ListSelectionHandler(CollagerState state, CollagerController controller,
                                PreviewPanel picturePanel,
                                Utils utils, JFrame aFrame, CollagerFrame mainFrame) {
      this.controller = controller;
      this.state = state;
      this.picturePanel = picturePanel;
      this.utils = utils;
      this.aFrame = aFrame;
      this.mainFrame = mainFrame;
    }

    /**
     * A method which is called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
      if (e.getSource() == this.mainFrame.getButton("LayerButton")) {
        if (!e.getValueIsAdjusting()) {
          this.selectedLayer = e.getFirstIndex();
          Layer tempLayer = this.state.currentProject.getLayers().get(this.selectedLayer);
          this.state.currentProject.getLayers().remove(this.selectedLayer);
          this.state.currentProject.getLayers().add(0, tempLayer);
          this.state.currentProject.forPreview = true;
          this.controller.possibleOptions("save-image preview");
          this.picturePanel.paint();
          this.state.currentProject.forPreview = false;
          aFrame.dispose();
        }
      }
    }
  }

  /**
   * A method that listens for events, and allows the program
   * to perform specific tasks based on these events.
   *
   * @param e the event to be processed
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.newProjectButton) {
      String heightWidth = JOptionPane.showInputDialog(
              "Enter the Width and Height of the new project" + "\n"
                      + "Format: 1000 1000");
      this.controller.possibleOptions("new-project " + heightWidth);
    }
    if (e.getSource() == this.loadProjectButton) {
      JFileChooser loadProjectFile = new JFileChooser();
      loadProjectFile.setCurrentDirectory(new File("."));
      if (loadProjectFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        String projectPath = new String(loadProjectFile.getSelectedFile().getAbsolutePath());
        this.controller.possibleOptions("load-project " + projectPath);
      }
    }
    if (e.getSource() == this.saveProjectButton) {
      String saveProjectPath = JOptionPane.showInputDialog(
              "Enter the path you want to save the file");
      this.controller.possibleOptions("save-project " + saveProjectPath);
    }
    if (e.getSource() == this.addLayerButton) {
      String layerName = JOptionPane.showInputDialog("Enter the name of the new layer");
      this.controller.possibleOptions("add-layer " + layerName);
    }
    if (e.getSource() == this.addImageToLayerButton) {
      String commandBulk = JOptionPane.showInputDialog(
              "Enter the image directory, the layer, and the x/y position"
                      + "\n" + "Format: initial-layer tako.ppm 0 0");
      this.controller.possibleOptions("add-image-to-layer " + commandBulk);
    }
    if (e.getSource() == this.setFilterButton) {
      String commandBulk = JOptionPane.showInputDialog("Enter the layer name and the filter name"
              + "\n" + "Format: initial-layer red-component");
      this.controller.possibleOptions("set-filter " + commandBulk);
    }
    if (e.getSource() == this.saveImageButton) {
      String imageName = JOptionPane.showInputDialog("Enter the directory to save the image");
      this.controller.possibleOptions("save-image " + imageName);
    }
    if (e.getSource() == this.quitButton) {
      this.dispose();
    }
    if (e.getSource() == this.layerButton) {
      if (this.state.active) {
        JFrame x = new JFrame();
        JPanel y = new JPanel();
        x.setVisible(true);
        x.setPreferredSize(new Dimension(300, 300));
        DefaultListModel<String> layerNames = new DefaultListModel<>();
        for (int i = 0; i < this.state.currentProject.getLayers().size(); i++) {
          layerNames.add(i, this.state.currentProject.getLayers().get(i).toString());
        }
        JList<String> jList = new JList<>(layerNames);
        jList.setVisible(true);
        ListSelectionHandler handler = new ListSelectionHandler(this.state, this.controller,
                this.picturePanel, this.utils, x, this);
        jList.addListSelectionListener(handler);
        y.setPreferredSize(new Dimension(300, 300));
        y.setVisible(true);
        y.add(jList);
        x.add(y);
        x.getContentPane();
        x.pack();
      } else {
        this.view.communicate("Must create project before editing layers.");
      }
    }
    if (this.state.active) {
      this.state.previewPixels = new ArrayList<ArrayList<PixelRGB>>();
      this.state.currentProject.forPreview = true;
      this.controller.possibleOptions("save-image preview");
      this.state.currentProject.forPreview = false;
      this.revalidate();
      this.picturePanel.paint();
      this.picturePanel.revalidate();
    }
  }

  public JButton getButton(String input) {
    if (input.equals("LayerButton")) {
      return this.layerButton;
    }
    else if (input.equals("addImageButton")) {
      return this.addImageToLayerButton;
    }
    else {
      return null;
    }
  }
}
