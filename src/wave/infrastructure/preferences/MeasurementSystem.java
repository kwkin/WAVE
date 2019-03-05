package wave.infrastructure.preferences;

import gov.nasa.worldwind.util.UnitsFormat;
import gov.nasa.worldwind.util.WWMath;

public enum MeasurementSystem
{
	METRIC(UnitsFormat.METRIC_SYSTEM),
	IMPERIAL(UnitsFormat.IMPERIAL_SYSTEM);
	
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
		switch(this)
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
}
