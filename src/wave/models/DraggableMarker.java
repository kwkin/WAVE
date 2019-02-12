package wave.models;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.drag.DragContext;
import gov.nasa.worldwind.drag.Draggable;
import gov.nasa.worldwind.drag.DraggableSupport;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;
import gov.nasa.worldwind.util.Logging;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DraggableMarker extends BasicMarker implements Movable, Draggable
{
	// TODO overwrite position property
	protected boolean dragEnabled = true;
	protected DraggableSupport draggableSupport = null;

	protected final ObjectProperty<Position> positionProperty;

	public DraggableMarker(Position position, MarkerAttributes attrs)
	{
		super(position, attrs);
		this.positionProperty = new SimpleObjectProperty<Position>(position);
	}

	public ObjectProperty<Position> positionProperty()
	{
		return this.positionProperty;
	}

	@Override
	public boolean isDragEnabled()
	{
		return this.dragEnabled;
	}

	@Override
	public void setDragEnabled(boolean enabled)
	{
		this.dragEnabled = enabled;
	}

	@Override
	public void drag(DragContext dragContext)
	{
		if (!this.dragEnabled)
		{
			return;
		}
		if (this.draggableSupport == null)
		{
			this.draggableSupport = new DraggableSupport(this, WorldWind.CLAMP_TO_GROUND);
		}
		this.doDrag(dragContext);
	}

	protected void doDrag(DragContext dragContext)
	{
		this.draggableSupport.dragGlobeSizeConstant(dragContext);
	}

	@Override
	public Position getReferencePosition()
	{
		return this.positionProperty().get();
	}

	@Override
	public void move(Position position)
	{
		if (position == null)
		{
			String message = Logging.getMessage("nullValue.PositionIsNull");
			Logging.logger().severe(message);
			throw new IllegalArgumentException(message);
		}

		Position referencePosition = this.getReferencePosition();
		if (referencePosition == null)
		{
			return;
		}
		this.moveTo(referencePosition.add(position));
	}

	@Override
	public void moveTo(Position position)
	{
		if (position == null)
		{
			String message = Logging.getMessage("nullValue.PositionIsNull");
			Logging.logger().severe(message);
			throw new IllegalArgumentException(message);
		}
		this.positionProperty().set(position);
		this.position = position;
	}
}
