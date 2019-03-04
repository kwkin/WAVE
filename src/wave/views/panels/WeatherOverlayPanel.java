package wave.views.panels;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.infrastructure.WaveSession;
import wave.infrastructure.layers.KMLLayer;

// TODO Change weather overlay text to icon images
public class WeatherOverlayPanel extends BorderPane
{
	public WeatherOverlayPanel(WaveSession session)
	{
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
			ToggleButton layerToggleButton = new ToggleButton("Toggle");
			layerToggleButton.selectedProperty().bindBidirectional(layer.isEnabledProperty());
			layerToggleButton.setOnAction((event) ->
			{
				layer.setEnabled(layerToggleButton.isSelected());
			});
			layerPane.add(layerLabel, 0, layerIndex);
			layerPane.add(layerToggleButton, 1, layerIndex);
			layerIndex = layerIndex + 1;
		}
		this.setCenter(layerPane);
	}
}
