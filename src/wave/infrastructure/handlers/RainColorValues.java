package wave.infrastructure.handlers;

import java.nio.ByteBuffer;

import gov.nasa.worldwind.util.WWMath;
import javafx.scene.paint.Color;
import wave.infrastructure.core.MeasurementSystem;
import wave.infrastructure.preferences.PreferencesLoader;

public enum RainColorValues
{
	// @formatter:off
	RAIN_NONE(Color.TRANSPARENT, 0), 
	RAIN_5(Color.rgb(30, 0, 130), 5), 
	RAIN_10(Color.rgb(0, 80, 170), 10),
	RAIN_15(Color.rgb(0, 141, 200), 15), 
	RAIN_20(Color.rgb(0, 150, 17), 20), 
	RAIN_25(Color.rgb(28, 240, 0), 25),
	RAIN_32(Color.rgb(143, 255, 45), 32), 
	RAIN_38(Color.rgb(255, 252, 1), 38), 
	RAIN_64(Color.rgb(255, 197, 21), 64),
	RAIN_76(Color.rgb(224, 110, 0), 76),
	RAIN_102(Color.rgb(164, 38, 0), 102), 
	RAIN_127(Color.rgb(200, 0, 23), 127), 
	RAIN_152(Color.rgb(255, 45, 143), 152);
	// @formatter:on

	private Color color;
	private double mm;

	RainColorValues(Color color, double mm)
	{
		this.color = color;
		this.mm = mm;
	}

	public static RainColorValues getRain(int intPixelColor)
	{
		byte[] colors = ByteBuffer.allocate(4).putInt(intPixelColor).array();

		RainColorValues rain = RainColorValues.RAIN_NONE;
		if (colors[1] == -1 && colors[2] == -1 && colors[3] == -1)
		{
			rain = RainColorValues.RAIN_NONE;
		}
		else
		{
			Color pixelColor = Color.rgb(colors[1] & 0xff, colors[2] & 0xff, colors[3] & 0xff);
			rain = getRain(pixelColor);
		}
		return rain;

	}

	public static RainColorValues getRain(Color color)
	{
		RainColorValues rain = RainColorValues.RAIN_NONE;
		for (RainColorValues value : RainColorValues.values())
		{
			if (value.getColor().equals(color))
			{
				rain = value;
				break;
			}
		}
		return rain;
	}

	public Color getColor()
	{
		return this.color;
	}

	public double getMeasurement()
	{
		double measurement = mm;
		if (PreferencesLoader.preferences().getLengthUnitDisplay() == MeasurementSystem.IMPERIAL)
		{
			// Get inches
			measurement = WWMath.convertMetersToFeet(measurement / 1000) * 12;
		}
		return measurement;
	}
}
