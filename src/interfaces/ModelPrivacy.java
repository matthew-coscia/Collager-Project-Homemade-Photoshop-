package interfaces;

/**
 * An interface that is used for the model class. It is used to 
 * keep the model private, as well as decoupling the controller.
 */
public interface ModelPrivacy {
  public void makeNewProject(String[] input);

  public void saveProject(String[] input);

  public void loadProject(String[] input);

  public void addLayer(String[] input);

  public void saveImage(String[] input);

  public void addImageToLayer(String[] splited);

  public void setFilter(String[] splited);

  public boolean getCommandStopper();

  public void changeCommandStopper();


}
