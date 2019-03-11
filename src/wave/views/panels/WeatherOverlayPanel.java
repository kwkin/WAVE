package wave.views.panels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.layers.Layer;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.WaveApp;
import wave.components.IconToggleButton;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;

// TODO Change weather overlay text to icon images
public class WeatherOverlayPanel extends BorderPane
{
	protected WaveSession session;

	public WeatherOverlayPanel(WaveSession session)
	{
		this.session = session;

		GridPane layerPane = new GridPane();
		layerPane.setPadding(new Insets(5, 5, 5, 5));
		layerPane.setHgap(5);
		layerPane.setVgap(5);

		ColumnConstraints nameColumn = new ColumnConstraints();
		nameColumn.setHgrow(Priority.ALWAYS);
		nameColumn.setHalignment(HPos.RIGHT);

		ColumnConstraints displayColumn = new ColumnConstraints();
		displayColumn.setHgrow(Priority.NEVER);
		displayColumn.setHalignment(HPos.CENTER);
		
		int layerIndex = 0;

		// TODO add icon style
		try
		{
			Path percipitationPath1 = Paths.get("data", "icons", "rain_selected.png");
			Image percipitationImage1 = new Image(percipitationPath1.toUri().toURL().toString());
			Path percipitationPath2 = Paths.get("data", "icons", "rain_unselected.png");
			Image percipitationImage2 = new Image(percipitationPath2.toUri().toURL().toString());
			IconToggleButton percipitationButton = new IconToggleButton("Rain", percipitationImage1, percipitationImage2);
			linkLayerWithButton(session.getWeatherLayers().get(0), percipitationButton);
			layerPane.add(percipitationButton, 0, layerIndex, 2, 1);
			layerIndex++;
			
			Path windPath1 = Paths.get("data", "icons", "wind_selected.png");
			Image windImage1 = new Image(windPath1.toUri().toURL().toString());
			Path windPath2 = Paths.get("data", "icons", "wind_unselected.png");
			Image windImage2 = new Image(windPath2.toUri().toURL().toString());
			IconToggleButton windButton = new IconToggleButton("Wind", windImage1, windImage2);
			linkLayerWithButton(session.getWeatherLayers().get(1), windButton);
			layerPane.add(windButton, 0, layerIndex, 2, 1);
			layerIndex++;
			
			Path lightningPath1 = Paths.get("data", "icons", "lightning_selected.png");
			Image lightningImage1 = new Image(lightningPath1.toUri().toURL().toString());
			Path lightningPath2 = Paths.get("data", "icons", "lightning_unselected.png");
			Image lightningImage2 = new Image(lightningPath2.toUri().toURL().toString());
			IconToggleButton lightningButton = new IconToggleButton("Lightning", lightningImage1, lightningImage2);
			linkLayerWithButton(session.getWeatherLayers().get(2), lightningButton);
			layerPane.add(lightningButton, 0, layerIndex, 2, 1);
			layerIndex++;
			
			Path temperaturePath1 = Paths.get("data", "icons", "temperature_selected.png");
			Image temperatureImage1 = new Image(temperaturePath1.toUri().toURL().toString());
			Path temperaturePath2 = Paths.get("data", "icons", "temperature_unselected.png");
			Image temperatureImage2 = new Image(temperaturePath2.toUri().toURL().toString());
			IconToggleButton temperatureButton = new IconToggleButton("Temperature", temperatureImage1, temperatureImage2);
			layerPane.add(temperatureButton, 0, layerIndex, 2, 1);
			layerIndex++;
			
			Path humidityPath1 = Paths.get("data", "icons", "humidity_selected.png");
			Image humidityImage1 = new Image(humidityPath1.toUri().toURL().toString());
			Path humidityPath2 = Paths.get("data", "icons", "humidity_unselected.png");
			Image humidityImage2 = new Image(humidityPath2.toUri().toURL().toString());
			IconToggleButton humidityButton = new IconToggleButton("Humidity", humidityImage1, humidityImage2);
			layerPane.add(humidityButton, 0, layerIndex, 2, 1);
			layerIndex++;
		}
		catch (MalformedURLException e)
		{
		}
		this.setCenter(layerPane);
		
		Button resetThemeButton = new Button("Reset");
		resetThemeButton.setOnAction((event) ->
		{
			Scene scene = WaveApp.getStage().getScene();
			Path stylesheet = Wave.WAVE_CSS_FILE;
			if (Files.exists(stylesheet))
			{
				scene.getStylesheets().set(0, "file:///" + stylesheet.toAbsolutePath().toString().replace("\\", "/"));
			}
			else
			{
			}
		});
		this.setBottom(resetThemeButton);
	}

	private void linkLayerWithButton(Layer layer, ToggleButton toggleButton)
	{
		if (layer != null)
		{
			toggleButton.setOnAction((action) ->
			{
				layer.setEnabled(toggleButton.isSelected());
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
}
