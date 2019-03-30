package wave.views.panels;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.geom.Position;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import wave.components.IconWeatherButton;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.MeasurementSystem;
import wave.infrastructure.handlers.WeatherConverter;
import wave.infrastructure.layers.KMLLayer;
import wave.infrastructure.layers.WindLayer;
import wave.infrastructure.preferences.PreferencesLoader;

public class MarkerPanel extends BorderPane implements ChangeListener<Object>
{	
	private final WaveSession session;

	private final Label latitudeLabel;
	private final TextField latitudeTextfield;
	private final Label latitudeUnitLabel;
	private final Label longitudeLabel;
	private final TextField longitudetextfield;
	private final Label longitudeUnitLabel;
	private final Label elevationLabel;
	private final TextField elevationTextfield;
	private final Label elevationUnitLabel;
	private final Label rainLabel;
	private final TextField rainTextfield;
	private final Label rainUnitLabel;
	private final Label windSpeedLabel;
	private final TextField windSpeedTextField;
	private final Label windSpeedUnitLabel;
	private final Label windDirectionLabel;
	private final TextField windDirectionTextField;
	private final Label windDirectionUnitLabel;
	private final Label tempuratureLabel;
	private final TextField tempuratureTextfield;
	private final Label temperatureUnitLabel;
	private final Label humidityLabel;
	private final TextField humidityTextfield;
	private final Label humidityUnitLabel;

	private IconWeatherButton resetToggleButton;
	
	private KMLLayer rainLayer;
	private int lastRainValue;
	private KMLLayer humidityLayer;
	private int lastHumidityValue;
	private KMLLayer temperatureLayer;
	private int lastTemperatureValue;
	private WindLayer windLayer;

	public MarkerPanel(WaveSession session)
	{
		this.session = session;
		
		MeasurementSystem system = PreferencesLoader.preferences().getLengthUnitDisplay();
		ObjectProperty<Position> soundPosition = session.getSoundMarker().positionProperty();
		HBox buttons = new HBox();
		this.setTop(buttons);
		try
		{
			Path unselectedPath = Paths.get("data", "icons", "marker_unselected.png");
			Image unselectedImage = new Image(unselectedPath.toUri().toURL().toString());
			Path selectedPath = Paths.get("data", "icons", "marker_selected.png");
			Image selectedImage = new Image(selectedPath.toUri().toURL().toString());
			IconWeatherButton markerToggleButton = new IconWeatherButton(selectedImage, unselectedImage);
			markerToggleButton.setTooltip(new Tooltip("Toggles the visibility of the marker."));
			markerToggleButton.setIconSize(48, 48);
			markerToggleButton.setSelected(session.getSoundMarkerVisibility());
			markerToggleButton.setOnAction((event) ->
			{
				boolean isSelected = markerToggleButton.isSelected();
				session.setSoundMarkerVisibility(isSelected);
			});
			buttons.getChildren().add(markerToggleButton);
			
			Path resetUnselectedPath = Paths.get("data", "icons", "reset_unselected.png");
			Image resetUnselectedImage = new Image(resetUnselectedPath.toUri().toURL().toString());
			Path resetSelectedPath = Paths.get("data", "icons", "reset_selected.png");
			Image resetSelectedImage = new Image(resetSelectedPath.toUri().toURL().toString());
			this.resetToggleButton = new IconWeatherButton(resetSelectedImage, resetUnselectedImage);
			this.resetToggleButton.setTooltip(new Tooltip("Toggles whether the data values update when the overlays are visible."));
			this.resetToggleButton.setIconSize(48, 48);
			this.resetToggleButton.setSelected(true);
			buttons.getChildren().add(this.resetToggleButton);
		}
		catch (MalformedURLException e)
		{
		}
		this.rainLayer = session.getRainLayer();
		this.humidityLayer = session.getHumidityLayer();
		this.temperatureLayer = session.getTemperatureLayer();
		this.windLayer = session.getWindLayer();

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(5);
		grid.setVgap(5);
		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setPercentWidth(45);
		labelColumn.setHgrow(Priority.ALWAYS);
		labelColumn.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().add(labelColumn);

		ColumnConstraints displayColumn = new ColumnConstraints();
		displayColumn.setPercentWidth(40);
		displayColumn.setHgrow(Priority.ALWAYS);
		displayColumn.setHalignment(HPos.LEFT);
		grid.getColumnConstraints().add(displayColumn);

		ColumnConstraints unitColumn = new ColumnConstraints();
		unitColumn.setPercentWidth(15);
		unitColumn.setHgrow(Priority.ALWAYS);
		unitColumn.setHalignment(HPos.LEFT);
		grid.getColumnConstraints().add(unitColumn);

		int rowIndex = 0;
		this.latitudeLabel = new Label("Latitude: ");
		grid.add(this.latitudeLabel, 0, rowIndex);
		this.latitudeTextfield = new TextField();
		grid.add(this.latitudeTextfield, 1, rowIndex);
		this.latitudeUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.latitudeUnitLabel, 2, rowIndex);
		rowIndex++;
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

