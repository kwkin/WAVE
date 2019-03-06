package wave.infrastructure.converters;

import java.util.regex.Pattern;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import javafx.util.StringConverter;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.AngleFormat;
import wave.infrastructure.core.MeasurementSystem;

public class PositionConverter extends StringConverter<Position>
{
	protected WaveSession session;
	protected PositionConverterOption option;
	protected Pattern pattern;
	protected MeasurementSystem elevationSystem = MeasurementSystem.METRIC;
	protected AngleFormat angleFormat = AngleFormat.DD;

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
	public void setElevationSystem(MeasurementSystem format)
	{
		this.elevationSystem = format;
	}

	/**
	 * Returns the current elevation system
	 * 
	 * @return The current elevation system
	 */
	public MeasurementSystem getElevationSystem()
	{
		return this.elevationSystem;
	}

	/**
	 * Sets the angle format
	 * 
	 * @param format The format specified using Angle
	 */
	public void setAngleFormat(AngleFormat format)
	{
		this.angleFormat = format;
	}

	/**
	 * Returns the current Angle format
	 * 
	 * @return The current Angle format
	 */
	public AngleFormat getAngleFormat()
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
			value = this.elevationSystem.lengthDescription(position.getAltitude());
			break;
		case ELEVATION:
			value = this.elevationSystem.lengthDescription(position.elevation);
			break;
		case LATITUDE:
			value = this.angleFormat.angleDescription(position.latitude);
			break;
		case LONGITUDE:
			value = this.angleFormat.angleDescription(position.longitude);
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
