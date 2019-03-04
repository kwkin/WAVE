package wave.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.Earth.NASAWFSPlaceNameLayer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import wave.WaveApp;
import wave.infrastructure.WaveSession;
import wave.infrastructure.layers.GARSGraticule;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.KMLLayerLoader;
import wave.infrastructure.layers.LatLonGraticule;
import wave.views.survey.SurveyWindow;

// TODO add a settings/preferences window.
// TODO add settings for the audio settings
// TODO add settings to change lat/lon display formats
// TODO add setting to enable/disable network connection
// TODO add settings to enable/disable developer panels
// TODO add an about dialog.
// TODO add a help dialog for information about the weather overlays
// TODO add a view tab to toggle compass, placenames, scalebar, graticules, crosshair
// TODO add a view tab -> units to change units and display formats
// TODO add a toggle full screen
// TODO add shortcut keys to the file menu
public class WaveMenu extends MenuBar
{
	private FileChooser kmlChooser;
	private WaveSession session;

	public WaveMenu(WaveSession session)
	{
		this.session = session;

		// File tabs
		Menu fileMenu = new Menu("File");
		this.getMenus().add(fileMenu);

		initializeKMLChooser();
		MenuItem loadKMLMenu = new MenuItem("Open KML...");
		loadKMLMenu.setOnAction(action ->
		{
			openKMLLayer();
		});
		fileMenu.getItems().add(loadKMLMenu);
		fileMenu.getItems().add(new SeparatorMenuItem());

		MenuItem surveyMenu = new MenuItem("Take Survey...");
		surveyMenu.setOnAction((event) ->
		{
			openSurvey();
		});
		fileMenu.getItems().add(surveyMenu);
		fileMenu.getItems().add(new SeparatorMenuItem());

		MenuItem exitMenu = new MenuItem("Exit");
		exitMenu.setOnAction(action ->
		{
			session.shutdown();
		});
		fileMenu.getItems().add(exitMenu);

		Menu viewMenu = new Menu("View");
		this.getMenus().add(viewMenu);
		CheckMenuItem compassMenu = this.createLayerMenuItem(CompassLayer.class);
		viewMenu.getItems().add(compassMenu);
		CheckMenuItem scaleBarMenu = this.createLayerMenuItem(ScalebarLayer.class);
		viewMenu.getItems().add(scaleBarMenu);
		CheckMenuItem worldMapMenu = this.createLayerMenuItem(WorldMapLayer.class);
		viewMenu.getItems().add(worldMapMenu);
		CheckMenuItem placeNamesMenu = this.createLayerMenuItem(NASAWFSPlaceNameLayer.class);
		viewMenu.getItems().add(placeNamesMenu);
		CheckMenuItem latLonMenu = this.createLayerMenuItem(LatLonGraticule.class);
		viewMenu.getItems().add(latLonMenu);
		CheckMenuItem garsMenu = this.createLayerMenuItem(GARSGraticule.class);
		viewMenu.getItems().add(garsMenu);

		// Layer Tabs
		Menu layerMenu = new Menu("Weather");
		this.getMenus().add(layerMenu);
		for (KMLLayer layer : session.getWeatherLayers())
		{
			CheckMenuItem layerMenuItem = this.createLayerMenuItem(layer.getName());
			layerMenu.getItems().add(layerMenuItem);
		}
	}

	protected void openKMLLayer()
	{
		try
		{
			File kmlFile = kmlChooser.showOpenDialog(WaveApp.getStage());
			if (kmlFile == null)
			{
				return;
			}
			else if (kmlFile.exists())
			{
				new KMLLayerLoader(kmlFile, this.session).start();
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
	}

	protected void openSurvey()
	{
		SurveyWindow window = new SurveyWindow(this.session);
		this.session.setIsTakingSurvey(true);
		window.show();
	}

	private void initializeKMLChooser()
	{
		this.kmlChooser = new FileChooser();
		ExtensionFilter kmlFilter = new ExtensionFilter("kml files (*.kml)", "*.kml", "*.KML");
		ExtensionFilter kmzFilter = new ExtensionFilter("kmz files (*.kmz)", "*.kmz", "*.KMZ");
		this.kmlChooser.getExtensionFilters().add(kmlFilter);
		this.kmlChooser.getExtensionFilters().add(kmzFilter);

		String executionDirectory = System.getProperty("user.dir");
		this.kmlChooser.setInitialDirectory(Paths.get(executionDirectory).toFile());
	}

	private CheckMenuItem createLayerMenuItem(Class<?> layerClass)
	{
		CheckMenuItem menuItem = null;
		List<Layer> matchingLayers = this.session.getLayers().getLayersByClass(layerClass);
		if (matchingLayers.size() > 0)
		{
			Layer layer = matchingLayers.get(0);
			CheckMenuItem layerMenuItem = new CheckMenuItem(layer.getName());
			layerMenuItem.setOnAction((action) ->
			{
				layer.setEnabled(layerMenuItem.isSelected());
			});
			layer.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent event)
				{
					if (event.getPropertyName() == "Enabled")
					{
						layerMenuItem.setSelected((boolean) event.getNewValue());
					}
				}
			});
			layerMenuItem.setSelected(layer.isEnabled());
			menuItem = layerMenuItem;
		}
		return menuItem;
	}

	private CheckMenuItem createLayerMenuItem(String layerName)
	{
		CheckMenuItem menuItem = null;
		Layer layer = this.session.getLayers().getLayerByName(layerName);
		if (layer != null)
		{
			CheckMenuItem layerMenuItem = new CheckMenuItem(layer.getName());
			layerMenuItem.setOnAction((action) ->
			{
				layer.setEnabled(layerMenuItem.isSelected());
			});
			layer.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent event)
				{
					if (event.getPropertyName() == "Enabled")
					{
						layerMenuItem.setSelected((boolean) event.getNewValue());
					}
				}
			});
			layerMenuItem.setSelected(layer.isEnabled());
			menuItem = layerMenuItem;
		}
		return menuItem;
	}
}
