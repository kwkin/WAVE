package wave.infrastructure.layers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.text.DecimalFormat;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.infrastructure.WaveSession;
import wave.infrastructure.handlers.WeatherHandler;
import wave.infrastructure.models.DraggableMarker;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class WeatherAnnotationLayer extends AnnotationLayer
{
	private WeatherHandler handler;
	private GlobeAnnotation annotation;

	public WeatherAnnotationLayer(WaveSession session, DraggableMarker marker)
	{
		this.handler = session.getWeatherHander();

		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setCornerRadius(10);
		defaultAttributes.setInsets(new Insets(8, 8, 8, 8));
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, .5f));
		defaultAttributes.setTextColor(Color.WHITE);
		defaultAttributes.setDrawOffset(new Point(25, 25));
		defaultAttributes.setDistanceMinScale(.5);
		defaultAttributes.setDistanceMaxScale(2);
		defaultAttributes.setDistanceMinOpacity(.5);
		defaultAttributes.setLeaderGapWidth(14);
		defaultAttributes.setSize(new Dimension(200, 0));

		this.annotation = new GlobeAnnotation("", marker.getPosition(), new Font("Segoi-UI", Font.BOLD, 16));
		this.annotation.getAttributes().setDefaults(defaultAttributes);
		this.annotation.getAttributes().setAdjustWidthToText(AVKey.SIZE_FIXED);
		this.annotation.getAttributes().setImageOpacity(.6);
		this.annotation.getAttributes().setImageScale(.7);
		this.annotation.getAttributes().setImageOffset(new Point(1, 1));
		this.annotation.getAttributes().setInsets(new Insets(6, 6, 6, 6));
		this.annotation.setDragEnabled(false);
		this.addAnnotation(this.annotation);

		if (PreferencesLoader.preferences().getShowAnnotation())
		{
			addAnnotation(annotation);
			marker.positionProperty().addListener(new ChangeListener<Position>()
			{
				@Override
				public void changed(ObservableValue<? extends Position> observable, Position oldValue,
						Position newValue)
				{
					if (newValue != oldValue)
					{
						updateAnnotation(newValue);
					}
				}
			});
		}
		else
		{
			removeAnnotation(annotation);
		}
		PreferencesLoader.preferences().showAnnotationProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				setEnabled(newValue);
			}
		});
	}

	public void updateAnnotation(Position newValue)
	{
		this.annotation.setPosition(newValue);
		StringBuilder markerText = new StringBuilder();
		Preferences pref = PreferencesLoader.preferences();
		boolean firstLine = true;
		if (pref.getShowPosition())
		{
			markerText.append("Latitude: ");
			markerText.append(pref.getAngleUnitDisplay().angleDescription(newValue.getLatitude()));
			markerText.append("\nLongitude: ");
			markerText.append(pref.getAngleUnitDisplay().angleDescription(newValue.getLongitude()));
			markerText.append("\nElevation: ");
			markerText.append(pref.getLengthUnitDisplay().lengthDescription(newValue.getElevation()));
		}
		// TODO append units
		DecimalFormat weatherFormat = new DecimalFormat(".##");
		if (this.handler.showRain())
		{
			if (!firstLine)
			{
				markerText.append("\n");
			}
			else
			{
				firstLine = false;
			}
			markerText.append("Rain: ");
			double rain = this.handler.getRain();
			markerText.append(weatherFormat.format(rain));
		}
		if (this.handler.showWind())
		{
			if (!firstLine)
			{
				markerText.append("\n");
			}
			else
			{
				firstLine = false;
			}
			markerText.append("Wind Spd: ");
			double speed = this.handler.getWindSpeed();
			markerText.append(weatherFormat.format(speed));
			markerText.append("\nWind Dir: ");
			double direction = this.handler.getWindDirection();
			markerText.append(weatherFormat.format(direction));
		}
		if (this.handler.showLightning())
		{
			if (!firstLine)
			{
				markerText.append("\n");
			}
			else
			{
				firstLine = false;
			}
			markerText.append("Lightning: ");
			double temperature = this.handler.getLightning();
			markerText.append(weatherFormat.format(temperature));
		}
		if (this.handler.showTemperature())
		{
			if (!firstLine)
			{
				markerText.append("\n");
			}
			else
			{
				firstLine = false;
			}
			markerText.append("Temperature: ");
			double temperature = this.handler.getTemperature();
			markerText.append(weatherFormat.format(temperature));
		}
		if (this.handler.showHumidity())
		{
			if (!firstLine)
			{
				markerText.append("\n");
			}
			else
			{
				firstLine = false;
			}
			markerText.append("Humidity: ");
			double temperature = this.handler.getHumidity();
			markerText.append(weatherFormat.format(temperature));
		}
		this.annotation.setText(markerText.toString());
	}
}
