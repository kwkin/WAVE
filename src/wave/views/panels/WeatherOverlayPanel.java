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
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.WaveApp;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;
import wave.infrastructure.layers.KMLLayer;

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
		for (KMLLayer layer : session.getWeatherLayers())
		{
			String layerName = layer.getName();
			Label layerLabel = new Label(layerName);
			layerPane.add(layerLabel, 0, layerIndex);
			ToggleButton layerToggleButton = createLayerToggleButton(layerName);
			layerPane.add(layerToggleButton, 1, layerIndex);
			layerIndex = layerIndex + 1;
		}
		this.setCenter(layerPane);

		// TODO add icon style
		try
		{
			Path percipitationPath = Paths.get("data", "icons", "rain_light.png");
			Image percipitationImage = new Image(percipitationPath.toUri().toURL().toString());
			ImageView percipitationImageView = new ImageView(percipitationImage);
			percipitationImageView.setFitHeight(128);
			percipitationImageView.setFitWidth(128);
			ToggleButton percipitationButton = new ToggleButton("Rain");
			percipitationButton.setGraphic(percipitationImageView);
			layerPane.add(percipitationButton, 0, layerIndex, 2, 1);

			Path temperaturePath = Paths.get("data", "icons", "temperature_light.png");
			Image temperatureImage = new Image(temperaturePath.toUri().toURL().toString());
			ImageView temperatureImageView = new ImageView(temperatureImage);
			temperatureImageView.setFitHeight(128);
			temperatureImageView.setFitWidth(128);
			ToggleButton temperatureButton = new ToggleButton("Temperature");
			temperatureButton.setGraphic(temperatureImageView);
			layerPane.add(temperatureButton, 0, layerIndex + 1, 2, 1);

			Path humidityPath = Paths.get("data", "icons", "humidity_light.png");
			Image humidityImage = new Image(humidityPath.toUri().toURL().toString());
			ImageView humidityImageView = new ImageView(humidityImage);
			humidityImageView.setFitHeight(128);
			humidityImageView.setFitWidth(128);
			ToggleButton humidityButton = new ToggleButton("Humidity");
			humidityButton.setGraphic(humidityImageView);
			layerPane.add(humidityButton, 0, layerIndex + 2, 2, 1);

			Path windPath = Paths.get("data", "icons", "wind_light.png");
			Image windImage = new Image(windPath.toUri().toURL().toString());
			ImageView windImageView = new ImageView(windImage);
			windImageView.setFitHeight(128);
			windImageView.setFitWidth(128);
			ToggleButton windButton = new ToggleButton("Wind");
			windButton.setGraphic(windImageView);
			layerPane.add(windButton, 0, layerIndex + 3, 2, 1);

			Path lightningPath = Paths.get("data", "icons", "lightning_light.png");
			Image lightningImage = new Image(lightningPath.toUri().toURL().toString());
			ImageView lightningImageView = new ImageView(lightningImage);
			lightningImageView.setFitHeight(128);
			lightningImageView.setFitWidth(128);
			ToggleButton lightningButton = new ToggleButton("Lightning");
			lightningButton.setGraphic(lightningImageView);
			layerPane.add(lightningButton, 0, layerIndex + 4, 2, 1);
		}
		catch (MalformedURLException e)
		{
		}
		
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

	private ToggleButton createLayerToggleButton(String layerName)
	{
		ToggleButton toggleButton = null;
		Layer layer = this.session.getLayers().getLayerByName(layerName);
		if (layer != null)
		{
			ToggleButton layerToggleButton = new ToggleButton("Toggle");
			layerToggleButton.setOnAction((action) ->
			{
				layer.setEnabled(layerToggleButton.isSelected());
			});
			layer.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent event)
				{
					if (event.getPropertyName() == "Enabled")
					{
						layerToggleButton.setSelected((boolean) event.getNewValue());
					}
				}
			});
			layerToggleButton.setSelected(layer.isEnabled());
			toggleButton = layerToggleButton;
		}
		return toggleButton;
	}
}
