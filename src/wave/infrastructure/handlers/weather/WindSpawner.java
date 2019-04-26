package wave.infrastructure.handlers.weather;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.audio.WindSounds;

public class WindSpawner implements ChangeListener<Double>
{
	private double intensity;
	private double direction;
	private WindSounds wind;
	private double previousIntensity;

	public WindSpawner(double intensity, double direction)
	{
		this.intensity = intensity;
		this.direction = direction;
		this.wind = new WindSounds(direction, intensity, true);
		this.setIntensity(intensity);
		this.setDirection(direction);
	}

	public void playAudio()
	{
		// TODO add varying intensities
		if (Math.abs(this.previousIntensity - this.intensity) > 1)
		{
			if (this.previousIntensity < this.intensity)
			{
				this.wind.fadeStop(1500);
			}
			else
			{
				this.wind.fadeStop(1500);
			}
			this.wind.playAudio();
		}
	}

	public void setIntensity(double intensity)
	{
		if (this.intensity != intensity)
		{
			if (Math.abs(this.previousIntensity - this.intensity) > 1)
			{
				this.previousIntensity = this.intensity;
			}
			this.intensity = intensity;
			
			this.wind.setIntensity(intensity);
		}
	}

	public double getIntensity()
	{
		return this.intensity;
	}

	public void setDirection(double direction)
	{
		this.direction = direction;
		this.wind.setHeading(direction);
	}

	public double getDirection()
	{
		return this.direction;
	}

	public void stopProcess()
	{
		this.wind.stopAudio();
	}

	@Override
	public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue)
	{
		this.setDirection(newValue);
		this.playAudio();
	}
}
