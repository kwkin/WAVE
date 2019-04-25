package wave.infrastructure.handlers.weather;

import java.nio.file.Path;

import wave.audio.Rain;
import wave.infrastructure.preferences.PreferencesLoader;

public class RainSpawner
{
	private double intensity;
	private Rain rain;
	
	public RainSpawner(double intensity)
	{
		this.intensity = intensity;
		this.setIntensity(intensity);
	}
	
	public void setIntensity(double intensity)
	{
		this.intensity = intensity;
		
//		Path rainClip = null;
//		if (this.rain != null)
//		{
//			this.rain.stop();
//		}
//		
//		if (intensity < RainColorValues.RAIN_15.getMeasurement())
//		{
//			rainClip = Rain.LIGHT;
//			this.rain = new Rain(-1, -1, rainClip, PreferencesLoader.preferences().getMasterVolume());
//			this.rain.play();
//		}
//		else if (intensity < RainColorValues.RAIN_32.getMeasurement())
//		{
//			rainClip = Rain.MEDIUM;
//			this.rain = new Rain(-1, -1, rainClip, PreferencesLoader.preferences().getMasterVolume());
//			this.rain.play();
//		}
//		else if (intensity < RainColorValues.RAIN_152.getMeasurement())
//		{
//			rainClip = Rain.HEAVY;
//			this.rain = new Rain(-1, -1, rainClip, PreferencesLoader.preferences().getMasterVolume());
//			this.rain.play();
//		}
	}
	
	public double getIntensity()
	{
		return this.intensity;
	}
}
