package wave.infrastructure.handlers.weather;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.audio.WindSounds;

//TODO fixed wind clipping
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
		this.wind.fadeStop(1500);
		this.wind.playAudio();
	}

	public void setIntensity(double intensity)
	{
		if (this.intensity != intensity)
		{
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
		if (this.direction != direction)
		{
			this.direction = direction;
			this.wind.setHeading(direction);
		}
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
		Platform.runLater(() -> 
		{
			@SuppressWarnings("unchecked")
			SimpleObjectProperty<Double> obs = (SimpleObjectProperty<Double>) observable;
			if (obs.getName() == "spd")
			{
				if (Math.abs(this.intensity - newValue) >= 1)
				{
					this.setIntensity(newValue);
					this.playAudio();
				}
			}
			else if (obs.getName() == "dir")
			{
				if (Math.abs(this.direction - newValue) >= 1)
				{
					this.setDirection(newValue);
					this.playAudio();
				}
			}
		});
	}
}
