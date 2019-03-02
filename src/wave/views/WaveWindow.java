package wave.views;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import gov.nasa.worldwind.util.PerformanceStatistic;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import wave.infrastructure.WaveSession;
import wave.views.panels.LayersPanel;
import wave.views.panels.MarkerPanel;
import wave.views.panels.StatisticsPanel;
import wave.views.panels.WeatherOverlayPanel;

public class WaveWindow extends BorderPane
{
	public WaveWindow(WaveSession session)
	{
		SwingNode swingNode = new SwingNode();
		SwingUtilities.invokeLater(() ->
		{
			swingNode.setContent((JComponent) session.getWorldWindow());
		});
		this.setCenter(swingNode);

		WaveMenu menu = new WaveMenu(session);
		this.setTop(menu);

		WaveStatusBar statusBar = new WaveStatusBar();
		statusBar.setEventSource(session.getWorldWindow());
		this.setBottom(statusBar);

		TabPane tabPane = new TabPane();
		this.setLeft(tabPane);
		WeatherOverlayPanel weatherOverlayPanel = new WeatherOverlayPanel(session);
		Tab weatherOverlayTab = new Tab("Weather Layers", weatherOverlayPanel);
		tabPane.getTabs().add(weatherOverlayTab);
		
		LayersPanel layersPanel = new LayersPanel(session);
		Tab layersTab = new Tab("Layers", layersPanel);
		tabPane.getTabs().add(layersTab);
		
		StatisticsPanel statisticsPanel = new StatisticsPanel(session, PerformanceStatistic.ALL_STATISTICS_SET);
		Tab statisticsTab = new Tab("Performance", statisticsPanel);
		tabPane.getTabs().add(statisticsTab);
		
		MarkerPanel markerPanel = new MarkerPanel(session);
		this.setRight(markerPanel);
	}
}
