package wave.views.panel;

import java.util.regex.Pattern;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import wave.models.WaveSession;

public class MarkerPanel extends BorderPane
{
	private final ToggleButton markerToggleButton;

	private final Label latitudeLabel;
	private final Label longitudeLabel;
	private final Label elevationLabel;
	private final TextField latitudeTextfield;
	private final TextField longitudeextfield;
	private final TextField elevationTextfield;

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
		
		String patternString = "[0-9]+[.][0-9]+";
		Pattern pattern = Pattern.compile(patternString);
		ObjectProperty<Position> soundPosition = session.getSoundMarker().positionProperty();
		this.latitudeLabel = new Label("Latitude: ");
		this.longitudeLabel = new Label("Longitude: ");
		this.elevationLabel = new Label("Elevation: ");
		this.latitudeTextfield = new TextField();
		this.latitudeTextfield.textProperty().bindBidirectional(soundPosition, new StringConverter<Position>()
		{
			@Override
			public Position fromString(String latitudeStr)
			{
				latitudeStr = pattern.matcher(latitudeStr).group();
				double latitude = Double.valueOf(latitudeStr);
				double longitude = soundPosition.getValue().longitude.degrees;
				Angle latitudeAngle = Angle.fromDegrees(latitude);
				Angle longitudeAngle = Angle.fromDegrees(longitude);
				double elevation = session.getModel().getGlobe().getElevation(latitudeAngle, longitudeAngle);
				Position position = Position.fromDegrees(latitude, longitude, elevation);
				return position;
			}

			@Override
			public String toString(Position position)
			{
				return position.latitude.toString();
			}
		});

		this.longitudeextfield = new TextField();
		this.longitudeextfield.textProperty().bindBidirectional(soundPosition, new StringConverter<Position>()
		{
			@Override
			public Position fromString(String longitudeStr)
			{
				double latitude = soundPosition.getValue().latitude.degrees;
				longitudeStr = pattern.matcher(longitudeStr).group();
				double longitude = Double.valueOf(longitudeStr);
				Angle latitudeAngle = Angle.fromDegrees(latitude);
				Angle longitudeAngle = Angle.fromDegrees(longitude);
				double elevation = session.getModel().getGlobe().getElevation(latitudeAngle, longitudeAngle);
				Position position = Position.fromDegrees(latitude, longitude, elevation);
				return position;
			}

			@Override
			public String toString(Position position)
			{
				return position.longitude.toString();
			}
		});

		this.elevationTextfield = new TextField();
		this.elevationTextfield.textProperty().bindBidirectional(soundPosition, new StringConverter<Position>()
		{
			@Override
			public Position fromString(String elevationStr)
			{
				double latitude = soundPosition.getValue().latitude.degrees;
				double longitude = soundPosition.getValue().longitude.degrees;
				double elevation = Double.valueOf(elevationStr);
				Position position = Position.fromDegrees(latitude, longitude, elevation);
				return position;
			}

			@Override
			public String toString(Position position)
			{
				return Double.toString(position.elevation);
			}
		});

		grid.add(this.latitudeLabel, 0, 0);
		grid.add(this.longitudeLabel, 0, 1);
		grid.add(this.elevationLabel, 0, 2);
		grid.add(this.latitudeTextfield, 1, 0);
		grid.add(this.longitudeextfield, 1, 1);
		grid.add(this.elevationTextfield, 1, 2);

		this.setTop(buttons);
		this.setCenter(grid);
	}
}
