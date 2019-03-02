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
import gov.nasa.worldwind.util.WWMath;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class StatusBar extends GridPane implements PositionListener, RenderingListener
{
	private static String OFF_GLOBE_MESSAGE = "Off Globe";
	private WorldWindow eventSource;
	private String elevationSystem = UnitsFormat.METRIC_SYSTEM;
	private String angleFormat = Angle.ANGLE_FORMAT_DMS;

	private final Label latitudeLabel = new Label("Latitude: ");
	private final Label latitudeDisplay = new Label("");
	private final Label longitudeLabel = new Label("Longitude: ");
	private final Label longitudeDisplay = new Label("");
	private final Label elevationLabel = new Label("Elevation: ");
	private final Label elevationDisplay = new Label("");
	private final Label altitudeLabel = new Label("Altitude: ");
	private final Label altitudeDisplay = new Label("");

	public StatusBar()
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
				this.altitudeDisplay.setText(this.makeElevationDescription(elevation));
			}
		});
	}

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

	public String getElevation()
	{
		return this.elevationSystem;
	}

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

	public String getAngleFormat()
	{
		return this.angleFormat;
	}

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
			String latitudeStr = makeAngleDescription(newPosition.getLatitude());
			String longitudeStr = makeAngleDescription(newPosition.getLongitude());
			Globe globe = this.eventSource.getModel().getGlobe();
			double elevation = globe.getElevation(newPosition.getLatitude(), newPosition.getLongitude());
			String elevationStr = makeElevationDescription(elevation);
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

	protected String makeAngleDescription(Angle angle)
	{
		String string = "";
		switch (this.angleFormat)
		{
		case Angle.ANGLE_FORMAT_DD:
			string = String.format("%7.4f\u00B0", angle.degrees);
			break;
		case Angle.ANGLE_FORMAT_DM:
			double[] degreeMinute = angle.toDMS();
			double minute = degreeMinute[1] + (degreeMinute[2] * 1 / 60);
			string = String.format("%3.0f\u00B0 %6.4f\u00B0", degreeMinute[0], minute);
			break;
		case Angle.ANGLE_FORMAT_DMS:
			string = angle.toDMSString();
			break;
		default:
			string = angle.toDMSString();
			break;
		}
		return string;
	}

	protected String makeElevationDescription(double meters)
	{
		String string;
		if (UnitsFormat.METRIC_SYSTEM.equals(this.elevationSystem))
		{
			if (Math.abs(meters) > 1000)
			{
				double km = meters / 1e3;
				string = String.format("%7.4f km", km);
			}
			else
			{
				string = String.format("%7.4f m", meters);
			}
		}
		else
		{
			int miles = (int) Math.round(WWMath.convertMetersToMiles(meters));
			if (Math.abs(miles) >= 1)
			{
				string = String.format("%7d mi", miles);
			}
			else
			{
				int feet = (int) Math.round(WWMath.convertMetersToFeet(meters));
				string = String.format("%7d ft", feet);
			}
		}
		return string;
	}
}
