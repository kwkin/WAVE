package wave.views.panels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Files;
import java.nio.file.Path;

import gov.nasa.worldwind.layers.Layer;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
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
