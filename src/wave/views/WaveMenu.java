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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import wave.WaveApp;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.AngleFormat;
import wave.infrastructure.core.MeasurementSystem;
import wave.infrastructure.layers.GARSGraticule;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.KMLLayerLoader;
import wave.infrastructure.layers.LatLonGraticule;
import wave.infrastructure.preferences.PreferencesLoader;
import wave.views.windows.AboutWindow;
import wave.views.windows.preferences.PreferencesWindow;
import wave.views.windows.survey.SurveyWindow;

// TODO add setting to enable/disable network connection
// TODO add settings to enable/disable developer panels
public class WaveMenu extends MenuBar
{
	private FileChooser kmlChooser;
	private WaveSession session;

	public WaveMenu(WaveSession session)
	{
		this.session = session;

		// File menu
		Menu fileMenu = new Menu("File");
		this.getMenus().add(fileMenu);

		initializeKMLChooser();
		MenuItem loadKMLMenu = new MenuItem("Open KML...");
		loadKMLMenu.setOnAction(action ->
		{
			openKMLLayer();
		});
		fileMenu.getItems().add(loadKMLMenu);
		loadKMLMenu.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));
		fileMenu.getItems().add(new SeparatorMenuItem());


		MenuItem preferencesMenu = new MenuItem("Preferences...");
		preferencesMenu.setOnAction((event) ->
		{
			openPreferences();
		});
		fileMenu.getItems().add(preferencesMenu);
		
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
		exitMenu.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
		
		// View menu
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
		latLonMenu.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
		CheckMenuItem garsMenu = this.createLayerMenuItem(GARSGraticule.class);
		viewMenu.getItems().add(garsMenu);
		garsMenu.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));

		viewMenu.getItems().add(new SeparatorMenuItem());
		Menu unitsMenu = new Menu("Units");
		viewMenu.getItems().add(unitsMenu);
		ToggleGroup lengthGroup = new ToggleGroup();
		RadioMenuItem metricMenu = new RadioMenuItem("Metric");
		metricMenu.setToggleGroup(lengthGroup);
		unitsMenu.getItems().add(metricMenu);
		metricMenu.setOnAction(action ->
		{
			PreferencesLoader.preferences().setLengthUnitDisplay(MeasurementSystem.METRIC);
		});
		metricMenu.setSelected(PreferencesLoader.preferences().getLengthUnitDisplay() == MeasurementSystem.METRIC);
		metricMenu.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN));
		RadioMenuItem imperialMenu = new RadioMenuItem("Imperial");
		imperialMenu.setToggleGroup(lengthGroup);
		unitsMenu.getItems().add(imperialMenu);
		imperialMenu.setOnAction(action ->
		{
			PreferencesLoader.preferences().setLengthUnitDisplay(MeasurementSystem.IMPERIAL);
		});
		imperialMenu.setSelected(PreferencesLoader.preferences().getLengthUnitDisplay() == MeasurementSystem.IMPERIAL);
		imperialMenu.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
		unitsMenu.getItems().add(new SeparatorMenuItem());
		ToggleGroup angleGroup = new ToggleGroup();
		RadioMenuItem dmsMenu = new RadioMenuItem("DMS");
		dmsMenu.setToggleGroup(angleGroup);
		unitsMenu.getItems().add(dmsMenu);
		dmsMenu.setOnAction(action ->
		{
			PreferencesLoader.preferences().setAngleUnitDisplay(AngleFormat.DMS);
		});
		dmsMenu.setSelected(PreferencesLoader.preferences().getAngleUnitDisplay() == AngleFormat.DMS);
		RadioMenuItem ddMenu = new RadioMenuItem("DD");
		ddMenu.setToggleGroup(angleGroup);
		unitsMenu.getItems().add(ddMenu);
		ddMenu.setOnAction(action ->
		{
			PreferencesLoader.preferences().setAngleUnitDisplay(AngleFormat.DD);
		});
		ddMenu.setSelected(PreferencesLoader.preferences().getAngleUnitDisplay() == AngleFormat.DD);
		RadioMenuItem dmMenu = new RadioMenuItem("DM");
		dmMenu.setToggleGroup(angleGroup);
		unitsMenu.getItems().add(dmMenu);
		dmMenu.setOnAction(action ->
		{
			PreferencesLoader.preferences().setAngleUnitDisplay(AngleFormat.DM);
		});
		dmMenu.setSelected(PreferencesLoader.preferences().getAngleUnitDisplay() == AngleFormat.DM);
		viewMenu.getItems().add(new SeparatorMenuItem());
		CheckMenuItem fullscreenMenu = new CheckMenuItem("Toggle full screen");
		fullscreenMenu.setAccelerator(new KeyCodeCombination(KeyCode.F11));
		viewMenu.getItems().add(fullscreenMenu);
		fullscreenMenu.setOnAction((action) ->
		{
			WaveApp.getStage().setFullScreen(fullscreenMenu.isSelected());
		});
		WaveApp.getStage().fullScreenProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				fullscreenMenu.setSelected(newValue);
			}
		});

		// Layer menu
		Menu layerMenu = new Menu("Weather");
		this.getMenus().add(layerMenu);
		for (KMLLayer layer : session.getWeatherLayers())
		{
			CheckMenuItem layerMenuItem = this.createLayerMenuItem(layer.getName());
			layerMenu.getItems().add(layerMenuItem);
		}
		
		// Help menu
		Menu helpMenu = new Menu("Help");
		this.getMenus().add(helpMenu);
		MenuItem weatherHelp = new MenuItem("Weather Information...");
		helpMenu.getItems().add(weatherHelp);
		weatherHelp.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
		weatherHelp.setOnAction((value) ->
		{
			System.out.println("Opening weather information window...");
		});
		helpMenu.getItems().add(new SeparatorMenuItem());
		MenuItem aboutHelp = new MenuItem("About WAVE...");
		helpMenu.getItems().add(aboutHelp);
		helpMenu.setOnAction((value) ->
		{
			AboutWindow about = new AboutWindow();
			about.show();
		});
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

	protected void openPreferences()
	{
		PreferencesWindow window = new PreferencesWindow(PreferencesLoader.preferences());
		window.show();
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
