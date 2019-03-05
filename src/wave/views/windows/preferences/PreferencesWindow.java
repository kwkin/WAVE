package wave.views.windows.preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import wave.infrastructure.WaveSession;

// TODO embed into primary stage (use scene to switch and animated)
public class PreferencesWindow extends Stage implements ChangeListener<String>
{
	protected final static String TITLE = "WAVE Preferences";
	protected final static int WINDOW_WIDTH = 720;
	protected final static int WINDOW_HEIGHT = 480;

	protected final WaveSession session;
	protected final SplitPane splitPane;
	protected PreferencesPanel currentPanel;

	public PreferencesWindow(WaveSession session)
	{
		this.session = session;

		BorderPane border = new BorderPane();
		border.setPadding(new Insets(10, 10, 5, 10));

		this.splitPane = new SplitPane();
		border.setCenter(this.splitPane);
		this.splitPane.setOrientation(Orientation.HORIZONTAL);
		ListView<String> list = new ListView<String>();
		list.setMinWidth(100);
		// @formatter:off
		ObservableList<String> items = FXCollections.observableArrayList(
				"Display", 
				"Audio");
		// @formatter:on
		list.setItems(items);
		this.splitPane.getItems().add(list);
		list.getSelectionModel().selectedItemProperty().addListener(this);
		list.getSelectionModel().select("Display");

		GridPane buttonPane = new GridPane();
		ColumnConstraints repeatColumn = new ColumnConstraints();
		repeatColumn.setHgrow(Priority.ALWAYS);
		repeatColumn.setHalignment(HPos.RIGHT);
		buttonPane.getColumnConstraints().add(repeatColumn);

		border.setBottom(buttonPane);
		buttonPane.add(new Separator(), 0, 0);
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setPadding(new Insets(5, 5, 5, 5));
		buttonPane.add(buttonBar, 0, 1);
		Button okButton = new Button("OK");
		okButton.setDefaultButton(true);
		buttonBar.getButtons().add(okButton);
		Button cancelButton = new Button("Cancel");
		buttonBar.getButtons().add(cancelButton);
		Button applyButton = new Button("Apply");
		buttonBar.getButtons().add(applyButton);

		Scene surveyScene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setScene(surveyScene);

		ChangeListener<Number> changeListener = new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				splitPane.setDividerPositions(0.2);
				if (isShowing())
				{
					observable.removeListener(this);
				}
			}
		};
		this.splitPane.widthProperty().addListener(changeListener);
		this.splitPane.heightProperty().addListener(changeListener);
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
	{
		
		switch(newValue)
		{
		case "Display":
			this.currentPanel = new DisplayPreferencesPanel();
			break;
		case "Audio":
			this.currentPanel = new AudioPreferencesPanel();
			break;
		default:
			break;
		}
		if (this.splitPane.getItems().size() >= 2)
		{
			this.splitPane.getItems().set(1, this.currentPanel.getNode());
		}
		else
		{
			this.splitPane.getItems().add(this.currentPanel.getNode());
		}
	}
}
