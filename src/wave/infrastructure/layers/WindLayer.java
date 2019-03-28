package wave.infrastructure.layers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.StringTokenizer;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import wave.infrastructure.util.PointUtil;

public class WindLayer extends MarkerLayer
{
	private static final double MIN_OPACITY = 0.4;
	private static final double MAX_OPACITY = 0.8;
	private static final int MAX_WIND = 60;
	
	
	public WindLayer(Path windFile) throws IOException
	{
		if (!Files.exists(windFile))
		{
			throw new IOException("Wind file does not exist.");
		}

		ArrayList<Marker> markers = new ArrayList<Marker>();
		BufferedReader reader = Files.newBufferedReader(windFile);
		String line = reader.readLine();
		while ((line = reader.readLine()) != null)
		{
			
			StringTokenizer tokenizer = new StringTokenizer(line, ",");
			if (tokenizer.countTokens() == 4)
			{
				int tokenIndex = 0;
				double latitude = 0;
				double longitude = 0;
				double direction = 0;
				double opacity = 0;
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
						// TODO change color based upon wind speed
						double speed = Double.parseDouble(token);
						opacity = PointUtil.map(speed, 0, MAX_WIND, MIN_OPACITY, MAX_OPACITY);
					}
					else if (tokenIndex == 3)
					{
						direction = Double.parseDouble(token);
					}
					tokenIndex++;
				}
				BasicMarkerAttributes wind = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.HEADING_ARROW, opacity);
				Marker marker = new BasicMarker(Position.fromDegrees(latitude, longitude, 0), wind);
				marker.setHeading(Angle.fromDegrees(direction));
				markers.add(marker);
			}
		}
		this.setOverrideMarkerElevation(true);
		this.setElevation(1000d);
		this.setPickEnabled(false);
		this.setMarkers(markers);
	}
}
