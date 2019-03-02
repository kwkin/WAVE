package wave.infrastructure.layers;

import gov.nasa.worldwind.layers.RenderableLayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class KMLLayer extends RenderableLayer
{
	protected final BooleanProperty isEnabledProperty;
	protected final DoubleProperty opacityProperty;
	
	public KMLLayer()
	{
		this.isEnabledProperty = new SimpleBooleanProperty();
		this.opacityProperty = new SimpleDoubleProperty();
		this.setIsEnabled(false);
	}
	
	public boolean isEnabled()
	{
		return this.isEnabledProperty.getValue();
	}
	
	public boolean getIsEnabled()
	{
		return this.isEnabledProperty.getValue();
	}
	
	public void setIsEnabled(boolean isEnabled)
	{
		this.setEnabled(isEnabled);
		this.isEnabledProperty.setValue(isEnabled);
	}
	
	public BooleanProperty isEnabledProperty()
	{
		return this.isEnabledProperty;
	}
	
	public double getOpacity()
	{
		return this.opacityProperty.getValue();
	}
	
	public void setOpacity(double opacity)
	{
		this.opacityProperty.setValue(opacity);
	}
	
	public DoubleProperty opacityProperty()
	{
		return this.opacityProperty;
	}
}
