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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

	private KMLLayer rainLayer;
	private int lastRainValue;

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
			markerToggleButton.setIconSize(48, 48);
			markerToggleButton.setSelected(session.getSoundMarkerVisibility());
			markerToggleButton.setOnAction((event) ->
			{
				boolean isSelected = markerToggleButton.isSelected();
				session.setSoundMarkerVisibility(isSelected);
			});
			buttons.getChildren().add(markerToggleButton);

			Path resetPath = Paths.get("data", "icons", "reset_unselected.png");
			Image resetImage = new Image(resetPath.toUri().toURL().toString());
			ImageView resetImageView = new ImageView(resetImage);
			resetImageView.setPreserveRatio(true);
			resetImageView.setFitWidth(48);
			resetImageView.setFitHeight(48);
			Button updateButton = new Button();
			updateButton.getStyleClass().add("icon-button");
			updateButton.setGraphic(resetImageView);
			updateButton.resize(48, 48);
			updateButton.setOnAction((event) ->
			{
				updateMarkerValues(soundPosition.get());
			});
			buttons.getChildren().add(updateButton);
		}
		catch (MalformedURLException e)
		{
		}

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

		this.latitudeLabel = new Label("Latitude: ");
		grid.add(this.latitudeLabel, 0, 0);
		this.latitudeTextfield = new TextField();
		grid.add(this.latitudeTextfield, 1, 0);
		this.latitudeUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.latitudeUnitLabel, 2, 0);

		this.longitudeLabel = new Label("Longitude: ");
		grid.add(this.longitudeLabel, 0, 1);
		this.longitudetextfield = new TextField();
		grid.add(this.longitudetextfield, 1, 1);
		this.longitudeUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.longitudeUnitLabel, 2, 1);

		this.elevationLabel = new Label("Elevation: ");
		grid.add(this.elevationLabel, 0, 2);
		this.elevationTextfield = new TextField();
		grid.add(this.elevationTextfield, 1, 2);
		this.elevationUnitLabel = new Label();
		grid.add(this.elevationUnitLabel, 2, 2);

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
		this.rainUnitLabel = new Label();
		grid.add(this.rainUnitLabel, 2, 3);

		this.windSpeedLabel = new Label("Wind Speed: ");
		grid.add(this.windSpeedLabel, 0, 4);
		this.windSpeedTextField = new TextField();
		grid.add(this.windSpeedTextField, 1, 4);
		this.windSpeedUnitLabel = new Label();
		grid.add(this.windSpeedUnitLabel, 2, 4);

		this.windDirectionLabel = new Label("Wind Direction: ");
		grid.add(this.windDirectionLabel, 0, 5);
		this.windDirectionTextField = new TextField();
		grid.add(this.windDirectionTextField, 1, 5);
		this.windDirectionUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.windDirectionUnitLabel, 2, 5);

		this.tempuratureLabel = new Label("Temp.: ");
		grid.add(this.tempuratureLabel, 0, 6);
		this.tempuratureTextfield = new TextField();
		grid.add(this.tempuratureTextfield, 1, 6);
		this.temperatureUnitLabel = new Label();
		grid.add(this.temperatureUnitLabel, 2, 6);

		this.humidityLabel = new Label("Humidity: ");
		grid.add(this.humidityLabel, 0, 7);
		this.humidityTextfield = new TextField();
		grid.add(this.humidityTextfield, 1, 7);
		this.humidityUnitLabel = new Label();
		grid.add(this.humidityUnitLabel, 2, 7);

		updateUnitLabels();
		updateMarkerValues(soundPosition.get());
		this.setCenter(grid);
		
		PreferencesLoader.preferences().lengthUnitDisplayProperty().addListener(this);
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
