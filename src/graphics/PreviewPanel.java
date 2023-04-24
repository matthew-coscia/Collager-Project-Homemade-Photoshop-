package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import javax.swing.*;

import state.CollagerState;

/**
 * A class representation of a panel in the collager GUI.
 */
public class PreviewPanel extends Panel {
  boolean previewReady;
  private CollagerState state;
  private JScrollPane jp;
  private JLabel image;

  /**
   * Constructor for the PreviewPanel class. This is used to
   * intialize the current state of the collager.
   * @param state represents the current state of the collager.
   */
  public PreviewPanel(CollagerState state) {
    super();
    this.previewReady = false;
    this.state = state;
    // make picture preview panel.
    this.setVisible(true);
    this.setPreferredSize(new Dimension(750, 750));
    BufferedImage initialImage = new BufferedImage(750, 750, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < 750; i++) {
      for (int k = 0; k < 750; k++) {
        initialImage.setRGB(k, i, new Color(255, 255, 255).getRGB());
      }
    }
    this.image = new JLabel(new ImageIcon(initialImage));
    this.jp = new JScrollPane();
    jp.setViewportView(this.image);
    this.add(jp);
    this.jp.setVisible(true);
    this.image.setVisible(true);
    this.setVisible(true);
    this.revalidate();

  }

  /**
   * A method that is used to load a given PPM into the GUI.
   */
  public void paint() {
    this.remove(this.jp);
    if (this.state.previewPixels.size() > 0) {
      BufferedImage img = new BufferedImage(this.state.previewPixels
              .get(0).size(), this.state.previewPixels.size(), BufferedImage.TYPE_INT_RGB);
      for (int i = 0; i < this.state.previewPixels.size(); i++) {
        for (int k = 0; k < this.state.previewPixels.get(i).size(); k++) {
          Color tempColor = new Color(
                  this.state.previewPixels.get(i).get(k).getColorInt("Red"),
                  this.state.previewPixels.get(i).get(k).getColorInt("Green"),
                  this.state.previewPixels.get(i).get(k).getColorInt("Blue"));
          img.setRGB(k, i, tempColor.getRGB());
        }
      }
      this.image = new JLabel(new ImageIcon(img));
      this.jp = new JScrollPane();
      this.jp.setPreferredSize(new Dimension(750, 750));
      jp.setViewportView(this.image);
      this.add(jp);
      this.jp.setVisible(true);
      this.image.setVisible(true);
      this.setVisible(true);
      this.revalidate();


    }
  }
}
