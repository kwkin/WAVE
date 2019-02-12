package wave.models;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.BasicDragger;
import javafx.application.Platform;

public class WaveSession
{
	private WorldWindow worldWindow;
	
	private final MarkerLayer markerLayer;
	private DraggableMarker soundMarker;
	
	public WaveSession()
	{
		this.worldWindow = new WorldWindowGLJPanel();
		Model model = (Model)WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		
		this.worldWindow.setModel(model);
		
		// Initialize draggable marker attributes
		BasicMarkerAttributes attributes = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.SPHERE, 1d, 10, 5);
		Position initialPosition = Position.fromDegrees(29.6516, -82.3248);
		this.soundMarker = new DraggableMarker(initialPosition, attributes);
		List<Marker> markers = new ArrayList<Marker>();
		markers.add(soundMarker);
		this.markerLayer = new MarkerLayer();
		this.markerLayer.setMarkers(markers);
        this.worldWindow.addSelectListener(new BasicDragger(this.worldWindow));
		this.worldWindow.getModel().getLayers().add(this.markerLayer);
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
		
	public void shutdown()
	{
		this.worldWindow.shutdown();
		Platform.exit();
		System.exit(0);
	}
}
