package wave.infrastructure.handlers;

import java.nio.ByteBuffer;

import javafx.scene.paint.Color;

public enum WeatherColorValues
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

	WeatherColorValues(Color color, double mm)
	{
		this.color = color;
		this.mm = mm;
	}

	public static WeatherColorValues getRain(int intPixelColor)
	{
		byte[] colors = ByteBuffer.allocate(4).putInt(intPixelColor).array();
		WeatherColorValues rain = WeatherColorValues.RAIN_NONE;
		if (colors[1] == -1 || colors[2] == -1 || colors[3] == -1)
		{
			rain = WeatherColorValues.RAIN_NONE;
		}
		else
		{
			Color pixelColor = Color.rgb(colors[1] & 0xff, colors[2] & 0xff, colors[3] & 0xff);
			for (byte color : colors)
			{
				System.out.print(" " + (color & 0xff));
			}
			System.out.println();
			rain = getRain(pixelColor);
		}
		return rain;

	}

	public static WeatherColorValues getRain(Color color)
	{
		WeatherColorValues rain = WeatherColorValues.RAIN_NONE;
		for (WeatherColorValues value : WeatherColorValues.values())
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

	public double getMM()
	{
		return this.mm;
	}
}
