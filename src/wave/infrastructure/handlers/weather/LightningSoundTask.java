package wave.infrastructure.handlers.weather;

import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

import wave.audio.SurveySounds;
import wave.audio.Thunder;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class LightningSoundTask extends TimerTask
{
	private double distance;
	private double direction;
	private long delayOffset;
	private Timer timerProcess;
	
	public LightningSoundTask(double distance, double direction, long delayOffset)
	{
		this.distance = distance;
		this.direction = direction;
		this.delayOffset = delayOffset;
		this.timerProcess = new Timer(true);
	}

	public void startProcess()
	{
		long delay = this.calculateDelay();
		this.timerProcess.schedule(this, delay);
	}
	
	public long calculateDelay()
	{
		// TODO factor in distance
		return this.delayOffset;
	}

	public void stopProcess()
	{
		this.timerProcess.cancel();
	}
	
	@Override
	public void run()
	{		
		// TODO update
		Path audio = null;
		if (this.distance == 0)
		{
			audio = Thunder.CLOSE;
		}
		else if (distance == 1)
		{
			audio = Thunder.MEDIUM;
		}
		else
		{
			audio = Thunder.FAR;
		}
		int direction = (int) this.direction;
		Preferences preferences = PreferencesLoader.preferences();
//		System.out.println(direction + " " + this.distance + " " + preferences.getMasterVolume());
		SurveySounds sound = new SurveySounds((int)direction, 25, audio, preferences.getMasterVolume(), 10000);
		sound.playAudio();
//		System.out.println("Lightning Volume: " + preferences.getMasterVolume());
//		System.out.println("Lightning Distance: " + this.distance);
//		System.out.println("Lightning Direction: " + this.direction);
	}
}
