package wave.views.panels;

import gov.nasa.worldwind.geom.Position;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.infrastructure.WaveSession;
import wave.infrastructure.converters.PositionConverter;
import wave.infrastructure.converters.PositionConverterOption;
import wave.infrastructure.layers.KMLLayer;

public class MarkerPanel extends BorderPane
{
	private final ToggleButton markerToggleButton;

	private final Label latitudeLabel;
	private final Label longitudeLabel;
	private final Label elevationLabel;
	private final Label humidityLabel;
	private final TextField latitudeTextfield;
	private final TextField longitudetextfield;
	private final TextField elevationTextfield;
	private final TextField humidityTextfield;

	public MarkerPanel(WaveSession session)
	{
		ButtonBar buttons = new ButtonBar();
		this.markerToggleButton = new ToggleButton("Marker");
		this.markerToggleButton.setOnAction(event ->
		{
			System.out.println("Position: " + session.getSoundMarker().positionProperty().getValue());
		});
		buttons.getButtons().add(this.markerToggleButton);

		// TODO change to table view
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(5);
		grid.setVgap(5);
		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setPercentWidth(33.3);
		labelColumn.setHgrow(Priority.ALWAYS);
		labelColumn.setHalignment(HPos.LEFT);

		ColumnConstraints displayColumn = new ColumnConstraints();
		displayColumn.setPercentWidth(66.67);
		displayColumn.setHgrow(Priority.ALWAYS);
		displayColumn.setHalignment(HPos.LEFT);

		grid.getColumnConstraints().add(labelColumn);
		grid.getColumnConstraints().add(displayColumn);

		PositionConverter latitudeConverter = new PositionConverter(session, PositionConverterOption.LATITUDE);
		PositionConverter longitudeConverter = new PositionConverter(session, PositionConverterOption.LONGITUDE);
		PositionConverter elevationConverter = new PositionConverter(session, PositionConverterOption.ELEVATION);
		ObjectProperty<Position> soundPosition = session.getSoundMarker().positionProperty();
		this.latitudeLabel = new Label("Latitude: ");
		grid.add(this.latitudeLabel, 0, 0);
		this.latitudeTextfield = new TextField();
		this.latitudeTextfield.textProperty().bindBidirectional(soundPosition, latitudeConverter);
		grid.add(this.latitudeTextfield, 1, 0);
		
		this.longitudeLabel = new Label("Longitude: ");
		grid.add(this.longitudeLabel, 0, 1);
		this.longitudetextfield = new TextField();
		this.longitudetextfield.textProperty().bindBidirectional(soundPosition, longitudeConverter);
		grid.add(this.longitudetextfield, 1, 1);
		
		this.elevationLabel = new Label("Elevation: ");
		grid.add(this.elevationLabel, 0, 2);
		this.elevationTextfield = new TextField();
		this.elevationTextfield.textProperty().bindBidirectional(soundPosition, elevationConverter);
		grid.add(this.elevationTextfield, 1, 2);

		KMLLayer humidityLayer = session.getWeatherLayers().get(1);
		this.humidityLabel = new Label("Humidity: ");
		grid.add(this.humidityLabel, 0, 3);
		this.humidityTextfield = new TextField();
		this.humidityTextfield.textProperty().bindBidirectional(humidityLayer.valueProperty());
		grid.add(this.humidityTextfield, 1, 3);

		Button updateButton = new Button("Update");
		updateButton.setOnAction((event) ->
		{
			humidityLayer.getColor();
		});
		grid.add(updateButton, 0, 4, 2, 1);
		
		this.setTop(buttons);
		this.setCenter(grid);
	}
}
