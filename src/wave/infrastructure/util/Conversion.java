package wave.infrastructure.util;

import java.nio.ByteBuffer;

public class Conversion
{
	public String colorDescription(int color)
	{
		byte[] bytes = ByteBuffer.allocate(4).putInt(color).array();
		StringBuilder message = new StringBuilder("A: ");
		message.append((byte) bytes[0] & 0xFF);
		message.append(" R: ");
		message.append((byte) bytes[1] & 0xFF);
		message.append(" G: ");
		message.append((byte) bytes[2] & 0xFF);
		message.append(" B: ");
		message.append((byte) bytes[3] & 0xFF);
		return message.toString();
	}
	
	public static double celsiusToFarenheit(double celsius)
	{
		double farenheit = (9/5) * celsius + 32;
		return farenheit;
	}
	
	public static double farenheitToCelcius(double fahrenheit)
	{
		double celsius = (5/9) * (fahrenheit - 32);
		return celsius;
	}
}
