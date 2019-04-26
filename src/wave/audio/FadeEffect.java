package wave.audio;

public class FadeEffect implements Runnable
{
	public static final int STEPS = 20;
	
	protected PlaySound sound;
	protected long durationMs;
	protected long stepMs;
	
	public FadeEffect(PlaySound sound, long durationMs)
	{
		this.sound = sound;
		this.durationMs = durationMs;
		this.stepMs = this.durationMs / STEPS;
	}
	
	@Override
	public void run()
	{
		for (int step = 1; step < STEPS + 1; ++step)
		{
			try
			{
				this.sound.setGain(-step);
				Thread.sleep(stepMs);
			}
			catch (InterruptedException e)
			{
				
			}
		}
		this.sound.stop();
	}
}
