package wave.audio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class ThunderSounds extends WeatherAudio
{
	public static final Path CLOSE = Paths.get("data", "audio", "thunder_close.wav");
	public static final Path MEDIUM = Paths.get("data", "audio", "thunder_medium.wav");
	public static final Path FAR = Paths.get("data", "audio", "thunder_far.wav");

	public static final File THUNDER_FILE = new File("thunder_sounds.wav");
	
	private double distance;
	private double scaleFactor;
	private PlaySound sound;
	
	public ThunderSounds(double heading, double distance)
	{
		super();
		this.headingAngle = heading;
		this.setDistance(distance);

		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume() * preferences.getThunderVolume();
		this.scaleFactor = volume;
	}

	public ThunderSounds(double heading, double distance, double scaleFactor)
	{
		super();
		this.setDistance(distance);
		this.headingAngle = heading;
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
	public boolean playAudio()
	{
		boolean hasStopped = false;
		if (sound != null)
		{
			hasStopped = this.sound.stop();
		}
		// @formatter:off
		this.sound = new PlaySound(
				THUNDER_FILE,
				this.getHeadingIndex(), 
				this.getElevationIndex(), 
				this.soundPath, 
				this.hrtf, 
				this.scaleFactor, 
				this.duration,
				false);
		// @formatter:on
		Thread thread = new Thread(this.sound);
		thread.start();
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
	
	public void setDistance(double distance)
	{
		this.distance = distance;

		if (distance <= 0)
		{
			this.soundPath = CLOSE;
		}
		else if (distance < 4)
		{
			this.soundPath = MEDIUM;
		}
		else
		{
			this.soundPath = FAR;
		}
	}
	
	public double getDistance()
	{
		return this.distance;
	}
}