package wave.views.windows.preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import wave.infrastructure.core.AudioListener;
import wave.infrastructure.preferences.Preferences;

public class AudioPreferencesPanel extends BorderPane implements PreferencesPanel
{
	private final Slider masterVolumeSlider;
	private final Slider rainVolumeSlider;
	private final Slider windVolumeSlider;
	private final Slider thunderVolumeSlider;
	private final CheckBox weatherModulatorCheckBox;
	private final ComboBox<AudioListener> listenerComboBox;

	public AudioPreferencesPanel(Preferences preferences)
	{
		this.setPadding(new Insets(10, 10, 10, 10));

		// TODO stylize this with larger labels
		Label settingsLabel = new Label("Audio Settings");
		settingsLabel.setPadding(new Insets(10, 10, 10, 10));
		this.setTop(settingsLabel);

		GridPane gridPane = new GridPane();
		this.setCenter(gridPane);
		gridPane.setMinWidth(100);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setHgrow(Priority.ALWAYS);
		labelColumn.setHalignment(HPos.RIGHT);
		labelColumn.setPercentWidth(25);

		ColumnConstraints interfaceColumn = new ColumnConstraints();
		interfaceColumn.setHgrow(Priority.ALWAYS);
		interfaceColumn.setFillWidth(true);
		interfaceColumn.setPercentWidth(25);

		gridPane.getColumnConstraints().add(labelColumn);
		gridPane.getColumnConstraints().add(interfaceColumn);
		gridPane.getColumnConstraints().add(interfaceColumn);
		gridPane.getColumnConstraints().add(interfaceColumn);

		Label masterVolumeLabel = new Label("Master Volume");
		gridPane.add(masterVolumeLabel, 0, 0);
		this.masterVolumeSlider = new Slider(0, 100, preferences.getMasterVolume());
		this.masterVolumeSlider.valueProperty().bindBidirectional(preferences.masterVolumeProperty());
		gridPane.add(this.masterVolumeSlider, 1, 0);

		TextFlow masterVolumeFlow = new TextFlow();
		Text masterVolumeText1 = new Text("The master volume control adjusts the audio level of all played audio.");
		Text masterVolumeText2 = new Text("This includes rain, wind, and thunder.");
		masterVolumeFlow.getChildren().add(masterVolumeText1);
		masterVolumeFlow.getChildren().add(masterVolumeText2);
		gridPane.add(masterVolumeFlow, 1, 1, 2, 1);

		Label rainVolumeLabel = new Label("Rain Volume");
		gridPane.add(rainVolumeLabel, 0, 2);
		this.rainVolumeSlider = new Slider(0, 100, preferences.getRainVolume());
		this.rainVolumeSlider.valueProperty().bindBidirectional(preferences.rainVolumeProperty());
		gridPane.add(this.rainVolumeSlider, 1, 2);

		Label windVolumeLabel = new Label("Wind Volume");
		gridPane.add(windVolumeLabel, 0, 3);
		this.windVolumeSlider = new Slider(0, 100, preferences.getWindVolume());
		this.windVolumeSlider.valueProperty().bindBidirectional(preferences.windVolumeProperty());
		gridPane.add(this.windVolumeSlider, 1, 3);

		Label thunderVolumeLabel = new Label("Thunder Volume");
		gridPane.add(thunderVolumeLabel, 0, 4);
		this.thunderVolumeSlider = new Slider(0, 100, preferences.getThunderVolume());
		this.thunderVolumeSlider.valueProperty().bindBidirectional(preferences.thunderVolumeProperty());
		gridPane.add(this.thunderVolumeSlider, 1, 4);

		Label weatherModulatorLabel = new Label("Use Modulators");
		gridPane.add(weatherModulatorLabel, 0, 5);
		this.weatherModulatorCheckBox = new CheckBox("");
		this.weatherModulatorCheckBox.setSelected(preferences.getEnableWeatherModulators());
		this.weatherModulatorCheckBox.setAlignment(Pos.CENTER_LEFT);
		gridPane.add(this.weatherModulatorCheckBox, 1, 5);

		TextFlow modulatorsFlow = new TextFlow();
		Text modulatorsText1 = new Text("Weather modulators change the intensity and speed of sound for all sounds.");
		Text modulatorsText2 = new Text("The intensity and speed of sound changes with humidity and temperature.");
		modulatorsFlow.getChildren().add(modulatorsText1);
		modulatorsFlow.getChildren().add(modulatorsText2);
		gridPane.add(modulatorsFlow, 1, 6, 2, 1);

		Label listenerLabel = new Label("Listener");
		gridPane.add(listenerLabel, 0, 7);
		ObservableList<AudioListener> options = FXCollections.observableArrayList(AudioListener.values());
		this.listenerComboBox = new ComboBox<AudioListener>(options);
		this.listenerComboBox.setMaxWidth(Double.MAX_VALUE);
		this.listenerComboBox.valueProperty().bindBidirectional(preferences.audioListenerProperty());
		gridPane.add(this.listenerComboBox, 1, 7);

		TextFlow listenerFlow = new TextFlow();
		Text listenerText1 = new Text("Audio can be rendered relative to various listeners.");
		Text listenerText2 = new Text(
				"The camera setting will render it relative to the view window, while the marker setting renders audio relative to the draggable marker.");
		listenerFlow.getChildren().add(listenerText1);
		listenerFlow.getChildren().add(listenerText2);
		gridPane.add(listenerFlow, 1, 8, 2, 1);
	}

	@Override
	public Node getNode()
	{
		return this;
	}

}
