package wave.views;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import gov.nasa.worldwind.util.PerformanceStatistic;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.BorderPane;
import wave.models.WaveSession;

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

		StatusBar statusBar = new StatusBar();
		statusBar.setEventSource(session.getWorldWindow());
		this.setBottom(statusBar);

		// TODO add tab pane and add panels to it
		StatisticsPanel statisticsPanel = new StatisticsPanel(session, PerformanceStatistic.ALL_STATISTICS_SET);
		this.setLeft(statisticsPanel);
		
		MarkerPanel markerPanel = new MarkerPanel(session);
		this.setRight(markerPanel);
	}
}
