package wave.infrastructure.converters;

import java.util.regex.Pattern;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.util.UnitsFormat;
import javafx.util.StringConverter;
import wave.infrastructure.WaveSession;
import wave.infrastructure.util.UnitToString;

public class PositionConverter extends StringConverter<Position>
{
	protected WaveSession session;
	protected PositionConverterOption option;
	protected Pattern pattern;
	protected String elevationSystem = UnitsFormat.METRIC_SYSTEM;
	protected String angleFormat = Angle.ANGLE_FORMAT_DD;

	public PositionConverter(WaveSession session, PositionConverterOption option)
	{
		this.session = session;
		this.option = option;
		this.pattern = Pattern.compile("[0-9]+[.][0-9]+");
	}

	/**
	 * Sets the elevation system
	 * 
	 * @param format The unit system specified using UnitsFormat
	 */
	public void setElevationSystem(String format)
	{
		switch (format)
		{
		case UnitsFormat.IMPERIAL_SYSTEM:
		case UnitsFormat.METRIC_SYSTEM:
			this.elevationSystem = format;
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the current elevation system
	 * 
	 * @return The current elevation system
	 */
	public String getElevationSystem()
	{
		return this.elevationSystem;
	}

	/**
	 * Sets the angle format
	 * 
	 * @param format The format specified using Angle
	 */
	public void setAngleFormat(String format)
	{
		switch (format)
		{
		case Angle.ANGLE_FORMAT_DD:
		case Angle.ANGLE_FORMAT_DM:
		case Angle.ANGLE_FORMAT_DMS:
			this.angleFormat = format;
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the current Angle format
	 * 
	 * @return The current Angle format
	 */
	public String getAngleFormat()
	{
		return this.angleFormat;
	}

	@Override
	public String toString(Position position)
	{
		String value = "";
		switch (this.option)
		{
		case ALL:
			value = position.toString();
			break;
		case ALTITUDE:
			value = UnitToString.distanceDescription(position.getAltitude(), this.elevationSystem);
			break;
		case ELEVATION:
			value = UnitToString.distanceDescription(position.elevation, this.elevationSystem);
			break;
		case LATITUDE:
			value = UnitToString.angleDescription(position.latitude, this.angleFormat);
			break;
		case LONGITUDE:
			value = UnitToString.angleDescription(position.longitude, this.angleFormat);
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public Position fromString(String string)
	{
		double latitude = 0;
		double longitude = 0;
		double elevation = 0;
		switch (this.option)
		{
		case ALL:
			break;
		case ALTITUDE:
			elevation = Double.valueOf(string);
			break;
		case ELEVATION:
			elevation = Double.valueOf(string);
			break;
		case LATITUDE:
			string = this.pattern.matcher(string).group();
			latitude = Double.valueOf(string);
			elevation = this.session.getModel().getGlobe().getElevation(Angle.fromDegrees(latitude),
					Angle.fromDegrees(longitude));
			break;
		case LONGITUDE:
			string = this.pattern.matcher(string).group();
			longitude = Double.valueOf(string);
			elevation = this.session.getModel().getGlobe().getElevation(Angle.fromDegrees(latitude),
					Angle.fromDegrees(longitude));
			break;
		default:
			break;
		}
		return Position.fromDegrees(latitude, longitude, elevation);
	}

}
