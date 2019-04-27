package wave.audio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import wave.infrastructure.handlers.weather.RainColorValues;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class RainSounds extends WeatherAudio
{
	public static final Path LIGHTEST = Paths.get("data", "audio", "rain_lightest_loop.wav");
	public static final Path LIGHT = Paths.get("data", "audio", "rain_light_loop.wav");
	public static final Path MEDIUM = Paths.get("data", "audio", "rain_medium_loop.wav");
	public static final Path HEAVY = Paths.get("data", "audio", "rain_heavy_loop.wav");
	public static final Path HEAVIEST = Paths.get("data", "audio", "rain_heaviest_loop.wav");
	
	public static final Path LIGHT_FADE = Paths.get("data", "audio", "rain_light_fade.wav");
	public static final Path MEDIUM_FADE = Paths.get("data", "audio", "rain_medium_fade.wav");
	public static final Path HEAVY_FADE = Paths.get("data", "audio", "rain_heavy_fade.wav");

	public static final File RAIN_FILE = new File("rain_sounds.wav");
	public static final File RAIN_MOD_FILE = new File("rain_sounds_mod.wav");
	
	private Path modSoundPath;
	private double intensity;
	private double modGain;
	private double scaleFactor;
	private boolean isLoop;
	private PlaySound baseSound;
	private PlaySound modSound;
	
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
		if (this.baseSound != null)
		{
			hasStopped = this.baseSound.stop();
		}
		if (this.modSound != null)
		{
			this.modSound.stop();
		}
		return hasStopped;
	}
	
	@Override
	public void playAudio()
	{
		// @formatter:off
		if (this.intensity > 0)
		{
			this.baseSound = new PlaySound(
					RAIN_FILE,
					this.getHeadingIndex(), 
					this.getElevationIndex(), 
					this.soundPath, 
					this.hrtf, 
					this.scaleFactor * (1 - this.modGain), 
					this.duration,
					this.isLoop);
			this.modSound = new PlaySound(
					RAIN_MOD_FILE,
					this.getHeadingIndex(), 
					this.getElevationIndex(), 
					this.modSoundPath, 
					this.hrtf, 
					this.scaleFactor * this.modGain, 
					this.duration,
					this.isLoop);
			// @formatter:on
			System.out.println(this.modGain);
			Thread baseThread = new Thread(this.baseSound);
			baseThread.start();
			Thread modThread = new Thread(this.modSound);
			modThread.start();
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
		if (this.baseSound != null)
		{
			FadeEffect fade = new FadeEffect(this.baseSound, duration);
			Thread thread = new Thread(fade);
			thread.start();
			hasStopped = true;
		}
		if (this.modSound != null)
		{
			FadeEffect modFade = new FadeEffect(this.modSound, duration);
			Thread modThread = new Thread(modFade);
			modThread.start();
		}
		return hasStopped;
	}
	
	public void setIntensity(double intensity)
	{
		this.intensity = intensity;
		this.modGain = 0;

		if (intensity <= 0)
		{
			this.soundPath = null;
			this.modSoundPath = null;
		}
		else if (intensity <= RainColorValues.RAIN_5.getMeasurement())
		{
			this.modSoundPath = RainSounds.LIGHTEST;
			this.modGain = intensity;
			this.modGain /= RainColorValues.RAIN_5.getMeasurement();
			
		}
		else if (intensity <= RainColorValues.RAIN_20.getMeasurement())
		{
			this.soundPath = RainSounds.LIGHTEST;
			this.modSoundPath = RainSounds.LIGHT;
			this.modGain = intensity - RainColorValues.RAIN_5.getMeasurement();
			this.modGain /= (RainColorValues.RAIN_20.getMeasurement() - RainColorValues.RAIN_5.getMeasurement());
		}
		else if (intensity <= RainColorValues.RAIN_32.getMeasurement())
		{
			this.soundPath = RainSounds.LIGHT;
			this.modSoundPath = RainSounds.MEDIUM;
			this.modGain = intensity - RainColorValues.RAIN_20.getMeasurement();
			this.modGain /= (RainColorValues.RAIN_32.getMeasurement() - RainColorValues.RAIN_20.getMeasurement());
		}
		else if (intensity <= RainColorValues.RAIN_64.getMeasurement())
		{
			this.soundPath = RainSounds.MEDIUM;
			this.modSoundPath = RainSounds.HEAVY;
			this.modGain = intensity - RainColorValues.RAIN_32.getMeasurement();
			this.modGain /= (RainColorValues.RAIN_64.getMeasurement() - RainColorValues.RAIN_32.getMeasurement());
		}
		else
		{
			this.soundPath = RainSounds.HEAVY;
			this.modSoundPath = RainSounds.HEAVIEST;
			this.modGain = intensity - RainColorValues.RAIN_127.getMeasurement();
			this.modGain /= (RainColorValues.RAIN_127.getMeasurement() - RainColorValues.RAIN_64.getMeasurement());
		}
		this.modGain = Math.min(this.modGain, 1);
	}
	
	public double getIntensity(double intensity)
	{
		return this.intensity;
	}
}