package wave.audio;

public class FadeEffect implements Runnable
{
	public static final int STEPS = 20;
	
	protected PlaySound sound;
	protected PlayCIPICSound cipicSound;
	protected long durationMs;
	protected long stepMs;
	
	public FadeEffect(PlaySound sound, long durationMs)
	{
		this.sound = sound;
		this.durationMs = durationMs;
		this.stepMs = this.durationMs / STEPS;
	}
	
	public FadeEffect(PlayCIPICSound cipicSound, long durationMs)
	{
		this.cipicSound = cipicSound;
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
				if (this.sound != null)
				{
					this.sound.setGain(-step);
				}
				if (this.cipicSound != null)
				{
					this.cipicSound.setGain(-step);
				}
				Thread.sleep(stepMs);
			}
			catch (InterruptedException e)
			{
				
			}
		}
		this.sound.stop();
	}
}
