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
import wave.WaveApp;
import wave.infrastructure.WaveSession;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.KMLLayerLoader;
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
// TODO fix stars layer
// TODO add shortcut keys to the file menu
public class WaveMenu extends MenuBar
{
	private FileChooser kmlChooser;
	private WaveSession session;

	public WaveMenu(WaveSession session)
	{
		this.session = session;

		// File tabs
		Menu menuFile = new Menu("File");
		this.getMenus().add(menuFile);

		initializeKMLChooser();
		MenuItem menuItemLoadKML = new MenuItem("Open KML...");
		menuItemLoadKML.setOnAction(action ->
		{
			openKMLLayer();
		});
		menuFile.getItems().add(menuItemLoadKML);
		menuFile.getItems().add(new SeparatorMenuItem());

		MenuItem surveyMenuItem = new MenuItem("Take Survey...");
		surveyMenuItem.setOnAction((event) ->
		{
			openSurvey();
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
}
