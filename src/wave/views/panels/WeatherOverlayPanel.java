package wave.views.panels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.layers.Layer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import wave.components.IconWeatherButton;
import wave.infrastructure.WaveSession;
import wave.infrastructure.handlers.LayerFadeTransition;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.WindLayer;

public class WeatherOverlayPanel extends BorderPane
{
	protected WaveSession session;

	protected IconWeatherButton percipitationButton;
	protected IconWeatherButton windButton;
	protected IconWeatherButton lightningButton;
	protected IconWeatherButton temperatureButton;
	protected IconWeatherButton humidityButton;

	public WeatherOverlayPanel(WaveSession session)
	{
		this.session = session;

		GridPane layerPane = new GridPane();
		layerPane.setPadding(new Insets(5, 5, 5, 5));
		layerPane.setHgap(5);
		layerPane.setVgap(5);

		ColumnConstraints nameColumn = new ColumnConstraints();
		nameColumn.setHgrow(Priority.ALWAYS);
		nameColumn.setHalignment(HPos.LEFT);
		nameColumn.setMinWidth(200);

		ColumnConstraints displayColumn = new ColumnConstraints();
		displayColumn.setHgrow(Priority.NEVER);
		displayColumn.setHalignment(HPos.CENTER);
		layerPane.getColumnConstraints().addAll(nameColumn, displayColumn);

		int layerIndex = 0;
		try
		{
			KMLLayer precipitationLayer = session.getRainLayer();
			if (precipitationLayer != null)
			{
				Slider percipitationSlider = new Slider(0, 1, precipitationLayer.getOpacity());
				layerPane.add(percipitationSlider, 1, layerIndex);
				this.linkLayerWithSlider(precipitationLayer, percipitationSlider);
				Path percipitationPath1 = Paths.get("data", "icons", "rain_selected.png");
				Image percipitationImage1 = new Image(percipitationPath1.toUri().toURL().toString());
				Path percipitationPath2 = Paths.get("data", "icons", "rain_unselected.png");
				Image percipitationImage2 = new Image(percipitationPath2.toUri().toURL().toString());
				this.percipitationButton = new IconWeatherButton("Rain", percipitationImage1, percipitationImage2);
				this.percipitationButton.setGraphicTextGap(12);
				this.linkLayerWithButton(precipitationLayer, this.percipitationButton, percipitationSlider.valueProperty());
				layerPane.add(this.percipitationButton, 0, layerIndex);
				layerIndex++;
			}

			WindLayer windLayer = session.getWindLayer();
			if (windLayer != null)
			{
				Slider windSlider = new Slider(0, 1, 1);
				layerPane.add(windSlider, 1, layerIndex);
				Path windPath1 = Paths.get("data", "icons", "wind_selected.png");
				Image windImage1 = new Image(windPath1.toUri().toURL().toString());
				Path windPath2 = Paths.get("data", "icons", "wind_unselected.png");
				Image windImage2 = new Image(windPath2.toUri().toURL().toString());
				this.windButton = new IconWeatherButton("Wind", windImage1, windImage2);
				windButton.setGraphicTextGap(12);
				this.linkLayerWithButton(windLayer, this.windButton, windSlider.valueProperty());
				layerPane.add(windButton, 0, layerIndex);
				layerIndex++;
			}

			KMLLayer lightningLayer = session.getLightningLayer();
			if (lightningLayer != null)
			{
				Slider lightningSlider = new Slider(0, 1, lightningLayer.getOpacity());
				layerPane.add(lightningSlider, 1, layerIndex);
				this.linkLayerWithSlider(lightningLayer, lightningSlider);
				Path lightningPath1 = Paths.get("data", "icons", "lightning_selected.png");
				Image lightningImage1 = new Image(lightningPath1.toUri().toURL().toString());
				Path lightningPath2 = Paths.get("data", "icons", "lightning_unselected.png");
				Image lightningImage2 = new Image(lightningPath2.toUri().toURL().toString());
				this.lightningButton = new IconWeatherButton("Lightning", lightningImage1, lightningImage2);
				this.lightningButton.setGraphicTextGap(12);
				this.linkLayerWithButton(lightningLayer, this.lightningButton, lightningSlider.valueProperty());
				layerPane.add(this.lightningButton, 0, layerIndex);
				layerIndex++;
			}

			KMLLayer temperatureLayer = session.getTemperatureLayer();
			if (temperatureLayer != null)
			{
				Slider temperatureSlider = new Slider(0, 1, temperatureLayer.getOpacity());
				layerPane.add(temperatureSlider, 1, layerIndex);
				this.linkLayerWithSlider(temperatureLayer, temperatureSlider);
				Path temperaturePath1 = Paths.get("data", "icons", "temperature_selected.png");
				Image temperatureImage1 = new Image(temperaturePath1.toUri().toURL().toString());
				Path temperaturePath2 = Paths.get("data", "icons", "temperature_unselected.png");
				Image temperatureImage2 = new Image(temperaturePath2.toUri().toURL().toString());
				this.temperatureButton = new IconWeatherButton("Temperature", temperatureImage1, temperatureImage2);
				this.temperatureButton.setGraphicTextGap(12);
				this.linkLayerWithButton(temperatureLayer, this.temperatureButton, temperatureSlider.valueProperty());
				layerPane.add(this.temperatureButton, 0, layerIndex);
				layerIndex++;
			}

			KMLLayer humidityLayer = session.getHumidityLayer();
			if (humidityLayer != null)
			{
				Slider humiditySlider = new Slider(0, 1, humidityLayer.getOpacity());
				layerPane.add(humiditySlider, 1, layerIndex);
				this.linkLayerWithSlider(humidityLayer, humiditySlider);
				Path humidityPath1 = Paths.get("data", "icons", "humidity_selected.png");
				Image humidityImage1 = new Image(humidityPath1.toUri().toURL().toString());
				Path humidityPath2 = Paths.get("data", "icons", "humidity_unselected.png");
				Image humidityImage2 = new Image(humidityPath2.toUri().toURL().toString());
				this.humidityButton = new IconWeatherButton("Humidity", humidityImage1, humidityImage2);
				this.humidityButton.setGraphicTextGap(12);
				this.linkLayerWithButton(humidityLayer, this.humidityButton, humiditySlider.valueProperty());
				layerPane.add(this.humidityButton, 0, layerIndex);
				layerIndex++;
			}
		}
		catch (MalformedURLException e)
		{
		}
		this.setCenter(layerPane);
	}

