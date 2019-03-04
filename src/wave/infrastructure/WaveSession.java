package wave.infrastructure;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwind.util.UnitsFormat;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import wave.infrastructure.handlers.GlobeSpinAnimation;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.KMLLayerLoader;
import wave.infrastructure.models.DraggableMarker;
import wave.views.WaveWindow;

// TODO add kml tree
public class WaveSession
{
	public final static String DEFAULT_LENGTH_UNIT = UnitsFormat.METRIC_SYSTEM;
	public final static String DEFAULT_ANGLE_UNIT = Angle.ANGLE_FORMAT_DMS;

	private WorldWindow worldWindow;
	private WaveWindow waveWindow;

	private MarkerLayer markerLayer;
	private DraggableMarker soundMarker;
	private List<KMLLayer> weatherOverlays;
	private boolean isTakingSurvey;

	private final StringProperty lengthUnitDisplayProperty;
	private final StringProperty angleUnitDisplayProperty;

	public WaveSession()
	{
		this.worldWindow = new WorldWindowGLJPanel();
		this.isTakingSurvey = false;
		this.weatherOverlays = new ArrayList<KMLLayer>();

		this.lengthUnitDisplayProperty = new SimpleStringProperty(DEFAULT_LENGTH_UNIT);
		this.angleUnitDisplayProperty = new SimpleStringProperty(DEFAULT_ANGLE_UNIT);

		Model model = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		this.worldWindow.setModel(model);

		this.initializeMarker();
		this.loadWeatherOverlays();
		this.intializeAnimator();
	}

	public DraggableMarker getSoundMarker()
	{
		return this.soundMarker;
	}

	public WorldWindow getWorldWindow()
	{
		return this.worldWindow;
	}

	public Model getModel()
	{
		return this.worldWindow.getModel();
	}

	public LayerList getLayers()
	{
		return this.worldWindow.getModel().getLayers();
	}

	public List<KMLLayer> getWeatherLayers()
	{
		return this.weatherOverlays;
	}

	/**
	 * Returns the current length format
	 * 
	 * @return The current length format
	 */
	public String getLengthUnitDisplay()
	{
		return this.lengthUnitDisplayProperty.getValue();
	}

	/**
	 * Sets the distance system
	 * 
	 * @param format The unit system specified using UnitsFormat
	 */
	public void setLengthUnitDisplay(String format)
	{
		switch (format)
		{
		case UnitsFormat.IMPERIAL_SYSTEM:
		case UnitsFormat.METRIC_SYSTEM:
			this.lengthUnitDisplayProperty.setValue(format);
			break;
		default:
			break;
		}
	}
	
	public StringProperty lengthUnitDisplayProperty()
	{
		return this.lengthUnitDisplayProperty;
	}

	/**
	 * Returns the current Angle format
	 * 
	 * @return The current Angle format
	 */
	public String getAngleUnitDisplay()
	{
		return this.angleUnitDisplayProperty.getValue();
	}

	/**
	 * Sets the distance system
	 * 
	 * @param format The unit system specified using UnitsFormat
	 */
	public void setAngleUnitDisplay(String format)
	{
		switch (format)
		{
		case Angle.ANGLE_FORMAT_DMS:
		case Angle.ANGLE_FORMAT_DD:
		case Angle.ANGLE_FORMAT_DM:
			this.angleUnitDisplayProperty.setValue(format);
			break;
		default:
			break;
		}
	}
	
	public StringProperty angleUnitDisplayProperty()
	{
		return this.angleUnitDisplayProperty;
	}

	public void shutdown()
	{
		this.worldWindow.shutdown();
		Platform.exit();
		System.exit(0);
	}

	public boolean isTakingSurvey()
	{
		return this.isTakingSurvey;
	}

	public void setIsTakingSurvey(boolean isTakingSurvey)
	{
		this.isTakingSurvey = isTakingSurvey;
		if (this.waveWindow != null)
		{
			this.waveWindow.setIsTakingSurvey(this.isTakingSurvey);
		}
	}

	public void addKMLLayer(KMLRoot kmlRoot)
	{
		// Create a KMLController to adapt the KMLRoot to the World Wind renderable
		// interface.
		KMLController kmlController = new KMLController(kmlRoot);
		KMLLayer layer = new KMLLayer();
		layer.setName((String) kmlRoot.getField(AVKey.DISPLAY_NAME));
		layer.addRenderable(kmlController);

		// TODO add configurable opacity
		layer.setOpacity(0.5);
		this.getModel().getLayers().add(layer);
		this.weatherOverlays.add(layer);
	}

	public WaveWindow getWaveWindow()
	{
		return this.waveWindow;
	}

	public void setWaveWindow(WaveWindow window)
	{
		this.waveWindow = window;
	}

	private void loadWeatherOverlays()
	{
		// TODO refactor this code
		Path precipitationLayer = Paths.get("data", "layer", "QPF24hr_Day1_latest.kml");
		if (Files.exists(precipitationLayer))
		{
			new KMLLayerLoader(precipitationLayer, this, true, "Rain Forcast March 1. (Jan 2019)");
		}
		Path humidityLayer = Paths.get("data", "layer", "MYDAL2_M_SKY_WV_2019-01-01_rgb_3600x1800.KMZ");
		if (Files.exists(humidityLayer))
		{
			new KMLLayerLoader(humidityLayer, this, true, "Avg. Humidity (Jan 2019)");
		}
		Path temperatureLayer = Paths.get("data", "layer", "MOD_LSTD_M_2019-01-01_rgb_3600x1800.KMZ");
		if (Files.exists(humidityLayer))
		{
			new KMLLayerLoader(temperatureLayer, this, true, "Avg. Land Temp. (Jan 2019)");
		}
		List<KMLLayer> weatherLayers = this.getWeatherLayers();
		for (KMLLayer layer : weatherLayers)
		{
			layer.setIsEnabled(false);
		}
		weatherLayers.get(0).setIsEnabled(true);
	}

	private void initializeMarker()
	{
		double initialLatitude;
		double initialLongitude;
		try
		{

			initialLatitude = Configuration.getDoubleValue("gov.nasa.worldwind.avkey.InitialLatitude");
			initialLongitude = Configuration.getDoubleValue("gov.nasa.worldwind.avkey.InitialLongitude");
		}
		catch (Exception e)
		{
			initialLatitude = 0;
			initialLongitude = 0;
		}
		BasicMarkerAttributes attributes = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.SPHERE, 1d, 10, 5);
		Position initialPosition = Position.fromDegrees(initialLatitude, initialLongitude);
		this.soundMarker = new DraggableMarker(initialPosition, attributes);
		List<Marker> markers = new ArrayList<Marker>();
		markers.add(soundMarker);
		this.markerLayer = new MarkerLayer();
		this.markerLayer.setMarkers(markers);
		this.worldWindow.addSelectListener(new BasicDragger(this.worldWindow));
		this.worldWindow.getModel().getLayers().add(this.markerLayer);
	}

	private void intializeAnimator()
	{
		// Rotate the globe until a mouse click is detected
		GlobeSpinAnimation animator = new GlobeSpinAnimation(this, -2);
		animator.setEventSource(this.worldWindow);
	}
}
