package wave.infrastructure.handlers.weather;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.audio.WindSounds;

public class WindSpawner implements ChangeListener<Double>
{
	private double intensity;
	private double direction;
	private WindSounds wind;

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
		// TODO add a fade effect
		// TODO add varying intensities
		this.wind.playAudio();
	}

	public void setIntensity(double intensity)
	{
		this.intensity = intensity;
		this.wind.setIntensity(intensity);
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
