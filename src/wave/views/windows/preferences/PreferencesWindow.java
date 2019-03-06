package wave.views.windows.preferences;

import java.io.IOException;

import javax.xml.bind.JAXBException;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.FXThemeLoader;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;
import wave.infrastructure.util.WaveUtil;

// TODO embed into primary stage (use scene to switch and animated)
public class PreferencesWindow extends Stage implements ChangeListener<String>
{
	protected final static String TITLE = "WAVE Preferences";
	protected final static int WINDOW_WIDTH = 720;
	protected final static int WINDOW_HEIGHT = 480;

	protected final SplitPane splitPane;
	protected final ScrollPane scrollPane;
	protected PreferencesPanel currentPanel;

	protected Preferences initialPreferences;

	public PreferencesWindow(Preferences preferences)
	{
		this.initialPreferences = WaveUtil.deepCopy(preferences);
		this.setTitle(PreferencesWindow.TITLE);
		try
		{
			FXThemeLoader.setIcon(this, Wave.MAIN_ICON);
		}
		catch (IOException e)
		{

		}

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
		this.scrollPane = new ScrollPane();
		this.scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scrollPane.setFitToWidth(true);
		this.splitPane.getItems().add(this.scrollPane);
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
		okButton.setOnAction((value) ->
		{
			writePreferences(preferences);
			closeWindow();
		});
		buttonBar.getButtons().add(okButton);
		Button cancelButton = new Button("Cancel");
		buttonBar.getButtons().add(cancelButton);
		cancelButton.setOnAction((value) ->
		{
			resetPreferences();
			closeWindow();
		});
		Button applyButton = new Button("Apply");
		applyButton.setOnAction((value) ->
		{
			writePreferences(preferences);
		});
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
		Preferences currentPreferences = PreferencesLoader.preferences();
		switch (newValue)
		{
		case "Display":
			this.currentPanel = new DisplayPreferencesPanel(currentPreferences);
			break;
		case "Audio":
			this.currentPanel = new AudioPreferencesPanel(currentPreferences);
			break;
		default:
			break;
		}
		this.scrollPane.setContent(this.currentPanel.getNode());
	}

	private void writePreferences(Preferences preferences)
	{
		try
		{
			preferences.writePreferences(Wave.WAVE_CONFIG_FILE);
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
	}

	private void resetPreferences()
	{
		Preferences preferences = PreferencesLoader.preferences();
		preferences.setPreferences(this.initialPreferences);
	}

	private void closeWindow()
	{
		this.close();
	}
}
