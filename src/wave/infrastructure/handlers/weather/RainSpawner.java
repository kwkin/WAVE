package wave.infrastructure.handlers.weather;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.audio.RainSounds;

public class RainSpawner implements ChangeListener<Double>
{
	private double intensity;
	private RainSounds rain;
	
	public RainSpawner(double intensity)
	{
		this.intensity = intensity;
		this.rain = new RainSounds(intensity, true);
		this.setIntensity(intensity);
	}

	public void playAudio()
	{
		// TODO add a fade effect
		// TODO add varying intensities
		this.rain.playAudio();
	}
	
	public void setIntensity(double intensity)
	{
		if (this.intensity != intensity)
		{
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
