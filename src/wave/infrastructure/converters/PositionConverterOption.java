package wave.infrastructure.converters;

public enum PositionConverterOption
{
	LATITUDE("Latitude"),
	LONGITUDE("Longitude"),
	ALTITUDE("Altitude"),
	ELEVATION("Elevation"),
	ALL("ALL");

	private String name;
	
	PositionConverterOption(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
