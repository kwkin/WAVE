package wave.infrastructure.handlers.weather;

import gov.nasa.worldwind.geom.Position;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import wave.infrastructure.WaveSession;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.WindLayer;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class WeatherHandler
{
	private final ObjectProperty<Double> latitude;
	private final ObjectProperty<Double> longitude;
	private final ObjectProperty<Double> elevation;
	private final ObjectProperty<Double> rain;
	private final ObjectProperty<Double> windDirection;
	private final ObjectProperty<Double> windSpeed;
	private final ObjectProperty<Double> humidity;
	private final ObjectProperty<Double> temperature;
	private final ObjectProperty<Double> lightning;

	private KMLLayer rainLayer;
	private KMLLayer humidityLayer;
	private KMLLayer temperatureLayer;
	private WindLayer windLayer;
	private KMLLayer lightningLayer;
	
	private LightningSpawner lightningSpawner;
	private RainSpawner rainSpawner;
	
	public WeatherHandler(WaveSession session)
	{
		this.rainLayer = session.getRainLayer();
		this.humidityLayer = session.getHumidityLayer();
		this.temperatureLayer = session.getTemperatureLayer();
		this.windLayer = session.getWindLayer();
		this.lightningLayer = session.getLightningLayer();

		this.latitude = new SimpleObjectProperty<Double>(0.0);
		this.longitude = new SimpleObjectProperty<Double>(0.0);
		this.elevation = new SimpleObjectProperty<Double>(0.0);
		this.rain = new SimpleObjectProperty<Double>(0.0);
		this.windDirection = new SimpleObjectProperty<Double>(0.0);
		this.windSpeed = new SimpleObjectProperty<Double>(0.0);
		this.humidity = new SimpleObjectProperty<Double>(0.0);
		this.temperature = new SimpleObjectProperty<Double>(0.0);
		this.lightning = new SimpleObjectProperty<Double>(0.0);
		
		this.lightningSpawner = new LightningSpawner(0);
		this.lightningSpawner.startProcess();
		
		this.rainSpawner = new RainSpawner(0);
	}

	public void updateMarkerValues(Position position)
	{
		double latitude = position.latitude.degrees;
		this.setLatitude(latitude);

		double longitude = position.longitude.degrees;
		this.setLongitude(longitude);

		double elevation = position.elevation;
		this.setElevation(elevation);

		Preferences pref = PreferencesLoader.preferences();
		if (this.rainLayer != null)
		{
			if (this.rainLayer.isEnabled() || pref.getShowAllWeather())
			{
				int rain = this.rainLayer.getLayerValue(position.latitude, position.longitude, elevation);
				double rainValue = WeatherConverter.convertRainToValue(rain);
				this.setRain(rainValue);
				
				this.rainSpawner.setIntensity(rainValue);
			}
		}
		if (this.windLayer != null || pref.getShowAllWeather())
		{
			if (this.windLayer.isEnabled())
			{
				this.windLayer.setNearestMarker(position);
				double direction = this.windLayer.getDirection(position);
				this.setWindDirection(direction);
				double speed = this.windLayer.getSpeed();
				this.setWindSpeed(speed);
			}
		}
		if (this.humidityLayer != null || pref.getShowAllWeather())
		{
			if (this.humidityLayer.isEnabled())
			{
				int humidity = this.humidityLayer.getLayerValue(position.latitude, position.longitude, elevation);
				double humidityValue = WeatherConverter.convertHumidityValue(humidity);
				this.setHumidity(humidityValue);
			}
		}
		if (this.temperatureLayer != null || pref.getShowAllWeather())
		{
			if (this.temperatureLayer.isEnabled())
			{
				int temperature = this.temperatureLayer.getLayerValue(position.latitude, position.longitude, elevation);
				double temperatureValue = WeatherConverter.convertTempToValue(temperature);
				this.setTemperature(temperatureValue);
			}
		}
		if (this.lightningLayer != null || pref.getShowAllWeather())
		{
			if (this.lightningLayer.isEnabled())
			{
				int lightning = this.lightningLayer.getLayerValue(position.latitude, position.longitude, elevation);
				double lightningValue = WeatherConverter.convertLightningToValue(lightning);
				this.setLightning(lightningValue);
				
				if (lightningValue >= 0.0001)
				{
					this.lightningSpawner.stopProcess();
					this.lightningSpawner = new LightningSpawner(lightningValue);
					this.lightningSpawner.startProcess();
				}
			}
		}
	}
	
	public boolean showRain()
	{
		boolean show = false;
		if (this.rainLayer != null)
		{
			if (this.rainLayer.isEnabled() || PreferencesLoader.preferences().getShowAllWeather())
			{
				show = true;
			}
		}
		return show;
	}
	
	public boolean showWind()
	{
		boolean show = false;
		if (this.windLayer != null)
		{
			if (this.windLayer.isEnabled() || PreferencesLoader.preferences().getShowAllWeather())
			{
				show = true;
			}
		}
		return show;
	}
	
	public boolean showLightning()
	{
		boolean show = false;
		if (this.lightningLayer != null)
		{
			if (this.lightningLayer.isEnabled() || PreferencesLoader.preferences().getShowAllWeather())
			{
				show = true;
			}
		}
		return show;
	}
	
	public boolean showTemperature()
	{
		boolean show = false;
		if (this.temperatureLayer != null)
		{
			if (this.temperatureLayer.isEnabled() || PreferencesLoader.preferences().getShowAllWeather())
			{
				show = true;
			}
		}
		return show;
	}
	
	public boolean showHumidity()
	{
		boolean show = false;
		if (this.humidityLayer != null)
		{
			if (this.humidityLayer.isEnabled() || PreferencesLoader.preferences().getShowAllWeather())
			{
				show = true;
			}
		}
		return show;
	}

	public double getLatitude()
	{
		return this.latitude.get();
	}

	public void setLatitude(double latitude)
	{
		this.latitude.set(latitude);
	}

	public ObjectProperty<Double> latitudeProperty()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude.get();
	}

	public void setLongitude(double longitude)
	{
		this.longitude.set(longitude);
	}

	public ObjectProperty<Double> longitudeProperty()
	{
		return this.longitude;
	}

	public double getElevation()
	{
		return this.elevation.get();
	}

	public void setElevation(double elevation)
	{
		this.elevation.set(elevation);
	}

	public ObjectProperty<Double> elevationProperty()
	{
		return this.elevation;
	}

	public double getRain()
	{
		return this.rain.get();
	}

	public void setRain(double rain)
	{
		this.rain.set(rain);
	}

	public ObjectProperty<Double> rainProperty()
	{
		return this.rain;
	}

	public double getWindDirection()
	{
		return this.windDirection.get();
	}

	public void setWindDirection(double windDirection)
	{
		this.windDirection.set(windDirection);
	}

	public ObjectProperty<Double> windDirectionProperty()
	{
		return this.windDirection;
	}

	public double getWindSpeed()
	{
		return this.windSpeed.get();
	}

	public void setWindSpeed(double windSpeed)
	{
		this.windSpeed.set(windSpeed);
	}

	public ObjectProperty<Double> windSpeedProperty()
	{
		return this.windSpeed;
	}

	public double getHumidity()
	{
		return this.humidity.get();
	}

	public void setHumidity(double humidity)
	{
		this.humidity.set(humidity);
	}

	public ObjectProperty<Double> humidityProperty()
	{
		return this.humidity;
	}

	public double getTemperature()
	{
		return this.temperature.get();
	}

	public void setTemperature(double temperature)
	{
		this.temperature.set(temperature);
	}

	public ObjectProperty<Double> temperatureProperty()
	{
		return this.temperature;
	}

	public double getLightning()
	{
		return this.lightning.get();
	}

	public void setLightning(double lightning)
	{
		this.lightning.set(lightning);
	}

	public ObjectProperty<Double> lightningProperty()
	{
		return this.lightning;
	}
}
