package wave.infrastructure.handlers.weather;

import java.util.Timer;
import java.util.TimerTask;

import wave.audio.ThunderSounds;

public class LightningSoundTask extends TimerTask
{
	private double distance;
	private double direction;
	private long delayOffset;
	private Timer timerProcess;
	private ThunderSounds sounds;
	
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
		return this.delayOffset;
	}

	public void stopProcess()
	{
		this.timerProcess.cancel();
		if (this.sounds != null)
		{
			this.sounds.stopAudio();
		}
	}
	
	@Override
	public void run()
	{
		this.sounds = new ThunderSounds(this.distance, this.direction);
		this.sounds.playAudio();
	}
}