	private void linkLayerWithButton(KMLLayer layer, ToggleButton toggleButton, DoubleProperty opacityProperty)
	{
		if (layer != null)
		{
			toggleButton.setOnAction((action) ->
			{
				Platform.runLater(() ->
				{
					double duration;
					double fromOpacity;
					double toOpacity;
					if (toggleButton.isSelected())
					{
						layer.setIsEnabled(true);
						duration = 1000;
						fromOpacity = 0;
						toOpacity = opacityProperty.getValue().doubleValue();
					}
					else
					{
						duration = 300;
						fromOpacity = opacityProperty.getValue().doubleValue();
						toOpacity = 0;
					}
					LayerFadeTransition fade = new LayerFadeTransition(this.session.getWorldWindow(),
							Duration.millis(duration), layer);
					fade.setStart(fromOpacity);
					fade.setStop(toOpacity);
					fade.setAutoReverse(true);
					fade.onFinishedProperty().addListener(new ChangeListener<Object>()
					{
						@Override
						public void changed(ObservableValue<?> observable, Object oldValue, Object newValue)
						{
							layer.setIsEnabled(toggleButton.isSelected());
						}
					});
					fade.play();
					this.session.updateWeatherAnnotation(session.getSoundMarker().getPosition());
				});
			});
			layer.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent event)
				{
					if (event.getPropertyName() == "Enabled")
					{
						toggleButton.setSelected((boolean) event.getNewValue());
					}
				}
			});
			toggleButton.setSelected(layer.isEnabled());
		}
	}

	private void linkLayerWithButton(WindLayer layer, ToggleButton toggleButton, DoubleProperty opacityProperty)
	{
		if (layer != null)
		{
			toggleButton.setOnAction((action) ->
			{
				Platform.runLater(() ->
				{
					layer.setEnabled(toggleButton.isSelected());
					this.session.updateWeatherAnnotation(session.getSoundMarker().getPosition());
				});
			});
			layer.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent event)
				{
					if (event.getPropertyName() == "Enabled")
					{
						toggleButton.setSelected((boolean) event.getNewValue());
					}
				}
			});
			toggleButton.setSelected(layer.isEnabled());
		}
	}
	
	private void linkLayerWithSlider(Layer layer, Slider slider)
	{
		slider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Platform.runLater(() ->
				{
					layer.setOpacity(newValue.doubleValue());
					session.getWorldWindow().redraw();
				});
			}
		});
	}
}
