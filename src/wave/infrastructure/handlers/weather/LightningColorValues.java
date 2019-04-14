package wave.infrastructure.handlers.weather;

import java.nio.ByteBuffer;

import javafx.scene.paint.Color;
import wave.infrastructure.core.MeasurementSystem;
import wave.infrastructure.preferences.PreferencesLoader;

public enum LightningColorValues
{
	// @formatter:off
	LIGHTNING_NONE(Color.TRANSPARENT, 0), 
	LIGHTNING_02(Color.rgb(69, 0, 72), 0.2), 
	LIGHTNING_04(Color.rgb(89, 0, 149), 0.4),
	LIGHTNING_06(Color.rgb(198, 1, 202), 0.6), 
	LIGHTNING_08(Color.rgb(56, 0, 223), 0.8),
	LIGHTNING_1(Color.rgb(0, 128, 255), 1), 
	LIGHTNING_2(Color.rgb(1, 219, 254), 2), 
	LIGHTNING_4(Color.rgb(0, 141, 0), 4),
	LIGHTNING_6(Color.rgb(149, 200, 0), 6),
	LIGHTNING_8(Color.rgb(199, 255, 120), 8), 
	LIGHTNING_10(Color.rgb(254, 255, 1), 10), 
	LIGHTNING_15(Color.rgb(254, 200, 3), 15),
	LIGHTNING_20(Color.rgb(254, 160, 0), 20),
	LIGHTNING_30(Color.rgb(255, 126, 0), 30),
	LIGHTNING_40(Color.rgb(255, 34, 0), 40),
	LIGHTNING_50(Color.rgb(176, 50, 72), 50),
	LIGHTNING_70(Color.rgb(0, 0, 0), 70);
	// @formatter:on

	private Color color;
	private double rate;

	LightningColorValues(Color color, double rate)
	{
		this.color = color;
		this.rate = rate;
	}

	public static LightningColorValues getLightning(int intPixelColor)
	{
		byte[] colors = ByteBuffer.allocate(4).putInt(intPixelColor).array();

		LightningColorValues rate = LightningColorValues.LIGHTNING_NONE;
		if (colors[1] == -1 && colors[2] == -1 && colors[3] == -1)
		{
			rate = LightningColorValues.LIGHTNING_NONE;
		}
		else
		{
			Color pixelColor = Color.rgb(colors[1] & 0xff, colors[2] & 0xff, colors[3] & 0xff);
			rate = getLightning(pixelColor);
		}
		return rate;

	}

	public static LightningColorValues getLightning(Color color)
	{
		LightningColorValues rain = LightningColorValues.LIGHTNING_NONE;
		for (LightningColorValues value : LightningColorValues.values())
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
		double measurement = rate;
		if (PreferencesLoader.preferences().getLengthUnitDisplay() == MeasurementSystem.IMPERIAL)
		{
			// Get miles squared
			measurement *= 2.59;
		}
		return measurement;
	}
}
