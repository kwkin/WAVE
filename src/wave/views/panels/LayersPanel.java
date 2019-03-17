package wave.views.panels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.layers.Layer;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.components.IconWeatherButton;
import wave.infrastructure.WaveSession;

public class LayersPanel extends BorderPane
{
	protected WaveSession session;

	public LayersPanel(WaveSession session)
	{
		this.session = session;

		GridPane layerPane = new GridPane();
		layerPane.setPadding(new Insets(5, 5, 5, 5));
		layerPane.setHgap(5);
		layerPane.setVgap(5);

		ColumnConstraints nameColumn = new ColumnConstraints();
		nameColumn.setHgrow(Priority.ALWAYS);
		nameColumn.setHalignment(HPos.RIGHT);

		ColumnConstraints displayColumn = new ColumnConstraints();
		displayColumn.setHgrow(Priority.NEVER);
		displayColumn.setHalignment(HPos.CENTER);

		int layerIndex = 0;
		for (Layer layer : session.getLayers())
		{
			String layerName = layer.getName();
			ToggleButton layerToggleButton = createLayerToggleButton(layerName);
			layerPane.add(layerToggleButton, 0, layerIndex);
			layerIndex = layerIndex + 1;
		}
		this.setCenter(layerPane);
	}

	private ToggleButton createLayerToggleButton(String layerName)
	{
		IconWeatherButton toggleButton = null;
		Layer layer = this.session.getLayers().getLayerByName(layerName);
		if (layer != null)
		{
			try
			{
				Path unselectedPath = Paths.get("data", "icons", "invisible_unselected.png");
				Image unselectedImage = new Image(unselectedPath.toUri().toURL().toString());
				Path selectedPath = Paths.get("data", "icons", "visible_selected.png");
				Image selectedImage = new Image(selectedPath.toUri().toURL().toString());
				IconWeatherButton layerToggleButton = new IconWeatherButton(layerName, selectedImage, unselectedImage);
				layerToggleButton.setIconSize(48, 48);
				layerToggleButton.setOnAction((action) ->
				{
					layer.setEnabled(layerToggleButton.isSelected());
				});
				layer.addPropertyChangeListener(new PropertyChangeListener()
				{
					@Override
					public void propertyChange(PropertyChangeEvent event)
					{
						if (event.getPropertyName() == "Enabled")
						{
							layerToggleButton.setSelected((boolean) event.getNewValue());
						}
					}
				});
				layerToggleButton.setSelected(layer.isEnabled());
				toggleButton = layerToggleButton;
			}
			catch (MalformedURLException e)
			{
				
			}
		}
		return toggleButton;
	}
}