package wave.infrastructure.util;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.util.UnitsFormat;
import gov.nasa.worldwind.util.WWMath;

public class UnitToString
{
	/**
	 * Converts the angle to a description in the specified format.
	 * 
	 * @param angle The angle to convert
	 * @param format The output format specified using Angle
	 * @return
	 */
	public static String angleDescription(Angle angle, String format)
	{
		String string = "";
		switch (format)
		{
		case Angle.ANGLE_FORMAT_DD:
			string = String.format("%7.4f\u00B0", angle.degrees);
			break;
		case Angle.ANGLE_FORMAT_DM:
			double[] degreeMinute = angle.toDMS();
			double minute = degreeMinute[1] + (degreeMinute[2] * 1 / 60);
			string = String.format("%3.0f\u00B0 %6.4f\u00B0", degreeMinute[0], minute);
			break;
		case Angle.ANGLE_FORMAT_DMS:
			string = angle.toDMSString();
			break;
		default:
			string = angle.toDMSString();
			break;
		}
		return string;
	}
	
	/**
	 * Converts meters to a unit description in the specified unit system
	 * 
	 * @param meters The unit to convert
	 * @param unitSystem The output system of units to specified using UnitsFormat
	 * @return
	 */
	public static String distanceDescription(double meters, String unitSystem)
	{
		String string;
		if (UnitsFormat.METRIC_SYSTEM.equals(unitSystem))
		{
			if (Math.abs(meters) > 1000)
			{
				double km = meters / 1e3;
				string = String.format("%7.4f km", km);
			}
			else
			{
				string = String.format("%7.4f m", meters);
			}
		}
		else
		{
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
		}
		return string;
	}
}
