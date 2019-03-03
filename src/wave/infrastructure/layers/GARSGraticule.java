package wave.infrastructure.layers;

import java.awt.Color;

import gov.nasa.worldwind.layers.GARSGraticuleLayer;

public class GARSGraticule extends GARSGraticuleLayer
{
	public GARSGraticule()
	{
		Color level0Color = new Color(240, 240, 240);
		this.setLabelColor(level0Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_0);
		this.setGraticuleLineColor(level0Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_0);

		Color level1Color = new Color(225, 225, 225);
		this.setLabelColor(level1Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_1);
		this.setGraticuleLineColor(level1Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_1);

		Color level2Color = new Color(210, 210, 210);
		this.setLabelColor(level2Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_2);
		this.setGraticuleLineColor(level2Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_2);

		Color level3Color = new Color(195, 195, 195);
		this.setLabelColor(level3Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_3);
		this.setGraticuleLineColor(level3Color, GARSGraticuleLayer.GRATICULE_GARS_LEVEL_3);

		this.set30MinuteThreshold(1200e3);
		this.set15MinuteThreshold(600e3);
		this.set5MinuteThreshold(180e3);
	}
}
