package wave.views;

import java.io.File;
import java.nio.file.Paths;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import wave.Wave;
import wave.layers.KMLLayer;
import wave.layers.KMLLayerLoader;
import wave.models.WaveSession;

public class WaveMenu extends MenuBar
{
	public WaveMenu(WaveSession session)
	{
		// File tabs
		Menu menuFile = new Menu("File");
		this.getMenus().add(menuFile);
		MenuItem menuItemLoadKML = new MenuItem("Open KML...");
		FileChooser kmlChooser = new FileChooser();
		ExtensionFilter kmlFilter = new ExtensionFilter("kml files (*.kml)", "*.kml", "*.KML");
		ExtensionFilter kmzFilter = new ExtensionFilter("kmz files (*.kmz)", "*.kmz", "*.KMZ");
		kmlChooser.getExtensionFilters().add(kmlFilter);
		kmlChooser.getExtensionFilters().add(kmzFilter);
		
		String executionDirectory = System.getProperty("user.dir");
		kmlChooser.setInitialDirectory(Paths.get(executionDirectory).toFile());
		menuItemLoadKML.setOnAction(action ->
		{
			try
			{
				File kmlFile = kmlChooser.showOpenDialog(Wave.getStage());
				if (kmlFile == null)
				{
					return;
				}
				else if (kmlFile.exists())
				{
					new KMLLayerLoader(kmlFile, session).start();
				}
				else
				{
					Alert errorDialog = new Alert(AlertType.ERROR);
					StringBuilder errorMessage = new StringBuilder("Error: File ");
					errorMessage.append(kmlFile.toString());
					errorMessage.append(" was not found.");
					errorDialog.setContentText(errorMessage.toString());
					errorDialog.show();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		menuFile.getItems().add(menuItemLoadKML);
		menuFile.getItems().add(new SeparatorMenuItem());

		MenuItem surveyMenuItem = new MenuItem("Take Survey...");
		surveyMenuItem.setOnAction((event) ->
		{
			System.out.println("Open survey...");
		});
		menuFile.getItems().add(surveyMenuItem);
		menuFile.getItems().add(new SeparatorMenuItem());

		MenuItem menuItemExit = new MenuItem("Exit");
		menuItemExit.setOnAction(action ->
		{
			session.shutdown();
		});
		menuFile.getItems().add(menuItemExit);

		
		// Layer Tabs
		Menu menuLayer = new Menu("Layer");
		this.getMenus().add(menuLayer);
		for (KMLLayer layer : session.getWeatherLayers())
		{
			String layerName = layer.getName();
			CheckMenuItem layerItem = new CheckMenuItem(layerName);
			layerItem.selectedProperty().bindBidirectional(layer.isEnabledProperty());
			layerItem.setOnAction((action) ->
			{
				layer.setEnabled(layerItem.isSelected());
			});
			menuLayer.getItems().add(layerItem);
		}
	}
}
