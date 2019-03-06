package wave.infrastructure.core;

public enum AudioListener
{
	CAMERA("Camera"),
	MARKER("Marker");
	
	private String name;
	
	AudioListener(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
