package wave.infrastructure.layers;

import java.awt.Color;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import wave.infrastructure.util.PointUtil;

public class WindMarker
{
	public static final int MAX_WIND = 50;
	
	private Marker marker;
	private double speed;
	private double direction;

	public WindMarker(Position position, double speed, double direction)
	{
		this.speed = speed;
		this.direction = direction;
		
		int colorIndex1 = (int) PointUtil.map(speed, 0, MAX_WIND, 0, 255);
		colorIndex1 = Math.min(colorIndex1, 255);
		colorIndex1 = Math.max(colorIndex1, 0);
		int colorIndex2 = (int) PointUtil.map(speed, 0, MAX_WIND, 255, 0);
		colorIndex2 = Math.min(colorIndex2, 255);
		colorIndex2 = Math.max(colorIndex2, 0);
		
		Color color = new Color(colorIndex1, colorIndex2, 0);
		Material material = new Material(color, 255);
		BasicMarkerAttributes wind = new BasicMarkerAttributes(material, BasicMarkerShape.CONE, 1);
		wind.setMarkerPixels(15);
		this.marker = new BasicMarker(position, wind);
		this.marker.setPitch(Angle.fromDegrees(90));
		this.marker.setHeading(Angle.fromDegrees(direction));
	}

	public Marker getMarker()
	{
		return this.marker;
	}

	public double getSpeed()
	{
		return this.speed;
	}

	public double getDirection()
	{
		return this.direction;
	}
}