package wave.views.panels;

import gov.nasa.worldwind.geom.Position;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.infrastructure.WaveSession;
import wave.infrastructure.handlers.WeatherConverter;
import wave.infrastructure.layers.KMLLayer;

// TODO enabled/disable marker visibility
// TODO add units to weather parameters
public class MarkerPanel extends BorderPane
{
	private final WaveSession session;
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
		this.session = session;
		ButtonBar buttons = new ButtonBar();
		this.markerToggleButton = new ToggleButton("Marker");
		this.markerToggleButton.setOnAction(event ->
		{

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
					updateMarkerValues(soundPosition.get());
				});
			}
		});
		this.latitudeTextfield.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				Platform.runLater(() ->
				{
					if (oldValue == newValue)
					{
						return;
					}
					updateLatitude();
				});
			}
		});
		this.latitudeTextfield.setOnKeyPressed(new EventHandler<KeyEvent>()
		{

			@Override
			public void handle(KeyEvent event)
			{
				if (event.getCode() == KeyCode.ENTER)
				{
					Platform.runLater(() ->
					{
						if (event.getCode() == KeyCode.ENTER)
						{
							updateLatitude();
						}
					});
				}
			}
		});
		this.longitudetextfield.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				Platform.runLater(() ->
				{
					if (oldValue == newValue)
					{
						return;
					}
					updateLongitude();
				});
			}
		});
		this.longitudetextfield.setOnKeyPressed(new EventHandler<KeyEvent>()
		{

			@Override
			public void handle(KeyEvent event)
			{
				Platform.runLater(() ->
				{
					if (event.getCode() == KeyCode.ENTER)
					{
						updateLongitude();
					}
				});
			}
		});
		this.elevationTextfield.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				Platform.runLater(() ->
				{
					if (oldValue == newValue)
					{
						return;
					}
					updateElevation();
				});
			}
		});
		this.elevationTextfield.setOnKeyPressed(new EventHandler<KeyEvent>()
		{

			@Override
			public void handle(KeyEvent event)
			{
				Platform.runLater(() ->
				{
					if (event.getCode() == KeyCode.ENTER)
					{
						updateElevation();
					}
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
			updateMarkerValues(soundPosition.get());
		});
		updateMarkerValues(soundPosition.get());
		grid.add(updateButton, 1, 8);

		this.setTop(buttons);
		this.setCenter(grid);
	}

	private void updateMarkerValues(Position position)
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

	private void updateLatitude()
	{
		double longitude = this.session.getSoundMarker().getPosition().longitude.degrees;
		double elevation = this.session.getSoundMarker().getPosition().elevation;
		double latitude = Double.valueOf(this.latitudeTextfield.getText());
		Position newPosition = Position.fromDegrees(latitude, longitude, elevation);
		this.session.getSoundMarker().setPosition(newPosition);
		this.session.getWorldWindow().redraw();
	}
	
	private void updateLongitude()
	{
		double latitude = this.session.getSoundMarker().getPosition().latitude.degrees;
		double elevation = this.session.getSoundMarker().getPosition().elevation;
		double longitude = Double.valueOf(this.longitudetextfield.getText());
		Position newPosition = Position.fromDegrees(latitude, longitude, elevation);
		this.session.getSoundMarker().setPosition(newPosition);
		this.session.getWorldWindow().redraw();
	}
	
	private void updateElevation()
	{
		double latitude = this.session.getSoundMarker().getPosition().latitude.degrees;
		double longitude = this.session.getSoundMarker().getPosition().longitude.degrees;
		double elevation = Double.valueOf(this.elevationTextfield.getText());
		Position newPosition = Position.fromDegrees(latitude, longitude, elevation);
		this.session.getSoundMarker().setPosition(newPosition);
		this.session.getWorldWindow().redraw();
	}
}
