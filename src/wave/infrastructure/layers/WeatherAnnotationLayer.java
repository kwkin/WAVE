package wave.infrastructure.layers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.infrastructure.models.DraggableMarker;

public class WeatherAnnotationLayer extends AnnotationLayer
{
	public WeatherAnnotationLayer(DraggableMarker marker)
	{
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setCornerRadius(10);
		defaultAttributes.setInsets(new Insets(8, 8, 8, 8));
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, .5f));
		defaultAttributes.setTextColor(Color.WHITE);
		defaultAttributes.setDrawOffset(new Point(25, 25));
		defaultAttributes.setDistanceMinScale(.5);
		defaultAttributes.setDistanceMaxScale(2);
		defaultAttributes.setDistanceMinOpacity(.5);
		defaultAttributes.setLeaderGapWidth(14);
		defaultAttributes.setDrawOffset(new Point(20, 40));

		GlobeAnnotation ga = new GlobeAnnotation("test", marker.getPosition(), new Font("Segoi-UI", Font.BOLD, 20));
		ga.getAttributes().setDefaults(defaultAttributes);
		ga.getAttributes().setImageOpacity(.6);
		ga.getAttributes().setImageScale(.7);
		ga.getAttributes().setImageOffset(new Point(1, 1));
		ga.getAttributes().setInsets(new Insets(6, 28, 6, 6));
		this.addAnnotation(ga);

		marker.positionProperty().addListener(new ChangeListener<Position>()
		{
			@Override
			public void changed(ObservableValue<? extends Position> observable, Position oldValue, Position newValue)
			{
				if (newValue != oldValue)
				{
					ga.setPosition(newValue);
				}
			}
		});
	}
}
