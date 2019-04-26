package wave.infrastructure.handlers.weather;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LightningSpawner extends TimerTask
{
	public static double MIN_LIGHTNING = 0.5;
	public static double MAX_LIGHTNING = 10;
	
	private Random random;
	private double baseRate;
	private Timer timerProcess;
		
	private LightningSoundTask lightningTasks;
	
	public LightningSpawner(double baseRate)
	{
		this.baseRate = baseRate;
		this.timerProcess = new Timer(true);
		this.random = new Random();
	}
	
	public void setBaserate(double baseRate)
	{
		this.baseRate = baseRate;
	}
	
	public double getBaserate()
	{
		return this.baseRate;
	}

	public void startProcess()
	{
		if (this.baseRate > 0)
		{
			long baseRate = this.calculateRate();
			if (baseRate > 0)
			{
				this.timerProcess.scheduleAtFixedRate(this, baseRate, baseRate);
			}
		}
	}
	
	public void stopProcess()
	{
		this.timerProcess.cancel();
		if (this.lightningTasks != null)
		{
			this.lightningTasks.stopProcess();
		}
	}
	
	public long calculateRate()
	{
		Random random = new Random();
		long rate = (long) ((double)(1 / this.baseRate) * 500000);
		double deviation = 2000 * (random.nextDouble() - 0.5);
		rate += deviation;
		rate = Math.max(1000, rate);
		return rate;
	}

	@Override
	public void run()
	{
		double lightningDir = this.random.nextInt(10);
		double lightningDist = this.random.nextInt(360);
		long deviation = (long) (2000 * this.random.nextDouble());
		this.lightningTasks = new LightningSoundTask(lightningDist, lightningDir, deviation);
		this.lightningTasks.startProcess();
	}
}
