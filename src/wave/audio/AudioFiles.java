package wave.audio;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum AudioFiles
{
	RAIN_LIGHTEST("Lighest Rain", Paths.get("data", "audio", "rain_lightest_fade.wav")),
	RAIN_LIGHT("Ligh Rain", Paths.get("data", "audio", "rain_light_fade.wav")),
	RAIN_MEDIUM("Medium Rain", Paths.get("data", "audio", "rain_medium_fade.wav")),
	RAIN_HEAVY("Heavy Rain", Paths.get("data", "audio", "rain_heavy_fade.wav")),
	RAIN_HEAVIEST("Heaviest Rain", Paths.get("data", "audio", "rain_heaviest_fade.wav")),
	WIND_LIGHTEST("Lightest Wind", Paths.get("data", "audio", "wind_lightest_fade.wav")),
	WIND_LIGHT("Light Wind", Paths.get("data", "audio", "wind_light_fade.wav")),
	WIND_MEDIUM("Medium Wind", Paths.get("data", "audio", "wind_medium_fade.wav")),
	WIND_HEAVY("Heavy Wind", Paths.get("data", "audio", "wind_heavy_fade.wav")),
	WIND_HEAVIEST("Heaviest Wind", Paths.get("data", "audio", "wind_heaviest_fade.wav")),
	THUNDER_FAR("Far Thunder", Paths.get("data", "audio", "thunder_far.wav")),
	THUNDER_MEDIUM("Medium Thunder", Paths.get("data", "audio", "thunder_medium.wav")),
	THUNDER_CLOSE("Close Thunder", Paths.get("data", "audio", "thunder_close.wav"));
	
	private String name;
	private Path path;
	
	AudioFiles(String name, Path file)
	{
		this.name = name;
		this.path = file;
	}
	
	public Path getPath()
	{
		return this.path;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
