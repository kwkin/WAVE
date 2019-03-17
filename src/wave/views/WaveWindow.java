package wave.views;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.util.PerformanceStatistic;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import wave.WaveApp;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.ConfirmCloseEventHandler;
import wave.views.panels.LayersPanel;
import wave.views.panels.MarkerPanel;
import wave.views.panels.StatisticsPanel;
import wave.views.panels.WeatherOverlayPanel;

public class WaveWindow extends BorderPane
{
	protected WaveSession session;
	protected BorderPane waveBorderPane;

	public WaveWindow(WaveSession session)
	{
		this.session = session;
		this.session.setWaveWindow(this);

		StackPane centerPane = new StackPane();
		this.setCenter(centerPane);
		SwingNode swingNode = new SwingNode();
		SwingUtilities.invokeLater(() ->
		{
			swingNode.setContent((JComponent) session.getWorldWindow());
		});
		StackPane.setAlignment(swingNode, Pos.CENTER);
		centerPane.getChildren().add(swingNode);

		WaveMenu menu = new WaveMenu(session);
		this.setTop(menu);

		this.waveBorderPane = new BorderPane();

		WaveStatusBar statusBar = new WaveStatusBar(session);
		statusBar.setEventSource(session.getWorldWindow());
		waveBorderPane.setBottom(statusBar);

		waveBorderPane.setPickOnBounds(false);
//		createInformationPanel();
		createInformationPanelTabs();

		MarkerPanel markerPanel = new MarkerPanel(session);
		waveBorderPane.setRight(markerPanel);
		centerPane.getChildren().add(waveBorderPane);


		for (Layer layer : session.getLayers())
		{
			if (layer instanceof CompassLayer)
			{
				// TODO bind to panel sizes
				CompassLayer compassLayer = (CompassLayer) layer;
				compassLayer.setIconScale(0.35);
				compassLayer.setIconFilePath(Wave.COMPASS_ICON.toString());
				compassLayer.setPosition(AVKey.NORTHEAST);
				Vec4 locationOffset = new Vec4(-300, 0);
				compassLayer.setLocationOffset(locationOffset);
			}
			else if (layer instanceof WorldMapLayer)
			{
				WorldMapLayer worldMapLayer = (WorldMapLayer) layer;
				worldMapLayer.setIconScale(0.35);
				worldMapLayer.setPosition(AVKey.NORTHWEST);
				Vec4 locationOffset = new Vec4(350, 0);
				worldMapLayer.setLocationOffset(locationOffset);
			}
			else if (layer instanceof ScalebarLayer)
			{
				ScalebarLayer scaleMapLayer = (ScalebarLayer) layer;
				scaleMapLayer.setPosition(AVKey.SOUTHEAST);
				Vec4 locationOffset = new Vec4(-300, 40);
				scaleMapLayer.setLocationOffset(locationOffset);
			}
		}
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
	
	protected void createInformationPanel()
	{
		// TODO toggle visibility of accordion 
		Accordion accordian = new Accordion();
		accordian.setMaxHeight(Double.MAX_VALUE);
		StackPane.setAlignment(accordian, Pos.CENTER_LEFT);
		this.waveBorderPane.setLeft(accordian);
				
		WeatherOverlayPanel weatherOverlayPanel = new WeatherOverlayPanel(session);
		ScrollPane weatherScrollPane = new ScrollPane();
		weatherScrollPane.setContent(weatherOverlayPanel);
		TitledPane weatherOverlayPane = new TitledPane("Weather Layers", weatherScrollPane);
		accordian.getPanes().add(weatherOverlayPane);
		accordian.setExpandedPane(weatherOverlayPane);
		weatherScrollPane.setStyle("-fx-background: transparent;");

		LayersPanel layersPanel = new LayersPanel(session);
		ScrollPane layersScrollPane = new ScrollPane();
		layersScrollPane.setContent(layersPanel);
		TitledPane layersPane = new TitledPane("Layers", layersScrollPane);
		accordian.getPanes().add(layersPane);
		layersScrollPane.setStyle("-fx-background: transparent;");

		StatisticsPanel statisticsPanel = new StatisticsPanel(session, PerformanceStatistic.ALL_STATISTICS_SET);
		ScrollPane statisticsScrollPane = new ScrollPane();
		statisticsScrollPane.setContent(statisticsPanel);
		TitledPane statisticsPane = new TitledPane("Performance", statisticsScrollPane);
		accordian.getPanes().add(statisticsPane);
		statisticsScrollPane.setStyle("-fx-background: transparent;");
	}
	
	protected void createInformationPanelTabs()
	{
		TitledPane titledPane = new TitledPane();
		titledPane.setMaxHeight(Double.MAX_VALUE);
		titledPane.setText("Information Panels");
		titledPane.setTextAlignment(TextAlignment.CENTER);
		this.waveBorderPane.setLeft(titledPane);
		
		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		titledPane.setContent(tabPane);
		StackPane.setAlignment(tabPane, Pos.CENTER_LEFT);
		WeatherOverlayPanel weatherOverlayPanel = new WeatherOverlayPanel(session);
		Tab weatherOverlayTab = new Tab("Weather Layers", weatherOverlayPanel);
		tabPane.getTabs().add(weatherOverlayTab);

		LayersPanel layersPanel = new LayersPanel(session);
		Tab layersTab = new Tab("Layers", layersPanel);
		tabPane.getTabs().add(layersTab);

		StatisticsPanel statisticsPanel = new StatisticsPanel(session, PerformanceStatistic.ALL_STATISTICS_SET);
		Tab statisticsTab = new Tab("Performance", statisticsPanel);
		tabPane.getTabs().add(statisticsTab);
		titledPane.getStyleClass().add("weather-panel");
	}
}
