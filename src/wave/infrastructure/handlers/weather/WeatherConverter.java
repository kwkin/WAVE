package wave.infrastructure.handlers.weather;

public class WeatherConverter
{	
	public static double convertRainToValue(int intPixelValue)
	{
		RainColorValues rain = RainColorValues.getRain(intPixelValue);
		return rain.getMeasurement();
	}
	
	public static double convertTempToValue(int intPixelValue)
	{
		double temp = TemperatureColorValues.getColor(intPixelValue);
		return TemperatureColorValues.getMeasurement(temp);
	}
	
	public static double convertHumidityValue(int intPixelValue)
	{
		return HumidityColorValues.getColor(intPixelValue);
	}
	
	public static double convertLightningToValue(int intPixelValue)
	{
		LightningColorValues lightning = LightningColorValues.getLightning(intPixelValue);
		return lightning.getMeasurement();
	}
}
