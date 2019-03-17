package wave.infrastructure.handlers;

import java.nio.ByteBuffer;

import javafx.scene.paint.Color;

public class WeatherConverter
{	
	public static double convertRainToValue(int intPixelValue)
	{
		WeatherColorValues rain = WeatherColorValues.getRain(intPixelValue);
		return rain.getMM();
	}
}
