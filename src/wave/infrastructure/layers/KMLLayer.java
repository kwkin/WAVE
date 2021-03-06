package wave.infrastructure.layers;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractContainer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLLatLonBox;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.SurfaceImage;
import gov.nasa.worldwind.util.Logging;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import wave.infrastructure.util.PointUtil;

public class KMLLayer extends RenderableLayer
{
	protected final BooleanProperty isEnabledProperty;
	protected final DoubleProperty opacityProperty;

	protected final StringProperty valueProperty;
	protected BufferedImage bufferedImage;
	protected KMLLatLonBox sector;

	protected DrawContext dc;

	public KMLLayer()
	{
		this.isEnabledProperty = new SimpleBooleanProperty();
		this.opacityProperty = new SimpleDoubleProperty();
		this.valueProperty = new SimpleStringProperty();
		this.setIsEnabled(false);
	}

	@Override
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

	public int getLayerValue(Position position)
	{
		return this.getLayerValue(position.latitude, position.longitude, position.elevation);
	}

	public int getLayerValue(Angle latitude, Angle longitude, double elevation)
	{
		if (this.dc == null)
		{
			return -1;
		}

		if (this.bufferedImage == null)
		{
			for (Renderable render : this.getRenderables())
			{
				KMLController controller = (KMLController) render;
				KMLAbstractContainer container = (KMLAbstractContainer) controller.getKmlRoot().getFeature();
				for (KMLAbstractFeature feature : container.getFeatures())
				{
					if (feature instanceof KMLGroundOverlay)
					{
						KMLGroundOverlay groundOverlay = (KMLGroundOverlay) feature;
						SurfaceImage image = (SurfaceImage) groundOverlay.getRenderable();
						try
						{
							this.bufferedImage = ImageIO.read(Paths.get((String) image.getImageSource()).toFile());
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						this.sector = ((KMLGroundOverlay) feature).getLatLonBox();
					}
				}
			}
		}
		double outMinX = 0;
		double outMaxX = this.bufferedImage.getWidth();
		double inMinX = this.sector.getWest();
		double inMaxX = this.sector.getEast();
		int pixelX = (int) PointUtil.map(longitude.degrees, inMinX, inMaxX, outMinX, outMaxX);
		if (pixelX >= this.bufferedImage.getWidth() || pixelX < 0)
		{
			return 0;
		}

		double outMinY = 0;
		double outMaxY = this.bufferedImage.getHeight();
		double inMinY = this.sector.getSouth();
		double inMaxY = this.sector.getNorth();
		int pixelY = (int) PointUtil.map(latitude.degrees, inMaxY, inMinY, outMinY, outMaxY);
		if (pixelY >= this.bufferedImage.getHeight() || pixelY < 0)
		{
			return 0;
		}
		return this.bufferedImage.getRGB(pixelX, pixelY);
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
