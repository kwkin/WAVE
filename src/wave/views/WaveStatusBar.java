package wave.views;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.util.UnitsFormat;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.infrastructure.util.UnitToString;

public class WaveStatusBar extends GridPane implements PositionListener, RenderingListener
{
	protected static String OFF_GLOBE_MESSAGE = "Off Globe";
	protected String elevationSystem = UnitsFormat.METRIC_SYSTEM;
	protected String angleFormat = Angle.ANGLE_FORMAT_DMS;
	protected WorldWindow eventSource;

	private final Label latitudeLabel = new Label("Latitude: ");
	private final Label latitudeDisplay = new Label("");
	private final Label longitudeLabel = new Label("Longitude: ");
	private final Label longitudeDisplay = new Label("");
	private final Label elevationLabel = new Label("Elevation: ");
	private final Label elevationDisplay = new Label("");
	private final Label altitudeLabel = new Label("Altitude: ");
	private final Label altitudeDisplay = new Label("");

	public WaveStatusBar()
	{
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(0, 10, 0, 10));

		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setHgrow(Priority.ALWAYS);
		labelColumn.setHalignment(HPos.RIGHT);

		ColumnConstraints displayColumn = new ColumnConstraints();
		displayColumn.setPercentWidth(12.5);
		displayColumn.setHgrow(Priority.ALWAYS);
		displayColumn.setHalignment(HPos.LEFT);

		this.getColumnConstraints().add(labelColumn);
		this.getColumnConstraints().add(displayColumn);
		this.getColumnConstraints().add(labelColumn);
		this.getColumnConstraints().add(displayColumn);
		this.getColumnConstraints().add(labelColumn);
		this.getColumnConstraints().add(displayColumn);
		this.getColumnConstraints().add(labelColumn);
		this.getColumnConstraints().add(displayColumn);

		this.add(this.latitudeLabel, 0, 0);
		this.add(this.latitudeDisplay, 1, 0);
		this.add(this.longitudeLabel, 2, 0);
		this.add(this.longitudeDisplay, 3, 0);
		this.add(this.elevationLabel, 4, 0);
		this.add(this.elevationDisplay, 5, 0);
		this.add(this.altitudeLabel, 6, 0);
		this.add(this.altitudeDisplay, 7, 0);
	}

	@Override
	public void moved(PositionEvent event)
	{
		this.handleCursorPositionChange(event);
	}

	@Override
	public void stageChanged(RenderingEvent event)
	{
		if (!event.getStage().equals(RenderingEvent.BEFORE_BUFFER_SWAP))
		{
			return;
		}
		Platform.runLater(() ->
		{
			if (this.eventSource.getView() != null && this.eventSource.getView().getEyePosition() != null)
			{
				double elevation = this.eventSource.getView().getEyePosition().getElevation();
				String displayText = UnitToString.distanceDescription(elevation, this.elevationSystem);
				this.altitudeDisplay.setText(displayText);
			}
		});
	}

	/**
	 * Sets the elevation system
	 * 
	 * @param format The unit system specified using UnitsFormat
	 */
	public void setElevationSystem(String format)
	{
		switch (format)
		{
		case UnitsFormat.IMPERIAL_SYSTEM:
		case UnitsFormat.METRIC_SYSTEM:
			this.elevationSystem = format;
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the current elevation system
	 * 
	 * @return The current elevation system
	 */
	public String getElevationSystem()
	{
		return this.elevationSystem;
	}

	/**
	 * Sets the angle format
	 * 
	 * @param format The format specified using Angle
	 */
	public void setAngleFormat(String format)
	{
		switch (format)
		{
		case Angle.ANGLE_FORMAT_DD:
		case Angle.ANGLE_FORMAT_DM:
		case Angle.ANGLE_FORMAT_DMS:
			this.angleFormat = format;
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the current Angle format
	 * 
	 * @return The current Angle format
	 */
	public String getAngleFormat()
	{
		return this.angleFormat;
	}

	/**
	 * Changes the event source used to update the position and cursor information
	 * 
	 * @param newEventSource The event source
	 */
	public void setEventSource(WorldWindow newEventSource)
	{
		if (this.eventSource != null)
		{
			this.eventSource.removePositionListener(this);
			this.eventSource.removeRenderingListener(this);
		}
		if (newEventSource != null)
		{
			newEventSource.addPositionListener(this);
			newEventSource.addRenderingListener(this);
		}
		this.eventSource = newEventSource;
	}

	protected void handleCursorPositionChange(PositionEvent event)
	{
		Position newPosition = event.getPosition();
		if (newPosition != null)
		{
			String latitudeStr = UnitToString.angleDescription(newPosition.getLatitude(), this.angleFormat);
			String longitudeStr = UnitToString.angleDescription(newPosition.getLongitude(), this.angleFormat);

			Globe globe = this.eventSource.getModel().getGlobe();
			double elevation = globe.getElevation(newPosition.getLatitude(), newPosition.getLongitude());
			String elevationStr = UnitToString.distanceDescription(elevation, this.elevationSystem);
			this.latitudeDisplay.setText(latitudeStr);
			this.longitudeDisplay.setText(longitudeStr);
			this.elevationDisplay.setText(elevationStr);
		}
		else
		{
			this.latitudeDisplay.setText(OFF_GLOBE_MESSAGE);
			this.longitudeDisplay.setText(OFF_GLOBE_MESSAGE);
			this.elevationDisplay.setText(OFF_GLOBE_MESSAGE);
		}
	}
}
