package wave.infrastructure.handlers.weather;

import java.util.Timer;
import java.util.TimerTask;

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
		System.out.println("Lightning Distance: " + this.distance);
		System.out.println("Lightning Direction: " + this.direction);
	}
}
