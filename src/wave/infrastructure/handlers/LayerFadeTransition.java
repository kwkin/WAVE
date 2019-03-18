package wave.infrastructure.handlers;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;
import wave.infrastructure.layers.KMLLayer;

public class LayerFadeTransition extends Transition
{
	protected WorldWindow window;
	
	private double start;
	private double delta;
	private ObjectProperty<Duration> durationProperty;
	private ObjectProperty<Layer> layerProperty;
	private DoubleProperty startProperty;
	private DoubleProperty stopProperty;

	public LayerFadeTransition(WorldWindow window, Duration duration, Layer layer)
	{
		this.window = window;
		this.durationProperty = new SimpleObjectProperty<Duration>();
		this.layerProperty = new SimpleObjectProperty<Layer>();
		this.startProperty = new SimpleDoubleProperty();
		this.stopProperty = new SimpleDoubleProperty();
		setDuration(duration);
		setLayer(layer);
		setCycleDuration(duration);
	}

	@Override
	protected void interpolate(double frac)
	{
		final double newOpacity = Math.max(0.0, Math.min(start + frac * delta, 1.0));
		if (newOpacity <= 0)
		{
			Layer layer = this.getLayer();
			if (layer instanceof KMLLayer)
			{
				((KMLLayer)layer).setIsEnabled(false);
			}
			else
			{
				layer.setEnabled(false);
			}
		}
		this.getLayer().setOpacity(newOpacity);
		window.redraw();
	}

	public void setDuration(Duration value)
	{
		this.durationProperty.set(value);
	}

	public Duration getDuration()
	{
		return this.durationProperty.get();
	}

	public ObjectProperty<Duration> durationProperty()
	{
		return this.durationProperty;
	}

	public void setLayer(Layer layer)
	{
		this.layerProperty.set(layer);
	}

	public Layer getLayer()
	{
		return this.layerProperty.get();
	}

	public ObjectProperty<Layer> layerProperty()
	{
		return this.layerProperty;
	}

	public void setStart(double start)
	{
		this.startProperty.set(start);
		calculateStartDelata();
	}

	public double getStart()
	{
		return this.startProperty.get();
	}

	public DoubleProperty startProperty()
	{
		return this.startProperty;
	}

	public void setStop(double stop)
	{
		this.stopProperty.set(stop);
		calculateStartDelata();
	}

	public double getStop()
	{
		return this.stopProperty.get();
	}

	public DoubleProperty stopProperty()
	{
		return this.stopProperty;
	}
	
	protected void calculateStartDelata()
	{
		double fromValue = getStart();
		double toValue = getStop();
		this.start = (!Double.isNaN(fromValue)) ? Math.max(0, Math.min(fromValue, 1)) : this.getLayer().getOpacity();
		this.delta = toValue - this.start;
		if (this.start + this.delta > 1.0)
		{
			this.delta = 1.0 - this.start;
		}
		else if (this.start + this.delta < 0.0)
		{
			this.delta = -this.start;
		}
	}
}
