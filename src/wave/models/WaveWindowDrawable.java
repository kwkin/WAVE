package wave.models;

import javax.swing.event.EventListenerList;

import gov.nasa.worldwind.WorldWindowGLAutoDrawable;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import javafx.application.Platform;

public class WaveWindowDrawable extends WorldWindowGLAutoDrawable
{
	private final EventListenerList eventListeners = new EventListenerList();

	@Override
	public void addPositionListener(PositionListener listener)
	{
		this.eventListeners.add(PositionListener.class, listener);
	}

	@Override
	public void removePositionListener(PositionListener listener)
	{
		this.eventListeners.remove(PositionListener.class, listener);
	}

	@Override
	protected void callPositionListeners(final PositionEvent event)
	{
		Platform.runLater(() ->
		{
			for (PositionListener listener : eventListeners.getListeners(PositionListener.class))
			{
				listener.moved(event);
			}
		});
	}

	@Override
	public void addSelectListener(SelectListener listener)
	{
		this.eventListeners.add(SelectListener.class, listener);
	}

	@Override
	public void removeSelectListener(SelectListener listener)
	{
		this.eventListeners.remove(SelectListener.class, listener);
	}

	@Override
	protected void callSelectListeners(final SelectEvent event)
	{
		Platform.runLater(() ->
		{
			for (SelectListener listener : eventListeners.getListeners(SelectListener.class))
			{
				listener.selected(event);
			}
		});
	}

	@Override
	public void addRenderingExceptionListener(RenderingExceptionListener listener)
	{
		this.eventListeners.add(RenderingExceptionListener.class, listener);
	}

	@Override
	public void removeRenderingExceptionListener(RenderingExceptionListener listener)
	{
		this.eventListeners.remove(RenderingExceptionListener.class, listener);
	}

	@Override
	protected void callRenderingExceptionListeners(final Throwable exception)
	{
		Platform.runLater(() ->
		{
			for (RenderingExceptionListener listener : eventListeners.getListeners(RenderingExceptionListener.class))
			{
				listener.exceptionThrown(exception);
			}
		});
	}
}
