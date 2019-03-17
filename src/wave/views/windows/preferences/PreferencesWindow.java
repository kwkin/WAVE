package wave.views.windows.preferences;

import javax.xml.bind.JAXBException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.WaveApp;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.ErrorDialog;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;
import wave.infrastructure.util.WaveUtil;

public class PreferencesWindow extends BorderPane implements ChangeListener<String>
{
	protected final static String TITLE = "WAVE Preferences";

	protected final ScrollPane scrollPane;
	protected PreferencesPanel currentPanel;

	protected Preferences initialPreferences;
	protected Scene previousScene;

	public PreferencesWindow(Preferences preferences)
	{
		this.initialPreferences = WaveUtil.deepCopy(preferences);

		this.setPadding(new Insets(10, 10, 5, 10));

		ListView<String> list = new ListView<String>();
		this.setLeft(list);
		list.setMinWidth(100);
		// @formatter:off
		ObservableList<String> items = FXCollections.observableArrayList(
				"Display", 
				"Audio");
		// @formatter:on
		list.setItems(items);
		this.scrollPane = new ScrollPane();
		this.scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scrollPane.setFitToWidth(true);
		this.setCenter(this.scrollPane);
		list.getSelectionModel().selectedItemProperty().addListener(this);
		list.getSelectionModel().select("Display");

		GridPane buttonPane = new GridPane();
		ColumnConstraints repeatColumn = new ColumnConstraints();
		repeatColumn.setHgrow(Priority.ALWAYS);
		repeatColumn.setHalignment(HPos.RIGHT);
		buttonPane.getColumnConstraints().add(repeatColumn);

		this.setBottom(buttonPane);
		buttonPane.add(new Separator(), 0, 0);
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setPadding(new Insets(5, 5, 5, 5));
		buttonPane.add(buttonBar, 0, 1);
		Button okButton = new Button("OK");
		okButton.setDefaultButton(true);
		okButton.setOnAction((value) ->
		{
			writePreferences(preferences);
			WaveApp.getStage().setScene(this.previousScene);
		});
		buttonBar.getButtons().add(okButton);
		Button cancelButton = new Button("Cancel");
		buttonBar.getButtons().add(cancelButton);
		cancelButton.setOnAction((value) ->
		{
			resetPreferences();
			WaveApp.getStage().setScene(this.previousScene);
		});
		Button applyButton = new Button("Apply");
		applyButton.setOnAction((value) ->
		{
			writePreferences(preferences);
		});
		buttonBar.getButtons().add(applyButton);

		this.previousScene = WaveApp.getStage().getScene();
		
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
			StringBuilder errorMessage = new StringBuilder("Error: Unable to write preferences file. \n\n");
			errorMessage.append(e.getMessage());
			ErrorDialog.show(null, errorMessage.toString());
		}
	}

	private void resetPreferences()
	{
		Preferences preferences = PreferencesLoader.preferences();
		preferences.setPreferences(this.initialPreferences);
	}
}
