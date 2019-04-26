package wave.infrastructure.handlers.weather;

import wave.audio.RainSounds;

public class RainSpawner
{
	private double intensity;
	private RainSounds rain;
	
	public RainSpawner(double intensity)
	{
		this.intensity = intensity;
		this.rain = new RainSounds(intensity, true);
		this.setIntensity(intensity);
	}
	
	public void setIntensity(double intensity)
	{
		if (this.intensity != intensity)
		{
			this.intensity = intensity;
			
			this.rain.setIntensity(intensity);
			
			// TODO add a fade effect
			// TODO add varying intensities
			this.rain.playAudio();
		}
	}
	
	public double getIntensity()
	{
		return this.intensity;
	}
	
	public void stopProcess()
	{
		this.rain.stopAudio();
	}
}
