package wave.audio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import wave.infrastructure.handlers.weather.RainColorValues;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class RainSounds extends WeatherAudio
{
	public static final Path LIGHT = Paths.get("data", "audio", "rain_light_loop.wav");
	public static final Path MEDIUM = Paths.get("data", "audio", "rain_medium_loop.wav");
	public static final Path HEAVY = Paths.get("data", "audio", "rain_heavy_loop.wav");
	
	public static final Path LIGHT_FADE = Paths.get("data", "audio", "rain_light_fade.wav");
	public static final Path MEDIUM_FADE = Paths.get("data", "audio", "rain_medium_fade.wav");
	public static final Path HEAVY_FADE = Paths.get("data", "audio", "rain_heavy_fade.wav");

	public static final File RAIN_FILE = new File("rain_sounds.wav");
	
	private double intensity;
	private double scaleFactor;
	private boolean isLoop;
	private PlaySound sound;
	
	public RainSounds(double intensity)
	{
		super();
		this.setIntensity(intensity);

		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume() * preferences.getRainVolume();
		this.scaleFactor = volume;
	}
	
	public RainSounds(double intensity, boolean isLoop)
	{
		super();
		this.setIntensity(intensity);
		this.isLoop = isLoop;

		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume() * preferences.getRainVolume();
		this.scaleFactor = volume;
	}

	public RainSounds(double intensity, double scaleFactor)
	{
		super();
		this.setIntensity(intensity);
		this.isLoop = true;
		this.scaleFactor = scaleFactor;
	}
	
	public RainSounds(double intensity, boolean isLoop, double scaleFactor)
	{
		super();
		this.setIntensity(intensity);
		this.isLoop = isLoop;
		this.scaleFactor = scaleFactor;
	}
	
	@Override
	public boolean stopAudio()
	{
		boolean hasStopped = false;
		if (this.sound != null)
		{
			hasStopped = this.sound.stop();
		}
		return hasStopped;
	}
	
	@Override
	public void playAudio()
	{
		// @formatter:off
		if (this.intensity > 0)
		{
			this.sound = new PlaySound(
					RAIN_FILE,
					this.getHeadingIndex(), 
					this.getElevationIndex(), 
					this.soundPath, 
					this.hrtf, 
					this.scaleFactor, 
					this.duration,
					this.isLoop);
			// @formatter:on
			Thread thread = new Thread(this.sound);
			thread.start();
		}
	}

	public void setScaleFactor(double scaleFactor)
	{
		this.scaleFactor = scaleFactor;
	}
	
	public double getScaleFactor()
	{
		return scaleFactor;
	}
	
	public void setIsLoop(boolean isLoop)
	{
		this.isLoop = isLoop;
	}

	public boolean isLoop()
	{
		return this.isLoop;
	}

	public boolean fadeStop(long duration)
	{
		boolean hasStopped = false;
		if (this.sound != null)
		{
			FadeEffect fade = new FadeEffect(this.sound, duration);
			Thread thread = new Thread(fade);
			thread.start();
			hasStopped = true;
		}
		return hasStopped;
	}
	
	public void setIntensity(double intensity)
	{
		this.intensity = intensity;

		if (intensity <= 0)
		{
			this.soundPath = null;
		}
		else if (intensity < RainColorValues.RAIN_15.getMeasurement())
		{
			this.soundPath = RainSounds.LIGHT;
		}
		else if (intensity < RainColorValues.RAIN_32.getMeasurement())
		{
			this.soundPath = RainSounds.MEDIUM;
		}
		else if (intensity < RainColorValues.RAIN_152.getMeasurement())
		{
			this.soundPath = RainSounds.HEAVY;
		}
	}
	
	public double getIntensity(double intensity)
	{
		return this.intensity;
	}
}