package wave.infrastructure;

import java.io.IOException;
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
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.BasicDragger;
import javafx.application.Platform;
import wave.infrastructure.handlers.GlobeSpinAnimation;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.KMLLayerLoader;
import wave.infrastructure.layers.WindLayer;
import wave.infrastructure.models.DraggableMarker;
import wave.views.WaveWindow;

// TODO fix wind speed not calculating
// TODO add audio listener class that is called when the marker position updates
public class WaveSession
{
	private WorldWindow worldWindow;
	private WaveWindow waveWindow;

	private MarkerLayer markerLayer;
	private DraggableMarker soundMarker;
	private KMLLayer rainLayer;
	private WindLayer windLayer;
	private KMLLayer temperatureLayer;
	private KMLLayer humidityLayer;
	private KMLLayer lightningLayer;
	private boolean isTakingSurvey;

	public WaveSession()
	{
		this.worldWindow = new WorldWindowGLJPanel();
		this.isTakingSurvey = false;

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

	public void shutdown()
	{
		this.worldWindow.shutdown();
		Platform.exit();
		System.exit(0);
	}

	public void setSoundMarkerVisibility(boolean isVisible)
	{
		this.markerLayer.setEnabled(isVisible);
		this.worldWindow.redraw();
	}

	public boolean getSoundMarkerVisibility()
	{
		return this.markerLayer.isEnabled();
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

	public void addKMLLayer(KMLLayer layer)
	{
		// Create a KMLController to adapt the KMLRoot to the World Wind renderable
		// interface.
		this.getModel().getLayers().add(layer);
	}

	public WaveWindow getWaveWindow()
	{
		return this.waveWindow;
	}

	public void setWaveWindow(WaveWindow window)
	{
		this.waveWindow = window;
	}

	public KMLLayer getRainLayer()
	{
		return this.rainLayer;
	}

	public KMLLayer getTemperatureLayer()
	{
		return this.temperatureLayer;
	}

	public KMLLayer getHumidityLayer()
	{
		return this.humidityLayer;
	}

	public WindLayer getWindLayer()
	{
		return this.windLayer;
	}
	
	public KMLLayer getLightningLayer()
	{
		return this.lightningLayer;
	}

	private void loadWeatherOverlays()
	{
		Path lightningLayer = Paths.get("data", "layer", "lightning_98_05.kmz");
		if (Files.exists(lightningLayer))
		{
			KMLLayerLoader loader = new KMLLayerLoader(lightningLayer, this, true, "Lightning Rate (98 - 05");
			KMLLayer layer = loader.loadKML();
			layer.setOpacity(0.5);
			layer.setIsEnabled(true);
			this.lightningLayer = layer;

		}
		Path humidityLayer = Paths.get("data", "layer", "humidity_2019_01_01.kmz");
		if (Files.exists(humidityLayer))
		{
			KMLLayerLoader loader = new KMLLayerLoader(humidityLayer, this, true, "Avg. Humidity (Jan 2019)");
			KMLLayer layer = loader.loadKML();
			layer.setOpacity(0.5);
			layer.setIsEnabled(false);
			this.humidityLayer = layer;
		}
		Path temperatureLayer = Paths.get("data", "layer", "temperature_2019_01_01.kmz");
		if (Files.exists(humidityLayer))
		{
			KMLLayerLoader loader = new KMLLayerLoader(temperatureLayer, this, true, "Avg. Land Temp. (Jan 2019)");
			KMLLayer layer = loader.loadKML();
			layer.setOpacity(0.5);
			layer.setIsEnabled(false);
			this.temperatureLayer = layer;
		}
		Path precipitationLayer = Paths.get("data", "layer", "rain_2019_01_01.kmz");
		if (Files.exists(precipitationLayer))
		{
			KMLLayerLoader loader = new KMLLayerLoader(precipitationLayer, this, true, "Rain Forcast (Jan 1 2019)");
			KMLLayer layer = loader.loadKML();
			layer.setOpacity(0.5);
			layer.setIsEnabled(true);
			this.rainLayer = layer;

		}

		Path windFile = Paths.get("data", "layer", "winds_2019_03_01.csv");
		if (Files.exists(windFile))
		{
			try
			{
				this.windLayer = new WindLayer(windFile);
				this.windLayer.setName("Wind Direction and Speed (Mar 2019)");
				this.worldWindow.getModel().getLayers().add(windLayer);
				this.windLayer.setEnabled(true);
			}
			catch (IOException e)
			{
			}
		}
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
