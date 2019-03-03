package wave.infrastructure.layers;

import java.awt.Point;

import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.Logging;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class KMLLayer extends RenderableLayer
{
	protected final BooleanProperty isEnabledProperty;
	protected final DoubleProperty opacityProperty;
	
	// TODO integrate this with marker position
	protected final StringProperty valueProperty;
	
	protected DrawContext dc;

	public KMLLayer()
	{
		this.isEnabledProperty = new SimpleBooleanProperty();
		this.opacityProperty = new SimpleDoubleProperty();
		this.valueProperty = new SimpleStringProperty();
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

	public String getValue()
	{
		return this.valueProperty.getValue();
	}

	public void setValue(String value)
	{
		this.valueProperty.setValue(value);
	}

	public StringProperty valueProperty()
	{
		return this.valueProperty;
	}
	
	public void getColor()
	{
		if (this.dc == null)
		{
			return;
		}
		int color = dc.getPickColorAtPoint(new Point(100, 100));
		this.valueProperty.setValue(Integer.toString(color));
	}

    protected void doRender(DrawContext dc, Iterable<? extends Renderable> renderables)
    {
    	this.dc = dc;
        for (Renderable renderable : renderables)
        {
            try
            {
                if (renderable != null)
                {
                    renderable.render(dc);
                }
            }
            catch (Exception e)
            {
                String msg = Logging.getMessage("generic.ExceptionWhileRenderingRenderable");
                Logging.logger().log(java.util.logging.Level.SEVERE, msg, e);
            }
        }
    }
}
