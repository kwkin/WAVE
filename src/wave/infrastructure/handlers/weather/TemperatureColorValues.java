package wave.infrastructure.handlers.weather;

import java.nio.ByteBuffer;

import javafx.scene.paint.Color;
import wave.infrastructure.core.MeasurementSystem;
import wave.infrastructure.preferences.PreferencesLoader;
import wave.infrastructure.util.Conversion;
import wave.infrastructure.util.PointUtil;

public class TemperatureColorValues
{
	public static double MIN_HUE = 175;
	public static double MAX_HUE = 60;
	public static double MIN_TEMP = -30;
	public static double MID_TEMP = 16;
	public static double MAX_TEMP = 55;

	public static double getColor(int intPixelColor)
	{
		byte[] colors = ByteBuffer.allocate(4).putInt(intPixelColor).array();
		double hue = 0;
		if (colors.length == 4)
		{
			Color pixelColor = Color.rgb(colors[1] & 0xff, colors[2] & 0xff, colors[3] & 0xff);
			hue = getColor(pixelColor);
		}
		else
		{
			return -1;
		}
		return hue;
	}

	public static double getColor(Color color)
	{
		double hue = -1;
		if (!color.equals(Color.BLACK))
		{
			hue = color.getHue();
			if (hue <= 360 && hue >= MIN_HUE)
			{
				hue = PointUtil.map(hue, 360, MIN_HUE, MID_TEMP, MIN_TEMP);
			}
			else
			{
				hue = PointUtil.map(hue, 0, MAX_HUE, MID_TEMP, MAX_TEMP);
			}
		}
		return hue;
	}

	public static double getMeasurement(double measurement)
	{
		// Convert celcius to farenheit
		if (PreferencesLoader.preferences().getLengthUnitDisplay() == MeasurementSystem.IMPERIAL)
		{
			measurement = Conversion.celsiusToFarenheit(measurement);
		}
		return measurement;
	}
}
