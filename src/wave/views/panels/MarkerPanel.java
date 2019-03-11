package wave.views.panels;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import wave.infrastructure.layers.KMLLayer;

// TODO add precipitation, temperature, and air pressure labels.
// TODO enabled/disable marker visibility
// TODO add reset button
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

		ObjectProperty<Position> soundPosition = session.getSoundMarker().positionProperty();
		this.latitudeLabel = new Label("Latitude: ");
		grid.add(this.latitudeLabel, 0, 0);
		this.latitudeTextfield = new TextField();
		grid.add(this.latitudeTextfield, 1, 0);

		this.longitudeLabel = new Label("Longitude: ");
		grid.add(this.longitudeLabel, 0, 1);
		this.longitudetextfield = new TextField();
		grid.add(this.longitudetextfield, 1, 1);

		this.elevationLabel = new Label("Elevation: ");
		grid.add(this.elevationLabel, 0, 2);
		this.elevationTextfield = new TextField();
		grid.add(this.elevationTextfield, 1, 2);

		session.getSoundMarker().positionProperty().addListener(new ChangeListener<Position>()
		{
			@Override
			public void changed(ObservableValue<? extends Position> observable, Position oldValue, Position newValue)
			{
				Platform.runLater(() ->
				{
					double latitude = newValue.latitude.degrees;
					double longitude = newValue.longitude.degrees;
					double elevation = newValue.elevation;
					latitudeTextfield.setText(Double.toString(latitude));
					longitudetextfield.setText(Double.toString(longitude));
					elevationTextfield.setText(Double.toString(elevation));
				});
			}
		});
		this.latitudeTextfield.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				Platform.runLater(() ->
				{
					if (oldValue == newValue)
					{
						return;
					}
					double longitude = session.getSoundMarker().getPosition().longitude.degrees;
					double elevation = session.getSoundMarker().getPosition().elevation;
					double latitude = Double.valueOf(newValue);
					Position newPosition = Position.fromDegrees(latitude, longitude, elevation);
					session.getSoundMarker().setPosition(newPosition);
					session.getWorldWindow().redraw();
				});
			}
		});
		this.longitudetextfield.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				Platform.runLater(() ->
				{
					if (oldValue == newValue)
					{
						return;
					}
					double latitude = session.getSoundMarker().getPosition().latitude.degrees;
					double elevation = session.getSoundMarker().getPosition().elevation;
					double longitude = Double.valueOf(newValue);
					Position newPosition = Position.fromDegrees(latitude, longitude, elevation);
					session.getSoundMarker().setPosition(newPosition);
					session.getWorldWindow().redraw();
				});
			}
		});
		this.elevationTextfield.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				Platform.runLater(() ->
				{
					if (oldValue == newValue)
					{
						return;
					}
					double latitude = session.getSoundMarker().getPosition().latitude.degrees;
					double longitude = session.getSoundMarker().getPosition().longitude.degrees;
					double elevation = Double.valueOf(newValue);
					Position newPosition = Position.fromDegrees(latitude, longitude, elevation);
					session.getSoundMarker().setPosition(newPosition);
					session.getWorldWindow().redraw();
				});
			}
		});

		KMLLayer humidityLayer = session.getWeatherLayers().get(1);
		this.humidityLabel = new Label("Humidity: ");
		grid.add(this.humidityLabel, 0, 3);
		this.humidityTextfield = new TextField();
		this.humidityTextfield.textProperty().bindBidirectional(humidityLayer.valueProperty());
		grid.add(this.humidityTextfield, 1, 3);

		Button updateButton = new Button("Update");
		updateButton.setOnAction((event) ->
		{
			Angle latitudeAngle = soundPosition.getValue().latitude;
			Angle longitude = soundPosition.getValue().longitude;
			double elevation = soundPosition.getValue().elevation;
			humidityLayer.getLayerValue(latitudeAngle, longitude, elevation);
		});
		grid.add(updateButton, 0, 4, 2, 1);

		this.setTop(buttons);
		this.setCenter(grid);
		this.getStyleClass().add("weather-panel");
	}
}
