package wave.infrastructure.layers;

import java.awt.Color;

import gov.nasa.worldwind.layers.LatLonGraticuleLayer;

public class LatLonGraticule extends LatLonGraticuleLayer
{
	public LatLonGraticule()
	{
		Color level0Color = new Color(240, 240, 240);
		this.setLabelColor(level0Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_0);
		this.setGraticuleLineColor(level0Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_0);

		Color level1Color = new Color(230, 230, 230);
		this.setLabelColor(level1Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_1);
		this.setGraticuleLineColor(level1Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_1);

		Color level2Color = new Color(220, 220, 220);
		this.setLabelColor(level2Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_2);
		this.setGraticuleLineColor(level2Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_2);

		Color level3Color = new Color(210, 210, 210);
		this.setLabelColor(level3Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_3);
		this.setGraticuleLineColor(level3Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_3);

		Color level4Color = new Color(200, 200, 200);
		this.setLabelColor(level4Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_4);
		this.setGraticuleLineColor(level4Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_4);

		Color level5Color = new Color(190, 190, 190);
		this.setLabelColor(level5Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_5);
		this.setGraticuleLineColor(level5Color, LatLonGraticuleLayer.GRATICULE_LATLON_LEVEL_5);
	}
}
