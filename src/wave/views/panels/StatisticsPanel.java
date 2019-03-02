package wave.views.panels;

import java.util.Set;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.util.PerformanceStatistic;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import wave.infrastructure.WaveSession;

public class StatisticsPanel extends BorderPane implements RenderingListener
{
	public final int DEFAULT_UPDATE_INTERVAL = 500;

	private WorldWindow eventSource;
	private int updateInterval = DEFAULT_UPDATE_INTERVAL;
	private long lastUpdate;
	private final ToggleButton toggleStreamButton;
	private final TableView<PerformanceStatistic> table;

	public StatisticsPanel(WaveSession session, Set<String> statistics)
	{
		this.setEventSource(session.getWorldWindow());
		session.getWorldWindow().setPerFrameStatisticsKeys(statistics);

		// TODO replace with icons
		this.toggleStreamButton = new ToggleButton("Stop Stream");
		this.toggleStreamButton.setPrefWidth(150);
		this.toggleStreamButton.setOnAction(event ->
		{
			this.toggleStream(this.toggleStreamButton.isSelected());
		});
		this.toggleStreamButton.setSelected(false);
		this.setTop(this.toggleStreamButton);

		this.table = new TableView<PerformanceStatistic>();
		this.table.setEditable(false);
		TableColumn<PerformanceStatistic, String> labelColumn = new TableColumn<>("Statistic");
		labelColumn.setMinWidth(200);
		labelColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<PerformanceStatistic, String>, ObservableValue<String>>()
				{
					@Override
					public ObservableValue<String> call(CellDataFeatures<PerformanceStatistic, String> param)
					{
						PerformanceStatistic stat = param.getValue();
						return Bindings.createStringBinding(() -> stat.getDisplayString());
					}
				});

		TableColumn<PerformanceStatistic, String> statColumn = new TableColumn<>("Value");
		statColumn.setMinWidth(100);
		statColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<PerformanceStatistic, String>, ObservableValue<String>>()
				{
					@Override
					public ObservableValue<String> call(CellDataFeatures<PerformanceStatistic, String> param)
					{
						PerformanceStatistic stat = param.getValue();
						return Bindings.createStringBinding(() -> stat.getValue().toString());
					}
				});

		this.table.getColumns().add(labelColumn);
		this.table.getColumns().add(statColumn);
		this.setCenter(this.table);
	}

	public void setEventSource(WorldWindow newEventSource)
	{
		if (this.eventSource != null)
		{
			this.eventSource.removeRenderingListener(this);
		}
		if (newEventSource != null)
		{
			newEventSource.addRenderingListener(this);
		}
		this.eventSource = newEventSource;
	}

	@Override
	public void stageChanged(RenderingEvent event)
	{
		if (!event.getStage().equals(RenderingEvent.AFTER_BUFFER_SWAP) || !(event.getSource() instanceof WorldWindow))
		{
			return;
		}
		long currentTime = System.currentTimeMillis();
		if (currentTime - this.lastUpdate > updateInterval)
		{
			Platform.runLater(() ->
			{
				update();
			});
			this.lastUpdate = currentTime;
		}
	}

	public void update()
	{
		ObservableList<PerformanceStatistic> data = FXCollections
				.observableArrayList(this.eventSource.getSceneController().getPerFrameStatistics());
		if (data.size() == 0)
		{
			return;
		}
		table.setItems(data);
	}
	
	public void toggleStream(boolean isEnabled)
	{
		if (this.toggleStreamButton.isSelected())
		{
			this.toggleStreamButton.setText("Resume Stream");
			this.eventSource.removeRenderingListener(this);
		}
		else
		{
			this.toggleStreamButton.setText("Stop Stream");
			this.eventSource.addRenderingListener(this);
		}
	}
}
