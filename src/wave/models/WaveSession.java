package wave.models;

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
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.BasicDragger;
import javafx.application.Platform;
import wave.layers.KMLLayer;
import wave.layers.KMLLayerLoader;

public class WaveSession
{
	private WorldWindow worldWindow;

	private final MarkerLayer markerLayer;
	private DraggableMarker soundMarker;
	private List<KMLLayer> weatherOverlays;

	public WaveSession()
	{
		this.worldWindow = new WorldWindowGLJPanel();
		Model model = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

		this.worldWindow.setModel(model);

		// Initialize draggable marker attributes
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

		// TODO add kml tree
		this.weatherOverlays = new ArrayList<KMLLayer>();
		this.loadWeatherOverlays();
		List<KMLLayer> weatherLayers = this.getWeatherLayers();
		for (KMLLayer layer : weatherLayers)
		{
			layer.setIsEnabled(false);
		}
		weatherLayers.get(0).setIsEnabled(true);
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

	public void shutdown()
	{
		this.worldWindow.shutdown();
		Platform.exit();
		System.exit(0);
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
	}
}
