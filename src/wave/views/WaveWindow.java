package wave.views;

import java.lang.invoke.MethodHandle;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import gov.nasa.worldwind.util.PerformanceStatistic;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import wave.WaveApp;
import wave.infrastructure.WaveSession;
import wave.infrastructure.handlers.ConfirmCloseEventHandler;
import wave.views.panels.LayersPanel;
import wave.views.panels.MarkerPanel;
import wave.views.panels.StatisticsPanel;
import wave.views.panels.WeatherOverlayPanel;

// TODO fix runtime exception when moving window to another desktop with a different scaling
// TODO embed fx panels into the wave window
public class WaveWindow extends BorderPane
{
	protected WaveSession session;

	public WaveWindow(WaveSession session)
	{
		this.session = session;
		this.session.setWaveWindow(this);

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

	public void setIsTakingSurvey(boolean isTakingSurvey)
	{
		if (isTakingSurvey)
		{
			ConfirmCloseEventHandler closeHandler = new ConfirmCloseEventHandler(WaveApp.getStage());
			closeHandler.setDialogText("You are currently taking a survey. Are you sure you want to exit?");
			closeHandler.setConfirmText("Exit");
			closeHandler.setCancelText("Continue Survey");
			closeHandler.shutdownOnClose(true);
			WaveApp.getStage().setOnCloseRequest(closeHandler);
		}
		else
		{
			WaveApp.getStage().setOnCloseRequest(event ->
			{
				this.session.shutdown();
				Platform.exit();
			});
		}
	}
}
