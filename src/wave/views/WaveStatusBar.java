package wave.views;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.infrastructure.WaveSession;
import wave.infrastructure.preferences.PreferencesLoader;

public class WaveStatusBar extends GridPane implements PositionListener, RenderingListener, ChangeListener<Object>
{
	protected static String OFF_GLOBE_MESSAGE = "Off Globe";
	protected WorldWindow eventSource;
	protected WaveSession session;
	protected Position lastPosition;

	private final Label latitudeLabel = new Label("Latitude: ");
	private final Label latitudeDisplay = new Label("");
	private final Label longitudeLabel = new Label("Longitude: ");
	private final Label longitudeDisplay = new Label("");
	private final Label elevationLabel = new Label("Elevation: ");
	private final Label elevationDisplay = new Label("");
	private final Label altitudeLabel = new Label("Altitude: ");
	private final Label altitudeDisplay = new Label("");

	public WaveStatusBar(WaveSession session)
	{
		this.session = session;

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

		PreferencesLoader.preferences().lengthUnitDisplayProperty().addListener(this);
		PreferencesLoader.preferences().angleUnitDisplayProperty().addListener(this);
	}

	@Override
	public void moved(PositionEvent event)
	{
		this.lastPosition = event.getPosition();
		this.handleCursorPositionChange();
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
				String elevationStr = PreferencesLoader.preferences().getLengthUnitDisplay().lengthDescription(elevation);
				this.altitudeDisplay.setText(elevationStr);
			}
		});
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

	protected void handleCursorPositionChange()
	{
		if (this.lastPosition != null)
		{
			String latitudeStr = PreferencesLoader.preferences().getAngleUnitDisplay()
					.angleDescription(this.lastPosition.getLatitude());
			String longitudeStr = PreferencesLoader.preferences().getAngleUnitDisplay()
					.angleDescription(this.lastPosition.getLongitude());

			Globe globe = this.eventSource.getModel().getGlobe();
			double elevation = globe.getElevation(this.lastPosition.getLatitude(), this.lastPosition.getLongitude());
			String elevationStr = PreferencesLoader.preferences().getLengthUnitDisplay().lengthDescription(elevation);
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

	@Override
	public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue)
	{
		handleCursorPositionChange();
	}
}
