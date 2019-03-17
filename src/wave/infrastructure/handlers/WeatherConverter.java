package wave.infrastructure.handlers;

public class WeatherConverter
{	
	public static double convertRainToValue(int intPixelValue)
	{
		WeatherColorValues rain = WeatherColorValues.getRain(intPixelValue);
		return rain.getMM();
	}
}
