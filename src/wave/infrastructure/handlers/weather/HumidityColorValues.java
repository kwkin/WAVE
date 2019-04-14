package wave.infrastructure.handlers.weather;

import java.nio.ByteBuffer;

import javafx.scene.paint.Color;
import wave.infrastructure.util.PointUtil;

public class HumidityColorValues
{
	public static double MIN_BRIGHTNESS = 0.36;
	public static double MAX_BRIGHTNESS = 1;
	public static double MIN_HUMIDITY = 0;
	public static double MAX_HUMIDITY = 100;

	public static double getColor(int intPixelColor)
	{
		byte[] colors = ByteBuffer.allocate(4).putInt(intPixelColor).array();
		double brightness = 0;
		if (colors.length == 4)
		{
			Color pixelColor = Color.rgb(colors[1] & 0xff, colors[2] & 0xff, colors[3] & 0xff);
			brightness = getColor(pixelColor);
		}
		else
		{
			return -1;
		}
		return brightness;
	}

	public static double getColor(Color color)
	{
		double brightness = -1;
		if (!color.equals(Color.TRANSPARENT))
		{
			brightness = color.getBrightness();
			if (brightness >= MIN_BRIGHTNESS && brightness <= MAX_BRIGHTNESS)
			{
				brightness = PointUtil.map(brightness, MAX_BRIGHTNESS, MIN_BRIGHTNESS, MIN_HUMIDITY, MAX_HUMIDITY);
			}
		}
		return brightness;
	}
}