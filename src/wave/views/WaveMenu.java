package wave.views;

import gov.nasa.worldwind.layers.Layer;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import wave.models.WaveSession;

public class WaveMenu extends MenuBar
{
	public WaveMenu(WaveSession session)
	{
		Menu menuFile = new Menu("File");
		MenuItem menuItemExit = new MenuItem("Exit");
		menuItemExit.setOnAction(action ->
		{
			session.shutdown();
		});
		menuFile.getItems().add(menuItemExit);
		
		Menu menuLayer = new Menu("Layer");
		for (Layer layer : session.getWorldWindow().getModel().getLayers())
		{
			CheckMenuItem layerItem = new CheckMenuItem(layer.getName());
			layerItem.setSelected(layer.isEnabled());
			layerItem.setOnAction((action) ->
			{
				layer.setEnabled(layerItem.isSelected());
			});
			menuLayer.getItems().add(layerItem);
		}
		this.getMenus().add(menuFile);
		this.getMenus().add(menuLayer);
	}
}
