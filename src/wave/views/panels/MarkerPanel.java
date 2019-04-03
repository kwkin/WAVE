package wave.views.panels;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.geom.Position;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.util.converter.DoubleStringConverter;
import wave.components.IconWeatherButton;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.MeasurementSystem;
import wave.infrastructure.preferences.Preferences;
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
	private final TextField temperatureTextfield;
	private final Label temperatureUnitLabel;
	private final Label humidityLabel;
	private final TextField humidityTextfield;
	private final Label humidityUnitLabel;
	private final Label lightningLabel;
	private final TextField lightningTextfield;
	private final Label lightningUnitLabel;

	private IconWeatherButton resetToggleButton;

	public MarkerPanel(WaveSession session)
	{
		this.session = session;
		
		MeasurementSystem system = PreferencesLoader.preferences().getLengthUnitDisplay();
		ObjectProperty<Position> soundPosition = session.getSoundMarker().positionProperty();
		HBox buttons = new HBox();
		this.setTop(buttons);
		try
		{
			Preferences pref = PreferencesLoader.preferences();
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

			Path annotationUnselectedPath = Paths.get("data", "icons", "annotation_unselected.png");
			Image annotationUnselectedImage = new Image(annotationUnselectedPath.toUri().toURL().toString());
			Path annotationSelectedPath = Paths.get("data", "icons", "annotation_selected.png");
			Image annotationSelectedImage = new Image(annotationSelectedPath.toUri().toURL().toString());
			IconWeatherButton showAnnotationButton = new IconWeatherButton(annotationSelectedImage, annotationUnselectedImage);
			showAnnotationButton.setTooltip(new Tooltip("Toggles whether the annotation will be displayed."));
			showAnnotationButton.setIconSize(48, 48);
			showAnnotationButton.setSelected(true);
			showAnnotationButton.selectedProperty().bindBidirectional(pref.showAnnotationProperty());
			buttons.getChildren().add(showAnnotationButton);
						
			Path resetUnselectedPath = Paths.get("data", "icons", "reset_unselected.png");
			Image resetUnselectedImage = new Image(resetUnselectedPath.toUri().toURL().toString());
			Path resetSelectedPath = Paths.get("data", "icons", "reset_selected.png");
			Image resetSelectedImage = new Image(resetSelectedPath.toUri().toURL().toString());
			this.resetToggleButton = new IconWeatherButton(resetSelectedImage, resetUnselectedImage);
			this.resetToggleButton.setTooltip(new Tooltip("Toggles whether the data values update when the overlays are visible."));
			this.resetToggleButton.setIconSize(48, 48);
			this.resetToggleButton.setSelected(true);
			this.resetToggleButton.selectedProperty().bindBidirectional(pref.showAllWeatherProperty());
			this.resetToggleButton.setOnAction(value -> 
			{
				session.updateWeatherAnnotation(session.getSoundMarker().getPosition());
				session.getWorldWindow().redraw();
			});
			buttons.getChildren().add(this.resetToggleButton);

			Path positionUnselectedPath = Paths.get("data", "icons", "position_unselected.png");
			Image positionUnselectedImage = new Image(positionUnselectedPath.toUri().toURL().toString());
			Path positionSelectedPath = Paths.get("data", "icons", "position_selected.png");
			Image positionSelectedImage = new Image(positionSelectedPath.toUri().toURL().toString());
			IconWeatherButton showPositionButton = new IconWeatherButton(positionSelectedImage, positionUnselectedImage);
			showPositionButton.setTooltip(new Tooltip("Toggles whether the position information will be added to the annotation."));
			showPositionButton.setIconSize(48, 48);
			showPositionButton.setSelected(true);
			showPositionButton.selectedProperty().bindBidirectional(pref.showPositionProperty());
			showPositionButton.disableProperty().bind(showAnnotationButton.selectedProperty().not());
			showPositionButton.setOnAction(value -> 
			{
				session.updateWeatherAnnotation(session.getSoundMarker().getPosition());
				session.getWorldWindow().redraw();
			});
			buttons.getChildren().add(showPositionButton);
		}
		catch (MalformedURLException e)
		{
		}

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(5);
		grid.setVgap(5);
		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setPercentWidth(32);
		labelColumn.setHgrow(Priority.ALWAYS);
		labelColumn.setHalignment(HPos.RIGHT);
		grid.getColumnConstraints().add(labelColumn);

		ColumnConstraints displayColumn = new ColumnConstraints();
		displayColumn.setPercentWidth(44);
		displayColumn.setHgrow(Priority.ALWAYS);
		displayColumn.setHalignment(HPos.LEFT);
		grid.getColumnConstraints().add(displayColumn);

		ColumnConstraints unitColumn = new ColumnConstraints();
		unitColumn.setPercentWidth(25);
		unitColumn.setHgrow(Priority.ALWAYS);
		unitColumn.setHalignment(HPos.LEFT);
		grid.getColumnConstraints().add(unitColumn);

		int rowIndex = 0;
		this.latitudeLabel = new Label("Latitude:");
		grid.add(this.latitudeLabel, 0, rowIndex);
		this.latitudeTextfield = new TextField();
		Bindings.bindBidirectional(this.latitudeTextfield.textProperty(), session.getWeatherHander().latitudeProperty(), new DoubleStringConverter());
		grid.add(this.latitudeTextfield, 1, rowIndex);
		this.latitudeUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.latitudeUnitLabel, 2, rowIndex);
		rowIndex++;
		// TODO add this stuff for the weather parameters
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

		this.longitudeLabel = new Label("Longitude:");
		grid.add(this.longitudeLabel, 0, rowIndex);
		this.longitudetextfield = new TextField("No Data");
		Bindings.bindBidirectional(this.longitudetextfield.textProperty(), session.getWeatherHander().longitudeProperty(), new DoubleStringConverter());
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

		this.elevationLabel = new Label("Elevation:");
		grid.add(this.elevationLabel, 0, rowIndex);
		this.elevationTextfield = new TextField("No Data");
		Bindings.bindBidirectional(this.elevationTextfield.textProperty(), session.getWeatherHander().elevationProperty(), new DoubleStringConverter());
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

		this.rainLabel = new Label("Rain:");
		grid.add(this.rainLabel, 0, rowIndex);
		this.rainTextfield = new TextField("No Data");
		Bindings.bindBidirectional(this.rainTextfield.textProperty(), session.getWeatherHander().rainProperty(), new DoubleStringConverter());
		grid.add(this.rainTextfield, 1, rowIndex);
		this.rainUnitLabel = new Label();
		grid.add(this.rainUnitLabel, 2, rowIndex);
		rowIndex++;

		this.windSpeedLabel = new Label("Wind Spd:");
		grid.add(this.windSpeedLabel, 0, rowIndex);
		this.windSpeedTextField = new TextField("No Data");
		Bindings.bindBidirectional(this.windSpeedTextField.textProperty(), session.getWeatherHander().windSpeedProperty(), new DoubleStringConverter());
		grid.add(this.windSpeedTextField, 1, rowIndex);
		this.windSpeedUnitLabel = new Label();
		grid.add(this.windSpeedUnitLabel, 2, rowIndex);
		rowIndex++;

		this.windDirectionLabel = new Label("Wind Dir:");
		grid.add(this.windDirectionLabel, 0, rowIndex);
		this.windDirectionTextField = new TextField("No Data");
		Bindings.bindBidirectional(this.windDirectionTextField.textProperty(), session.getWeatherHander().windDirectionProperty(), new DoubleStringConverter());
		grid.add(this.windDirectionTextField, 1, rowIndex);
		this.windDirectionUnitLabel = new Label(system.getAngleUnit());
		grid.add(this.windDirectionUnitLabel, 2, rowIndex);
		rowIndex++;

		this.tempuratureLabel = new Label("Temp.:");
		grid.add(this.tempuratureLabel, 0, rowIndex);
		this.temperatureTextfield = new TextField("No Data");
		Bindings.bindBidirectional(this.temperatureTextfield.textProperty(), session.getWeatherHander().temperatureProperty(), new DoubleStringConverter());
		grid.add(this.temperatureTextfield, 1, rowIndex);
		this.temperatureUnitLabel = new Label();
		grid.add(this.temperatureUnitLabel, 2, rowIndex);
		rowIndex++;

		this.humidityLabel = new Label("Humidity:");
		grid.add(this.humidityLabel, 0, rowIndex);
		this.humidityTextfield = new TextField("No Data");
		Bindings.bindBidirectional(this.humidityTextfield.textProperty(), session.getWeatherHander().humidityProperty(), new DoubleStringConverter());
		grid.add(this.humidityTextfield, 1, rowIndex);
		this.humidityUnitLabel = new Label();
		grid.add(this.humidityUnitLabel, 2, rowIndex);
		rowIndex++;

		this.lightningLabel = new Label("Lightning:");
		grid.add(this.lightningLabel, 0, rowIndex);
		this.lightningTextfield = new TextField("No Data");
		Bindings.bindBidirectional(this.lightningTextfield.textProperty(), session.getWeatherHander().lightningProperty(), new DoubleStringConverter());
		grid.add(this.lightningTextfield, 1, rowIndex);
		this.lightningUnitLabel = new Label("km^2/yr");
		grid.add(this.lightningUnitLabel, 2, rowIndex);
		rowIndex++;

		Button updateButton = new Button("Reset Values");
		updateButton.setMaxWidth(Double.MAX_VALUE);
		updateButton.setOnAction((event) ->
		{
			this.session.getWeatherHander().updateMarkerValues(soundPosition.get());
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
		this.session.getWeatherHander().updateMarkerValues(position);
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
		this.updateUnitLabels();
		this.updateMarkerValues(session.getSoundMarker().getPosition());
	}

	private void updateUnitLabels()
	{
		MeasurementSystem system = PreferencesLoader.preferences().getLengthUnitDisplay();
		this.elevationUnitLabel.setText(system.getLengthUnit());
		this.rainUnitLabel.setText(system.getRainUnit());
		this.windSpeedUnitLabel.setText(system.getWindSpeedUnit());
		this.temperatureUnitLabel.setText(system.getTemperatureUnit());
		this.humidityUnitLabel.setText(system.getHumidityUnit());
		this.lightningUnitLabel.setText(system.getLightningUnit());
	}
}
