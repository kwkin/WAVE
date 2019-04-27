package wave.audio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class WindSounds extends WeatherAudio
{
	public static final Path BREEZE = Paths.get("data", "audio", "wind_lightest_loop.wav");
	public static final Path LIGHT = Paths.get("data", "audio", "wind_light_loop.wav");
	public static final Path MEDIUM = Paths.get("data", "audio", "wind_medium_loop.wav");
	public static final Path HIGH = Paths.get("data", "audio", "wind_heavy_loop.wav");
	public static final Path EXTREME = Paths.get("data", "audio", "wind_heaviest_loop.wav");
	
	public static final Path BREEZE_FADE = Paths.get("data", "audio", "wind_lightest_fade.wav");
	public static final Path LIGHT_FADE = Paths.get("data", "audio", "wind_light_fade.wav");
	public static final Path MEDIUM_FADE = Paths.get("data", "audio", "wind_medium_fade.wav");
	public static final Path HIGH_FADE = Paths.get("data", "audio", "wind_heavy_fade.wav");
	public static final Path EXTREME_FADE = Paths.get("data", "audio", "wind_heaviest_fade.wav");

	public static final File WIND_FILE = new File("wind_sounds.wav");

	private double intensity;
	private double scaleFactor;
	private boolean isLoop;
	private Thread thread;
	private PlaySound sound;
	
	public WindSounds(double heading, double intensity)
	{
		super();
		this.headingAngle = heading;
		this.intensity = intensity;

		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume() * preferences.getThunderVolume();
		this.scaleFactor = volume;
	}
	
	public WindSounds(double heading, double intensity, boolean isLoop)
	{
		super();
		this.headingAngle = heading;
		this.intensity = intensity;
		this.isLoop = isLoop;

		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume() * preferences.getThunderVolume();
		this.scaleFactor = volume;
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
		if (this.intensity > 0)
		{
			int framePosition = 0;
			Path previousPath = null;
			if (this.sound != null)
			{
				previousPath = this.sound.getSoundToPlay();
				framePosition = this.sound.getPosition();
			}
			// @formatter:off
			this.sound = new PlaySound(
					WIND_FILE,
					this.headingAngle, 
					this.elevationIndex, 
					this.soundPath, 
					this.hrtf, 
					this.scaleFactor, 
					this.duration,
					this.isLoop);
			// @formatter:on
			if (previousPath == this.soundPath)
			{
				this.sound.setPosition(framePosition);
			}
			this.thread = new Thread(this.sound);
			this.thread.start();
		}
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
	
	public void setIntensity(double intensity)
	{
		this.intensity = intensity;

		if (intensity <= 5)
		{
			this.soundPath = null;
		}
		else if (intensity < 10)
		{
			this.soundPath = WindSounds.BREEZE;
		}
		else if (intensity < 20)
		{
			this.soundPath = WindSounds.LIGHT;
		}
		else if (intensity < 30)
		{
			this.soundPath = WindSounds.MEDIUM;
		}
		else if (intensity < 40)
		{
			this.soundPath = WindSounds.HIGH;
		}
		else
		{
			this.soundPath = WindSounds.EXTREME;
		}
	}
	
	public double getIntensity(double intensity)
	{
		return this.intensity;
	}
}
