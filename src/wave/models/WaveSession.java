package wave.models;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import javafx.application.Platform;

public class WaveSession
{
	private WorldWindow worldWindow;
	
	public WaveSession()
	{
		this.worldWindow = new WorldWindowGLJPanel();
		Model model = (Model)WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		
		this.worldWindow.setModel(model);
	}
	
	public WorldWindow getWorldWindow()
	{
		return this.worldWindow;
	}
	
	public Model getModel()
	{
		return this.worldWindow.getModel();
	}
	
	public void shutdown()
	{
		this.worldWindow.shutdown();
		Platform.exit();
		System.exit(0);
	}
	
}
