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
import wave.infrastructure.handlers.WeatherConverter;
import wave.infrastructure.layers.KMLLayer;

// TODO enabled/disable marker visibility
// TODO add units to weather parameters
// TODO change position values only on textfield lose focus
public class MarkerPanel extends BorderPane
{
	private final ToggleButton markerToggleButton;

	private final Label latitudeLabel;
	private final TextField latitudeTextfield;
	private final Label longitudeLabel;
	private final TextField longitudetextfield;
	private final Label elevationLabel;
	private final TextField elevationTextfield;
	private final Label rainLabel;
	private final TextField rainTextfield;
	private final Label windSpeedLabel;
	private final TextField windSpeedTextField;
	private final Label windDirectionLabel;
	private final TextField windDirectionTextField;
	private final Label tempuratureLabel;
	private final TextField tempuratureTextfield;
	private final Label humidityLabel;
	private final TextField humidityTextfield;

	private KMLLayer rainLayer;
	private int lastRainValue;
	
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

		this.rainLayer = session.getWeatherLayers().get(0);
		session.getSoundMarker().positionProperty().addListener(new ChangeListener<Position>()
		{			
			@Override
			public void changed(ObservableValue<? extends Position> observable, Position oldValue, Position newValue)
			{
				Platform.runLater(() ->
				{
					updateWeatherValues(soundPosition.get());
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

		this.rainLabel = new Label("Rain: ");
		grid.add(this.rainLabel, 0, 3);
		this.rainTextfield = new TextField();
		grid.add(this.rainTextfield, 1, 3);

		this.windSpeedLabel = new Label("Wind Speed: ");
		grid.add(this.windSpeedLabel, 0, 4);
		this.windSpeedTextField = new TextField();
		grid.add(this.windSpeedTextField, 1, 4);

		this.windDirectionLabel = new Label("Wind Dir.: ");
		grid.add(this.windDirectionLabel, 0, 5);
		this.windDirectionTextField = new TextField();
		grid.add(this.windDirectionTextField, 1, 5);

		this.tempuratureLabel = new Label("Temp.: ");
		grid.add(this.tempuratureLabel, 0, 6);
		this.tempuratureTextfield = new TextField();
		grid.add(this.tempuratureTextfield, 1, 6);

		this.humidityLabel = new Label("Humidity: ");
		grid.add(this.humidityLabel, 0, 7);
		this.humidityTextfield = new TextField();
		grid.add(this.humidityTextfield, 1, 7);

		Button updateButton = new Button("Reset Weather");
		updateButton.setMaxWidth(Double.MAX_VALUE);
		updateButton.setOnAction((event) ->
		{
			updateWeatherValues(soundPosition.get());
		});
		grid.add(updateButton, 1, 8);

		this.setTop(buttons);
		this.setCenter(grid);
		this.getStyleClass().add("weather-panel");
	}
	
	private void updateWeatherValues(Position position)
	{
		double latitude = position.latitude.degrees;
		double longitude = position.longitude.degrees;
		double elevation = position.elevation;
		int rain = this.rainLayer.getLayerValue(position.latitude, position.longitude, elevation);
		this.latitudeTextfield.setText(Double.toString(latitude));
		this.longitudetextfield.setText(Double.toString(longitude));
		this.elevationTextfield.setText(Double.toString(elevation));
		if (rainLayer.isEnabled() && (this.lastRainValue != rain))
		{
			double mmRain = WeatherConverter.convertRainToValue(rain);
			this.lastRainValue = rain;
			this.rainTextfield.setText(Double.toString(mmRain));
		}
	}
}
