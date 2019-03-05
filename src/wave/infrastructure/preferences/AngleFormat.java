package wave.infrastructure.preferences;

import gov.nasa.worldwind.geom.Angle;

public enum AngleFormat
{
	DMS(Angle.ANGLE_FORMAT_DMS),
	DM(Angle.ANGLE_FORMAT_DM),
	DD(Angle.ANGLE_FORMAT_DD);
	
	private String wwName;
	
	AngleFormat(String wwName)
	{
		this.wwName = wwName;
	}
	
	public String getWWName()
	{
		return this.wwName;
	}

	/**
	 * Converts the angle to a description in the specified format.
	 * 
	 * @param angle The angle to convert
	 * @param format The output format specified using Angle
	 * @return
	 */
	public String angleDescription(Angle angle)
	{
		String string = "";
		switch (this)
		{
		case DD:
			string = String.format("%7.4f\u00B0", angle.degrees);
			break;
		case DM:
			double[] degreeMinute = angle.toDMS();
			double minute = degreeMinute[1] + (degreeMinute[2] * 1 / 60);
			string = String.format("%3.0f\u00B0 %6.4f\u00B0", degreeMinute[0], minute);
			break;
		case DMS:
			string = angle.toDMSString();
			break;
		default:
			string = "";
			break;
		}
		return string;
	}
}