		this.longitudeLabel = new Label("Longitude: ");
		grid.add(this.longitudeLabel, 0, rowIndex);
		this.longitudetextfield = new TextField();
		grid.add(this.longitudetextfield, 1, rowIndex);
		this.longitudeUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.longitudeUnitLabel, 2, rowIndex);
		rowIndex++;
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

		this.elevationLabel = new Label("Elevation: ");
		grid.add(this.elevationLabel, 0, rowIndex);
		this.elevationTextfield = new TextField();
		grid.add(this.elevationTextfield, 1, rowIndex);
		this.elevationUnitLabel = new Label();
		grid.add(this.elevationUnitLabel, 2, rowIndex);
		rowIndex++;
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
		grid.add(this.rainLabel, 0, rowIndex);
		this.rainTextfield = new TextField();
		grid.add(this.rainTextfield, 1, rowIndex);
		this.rainUnitLabel = new Label();
		grid.add(this.rainUnitLabel, 2, rowIndex);
		rowIndex++;

		this.windSpeedLabel = new Label("Wind Speed: ");
		grid.add(this.windSpeedLabel, 0, rowIndex);
		this.windSpeedTextField = new TextField();
		grid.add(this.windSpeedTextField, 1, rowIndex);
		this.windSpeedUnitLabel = new Label();
		grid.add(this.windSpeedUnitLabel, 2, rowIndex);
		rowIndex++;

		this.windDirectionLabel = new Label("Wind Direction: ");
		grid.add(this.windDirectionLabel, 0, rowIndex);
		this.windDirectionTextField = new TextField();
		grid.add(this.windDirectionTextField, 1, rowIndex);
		this.windDirectionUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.windDirectionUnitLabel, 2, rowIndex);
		rowIndex++;

		this.tempuratureLabel = new Label("Temp.: ");
		grid.add(this.tempuratureLabel, 0, rowIndex);
		this.tempuratureTextfield = new TextField();
		grid.add(this.tempuratureTextfield, 1, rowIndex);
		this.temperatureUnitLabel = new Label();
		grid.add(this.temperatureUnitLabel, 2, rowIndex);
		rowIndex++;

		this.humidityLabel = new Label("Humidity: ");
		grid.add(this.humidityLabel, 0, rowIndex);
		this.humidityTextfield = new TextField();
		grid.add(this.humidityTextfield, 1, rowIndex);
		this.humidityUnitLabel = new Label();
		grid.add(this.humidityUnitLabel, 2, rowIndex);
		rowIndex++;

		Button updateButton = new Button("Reset Values");
		updateButton.setMaxWidth(Double.MAX_VALUE);
		updateButton.setOnAction((event) ->
		{
			updateMarkerValues(soundPosition.get());
		});
		grid.add(updateButton, 1, rowIndex, 2, 1);
		rowIndex++;
		
		updateUnitLabels();
		updateMarkerValues(soundPosition.get());
		this.setCenter(grid);

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
		
		PreferencesLoader.preferences().lengthUnitDisplayProperty().addListener(this);
	}

	private void updateMarkerValues(Position position)
	{
		double latitude = position.latitude.degrees;
		double longitude = position.longitude.degrees;
		double elevation = position.elevation;
		this.latitudeTextfield.setText(Double.toString(latitude));
		this.longitudetextfield.setText(Double.toString(longitude));
		this.elevationTextfield.setText(Double.toString(elevation));

		if (this.rainLayer != null)
		{
			if (this.rainLayer.isEnabled() || this.resetToggleButton.isSelected())
			{
				int rain = this.rainLayer.getLayerValue(position.latitude, position.longitude, elevation);
				if (this.lastRainValue != rain)
				{
					double mmRain = WeatherConverter.convertRainToValue(rain);
					this.lastRainValue = rain;
					this.rainTextfield.setText(Double.toString(mmRain));
				}
			}
		}
		if (this.windLayer != null)
		{
			if (this.windLayer.isEnabled() || this.resetToggleButton.isSelected())
			{
				this.windLayer.setNearestMarker(position);
				double direction = this.windLayer.getDirection(position);
				double speed = this.windLayer.getSpeed();
				
				this.windDirectionTextField.setText(Double.toString(direction));
				this.windSpeedTextField.setText(Double.toString(speed));
			}
		}
		if (this.humidityLayer != null)
		{
			if (this.humidityLayer.isEnabled() || this.resetToggleButton.isSelected())
			{
				int humidity = this.humidityLayer.getLayerValue(position.latitude, position.longitude, elevation);
				if (this.lastHumidityValue != humidity)
				{
					double temp = WeatherConverter.convertHumidityValue(humidity);
					this.lastHumidityValue = humidity;
					if (temp == -1)
					{
						this.humidityTextfield.setText("No Data");
					}
					else
					{
						this.humidityTextfield.setText(Double.toString(temp));
					}
				}
			}
		}
		if (this.temperatureLayer != null)
		{
			if (this.temperatureLayer.isEnabled() || this.resetToggleButton.isSelected())
			{
				int temperature = this.temperatureLayer.getLayerValue(position.latitude, position.longitude, elevation);
				if (this.lastTemperatureValue != temperature)
				{
					double temp = WeatherConverter.convertTempToValue(temperature);
					this.lastTemperatureValue = temperature;
					if (temp == -1)
					{
						this.tempuratureTextfield.setText("No Data");
					}
					else
					{
						this.tempuratureTextfield.setText(Double.toString(temp));
					}
				}
			}
		}
	}
	
	private void forceUpdateMarkerValues(Position position)
	{
		double latitude = position.latitude.degrees;
		double longitude = position.longitude.degrees;
		double elevation = position.elevation;
		int rain = this.rainLayer.getLayerValue(position.latitude, position.longitude, elevation);
		this.latitudeTextfield.setText(Double.toString(latitude));
		this.longitudetextfield.setText(Double.toString(longitude));
		this.elevationTextfield.setText(Double.toString(elevation));
		double mmRain = WeatherConverter.convertRainToValue(rain);
		this.lastRainValue = rain;
		this.rainTextfield.setText(Double.toString(mmRain));
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

	@Override
	public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue)
	{
		updateUnitLabels();
		forceUpdateMarkerValues(session.getSoundMarker().getPosition());
	}

	private void updateUnitLabels()
	{
		MeasurementSystem system = PreferencesLoader.preferences().getLengthUnitDisplay();
		this.elevationUnitLabel.setText(system.getLengthUnit());
		this.rainUnitLabel.setText(system.getRainUnit());
		this.windSpeedUnitLabel.setText(system.getWindSpeedUnit());
		this.temperatureUnitLabel.setText(system.getTemperatureUnit());
		this.humidityUnitLabel.setText(system.getHumidityUnit());
	}
}
