package wave.infrastructure.handlers.weather;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.audio.RainSounds;

public class RainSpawner implements ChangeListener<Double>
{
	private double intensity;
	private RainSounds rain;
	private double previousIntensity;
	
	public RainSpawner(double intensity)
	{
		this.intensity = intensity;
		this.rain = new RainSounds(intensity, true);
		this.setIntensity(intensity);
	}

	public void playAudio()
	{
		// TODO add varying intensities
		if (this.previousIntensity < this.intensity)
		{
			this.rain.fadeStop(2000);
		}
		else
		{
			this.rain.fadeStop(3000);
		}
		this.rain.playAudio();
	}
	
	public void setIntensity(double intensity)
	{
		if (this.intensity != intensity)
		{
			this.previousIntensity = this.intensity;
			this.intensity = intensity;
			
			this.rain.setIntensity(intensity);
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

	@Override
	public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue)
	{
		this.setIntensity(newValue);
		Platform.runLater(() -> 
		{
			this.playAudio();
		});
	}
}
