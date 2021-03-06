package wave.infrastructure.layers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.StringTokenizer;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.markers.Marker;
import wave.infrastructure.models.WindMarker;
import wave.infrastructure.util.PointUtil;

public class WindLayer extends MarkerLayer
{
	private int numLatitude = 18;
	private int numLongitude = 36;
	
	private double maxLatitude = 0;
	private double minLatitude = Double.MAX_VALUE;
	private double maxLongitude = 0;
	private double minLongitude = Double.MAX_VALUE;
	
	private WindMarker[][] markers;
	private double latIndex;
	private double lonIndex;
	private double tlScale;
	private double trScale;
	private double blScale;
	private double brScale;
	
	public WindLayer(Path windFile) throws IOException
	{
		if (!Files.exists(windFile))
		{
			throw new IOException("Wind file does not exist.");
		}
		ArrayList<WindMarker> windMarkers = new ArrayList<WindMarker>();
		ArrayList<Marker> markerRender = new ArrayList<Marker>();
		BufferedReader reader = Files.newBufferedReader(windFile);
		String line = reader.readLine();
		this.markers = new WindMarker[this.numLatitude][this.numLongitude];
		while ((line = reader.readLine()) != null)
		{
			StringTokenizer tokenizer = new StringTokenizer(line, ",");
			if (tokenizer.countTokens() == 4)
			{
				int tokenIndex = 0;
				double latitude = 0;
				double longitude = 0;
				double direction = 0;
				double speed = 0;
				while (tokenizer.hasMoreTokens())
				{
					String token = tokenizer.nextToken();
					if (tokenIndex == 0)
					{
						latitude = Double.parseDouble(token);
					}
					else if (tokenIndex == 1)
					{
						longitude = Double.parseDouble(token);
					}
					else if (tokenIndex == 2)
					{
						speed = Double.parseDouble(token);
					}
					else if (tokenIndex == 3)
					{
						direction = Double.parseDouble(token);
					}
					tokenIndex++;
				}
				Position position = Position.fromDegrees(latitude, longitude, 0);
				WindMarker windMarker = new WindMarker(position, speed, direction);
				markerRender.add(windMarker.getMarker());
				windMarkers.add(windMarker);
				this.maxLatitude = Math.max(this.maxLatitude, latitude);
				this.minLatitude = Math.min(this.minLatitude, latitude);
				this.maxLongitude = Math.max(this.maxLongitude, longitude);
				this.minLongitude = Math.min(this.minLongitude, longitude);
			}
		}
		int positionIndex = 0;
		for (int latIndex = 0; latIndex < this.numLatitude - 1; ++latIndex)
		{
			for (int lonIndex = 0; lonIndex < this.numLongitude; ++lonIndex)
			{
				this.markers[latIndex][lonIndex] = windMarkers.get(positionIndex++);
			}
		}
		this.setOverrideMarkerElevation(true);
		this.setElevation(5000d);
		this.setPickEnabled(false);
		this.setMarkers(markerRender);
	}
	
	public void setNearestMarker(Position position)
	{
		this.latIndex = PointUtil.map(position.latitude.degrees, -90, 90, 0, this.numLatitude);
		this.lonIndex = PointUtil.map(position.longitude.degrees, -180, 180, 0, this.numLongitude);

		int latIntIndex = (int)this.latIndex;
		int lonIntIndex = (int)this.lonIndex;
		
		double td = this.latIndex - latIntIndex;
		double bd = Math.abs(1 - td);

		double rd = this.lonIndex - lonIntIndex;
		double ld = Math.abs(1 - rd);
		
		double tlDistance = Math.sqrt(bd * bd + rd * rd);
		this.tlScale = 0;
		if (tlDistance <= 1)
		{
			this.tlScale = Math.abs(1 - tlDistance);
		}
		
		double trDistance = Math.sqrt(bd * bd + ld * ld);
		this.trScale = 0;
		if (trDistance <= 1)
		{
			this.trScale = Math.abs(1 - trDistance);
		}
		
		double blDistance = Math.sqrt(td * td + rd * rd);
		this.blScale = 0;
		if (blDistance <= 1)
		{
			this.blScale = Math.abs(1 - blDistance);
		}
		
		double brDistance = Math.sqrt(td * td + ld * ld);
		this.brScale = 0;
		if (brDistance <= 1)
		{
			this.brScale = Math.abs(1 - brDistance);
		}
	}
	
	public double getDirection(Position position)
	{
		double direction = 0;
		int topIndex = (int)this.latIndex;
		int rightIndex = (int)this.lonIndex;
		int bottomIndex = topIndex - 1;
		int leftIndex = rightIndex - 1;
		if (topIndex == 0)
		{
			bottomIndex = this.numLatitude - 1;
		}
		if (rightIndex == 0)
		{
			leftIndex = this.numLongitude - 1;
		}
		
		
		// Top right
		direction += this.markers[topIndex][rightIndex].getDirection() * this.trScale;
		
		// Bottom right
		direction += this.markers[bottomIndex][rightIndex].getDirection() * this.brScale;

		// Top Left
		direction += this.markers[topIndex][leftIndex].getDirection() * this.tlScale;

		// Bottom Left
		direction += this.markers[bottomIndex][leftIndex].getDirection() * this.blScale;
		direction = Math.min(direction, 360);
		return direction;
	}
	
	public double getSpeed()
	{
		double speed = 0;
		int topIndex = (int)this.latIndex;
		int rightIndex = (int)this.lonIndex;
		int bottomIndex = topIndex - 1;
		int leftIndex = rightIndex - 1;
		if (topIndex == 0)
		{
			bottomIndex = this.numLatitude - 1;
		}
		if (rightIndex == 0)
		{
			leftIndex = this.numLongitude - 1;
		}
		
		// Top right
		speed += this.markers[topIndex][rightIndex].getSpeed() * this.trScale;
		
		// Bottom right
		speed += this.markers[bottomIndex][rightIndex].getSpeed() * this.brScale;

		// Top Left
		speed += this.markers[topIndex][leftIndex].getSpeed() * this.tlScale;

		// Bottom Left
		speed += this.markers[bottomIndex][leftIndex].getSpeed() * this.blScale;
		return speed;
	}
	
	public void setIsEnabled(boolean isEnabled)
	{
		this.setEnabled(isEnabled);
	}
}
