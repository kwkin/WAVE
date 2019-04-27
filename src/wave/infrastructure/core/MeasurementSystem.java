package wave.infrastructure.core;

import gov.nasa.worldwind.util.UnitsFormat;
import gov.nasa.worldwind.util.WWMath;

public enum MeasurementSystem
{
	// @formatter:off
	METRIC(UnitsFormat.METRIC_SYSTEM), 
	IMPERIAL(UnitsFormat.IMPERIAL_SYSTEM);
	// @formatter:on

	private String wwName;

	MeasurementSystem(String wwName)
	{
		this.wwName = wwName;
	}

	public String getWWName()
	{
		return this.wwName;
	}

	/**
	 * Converts meters to a unit description in the specified unit system
	 * 
	 * @param meters The unit to convert
	 * @param unitSystem The output system of units to specified using UnitsFormat
	 * @return
	 */
	public String lengthDescription(double meters)
	{
		String string;
		switch (this)
		{
		case IMPERIAL:
			int miles = (int) Math.round(WWMath.convertMetersToMiles(meters));
			if (Math.abs(miles) >= 1)
			{
				string = String.format("%7d mi", miles);
			}
			else
			{
				int feet = (int) Math.round(WWMath.convertMetersToFeet(meters));
				string = String.format("%7d ft", feet);
			}
			break;
		case METRIC:
			if (Math.abs(meters) > 1000)
			{
				double km = meters / 1e3;
				string = String.format("%7.4f km", km);
			}
			else
			{
				string = String.format("%7.4f m", meters);
			}
			break;
		default:
			string = "";
			break;
		}
		return string;
	}

	public String getAngleUnit()
	{
		return "\u00B0";
	}

	public String getLengthUnit()
	{
		String string;
		switch (this)
		{
		case IMPERIAL:
			string = "ft";
			break;
		case METRIC:
			string = "m";
			break;
		default:
			string = "";
			break;
		}
		return string;
	}

	public String getRainUnit()
	{
		String string;
		switch (this)
		{
		case IMPERIAL:
			string = "in";
			break;
		case METRIC:
			string = "mm";
			break;
		default:
			string = "";
			break;
		}
		return string;
	}

	public String getWindSpeedUnit()
	{
		String string;
		switch (this)
		{
		case IMPERIAL:
			string = "mph";
			break;
		case METRIC:
			string = "km/h";
			break;
		default:
			string = "";
			break;
		}
		return string;
	}

	public String getTemperatureUnit()
	{
		String string;
		switch (this)
		{
		case IMPERIAL:
			string = "\u00B0F";
			break;
		case METRIC:
			string = "\u00B0C";
			break;
		default:
			string = "";
			break;
		}
		return string;
	}

	public String getHumidityUnit()
	{
		return "%";
	}

	public String getLightningUnit()
	{
		String string;
		switch (this)
		{
		case IMPERIAL:
			string = "mi^2/yr";
			break;
		case METRIC:
			string = "km^2/yr";
			break;
		default:
			string = "";
			break;
		}
		return string;
	}
}
