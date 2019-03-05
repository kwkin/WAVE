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

public class AudioPreferencesPanel extends BorderPane implements PreferencesPanel
{
	public AudioPreferencesPanel()
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
		gridPane.add(masterVolumeLabel, 0, 1);
		Slider masterVolumeSlider = new Slider(0, 100, 100);
		gridPane.add(masterVolumeSlider, 1, 1);

		Label rainVolumeLabel = new Label("Rain Volume");
		gridPane.add(rainVolumeLabel, 0, 2);
		Slider rainVolumeSlider = new Slider(0, 100, 100);
		gridPane.add(rainVolumeSlider, 1, 2);

		Label windVolumeLabel = new Label("Wind Volume");
		gridPane.add(windVolumeLabel, 0, 3);
		Slider windVolumeSlider = new Slider(0, 100, 100);
		gridPane.add(windVolumeSlider, 1, 3);

		Label thunderVolumeLabel = new Label("Thunder Volume");
		gridPane.add(thunderVolumeLabel, 0, 4);
		Slider thunderVolumeSlider = new Slider(0, 100, 100);
		gridPane.add(thunderVolumeSlider, 1, 4);

		Label weatherModulatorLabel = new Label("Use Modulators");
		gridPane.add(weatherModulatorLabel, 0, 5);
		CheckBox weatherModulatorCheckBox = new CheckBox("");
		weatherModulatorCheckBox.setSelected(true);
		weatherModulatorCheckBox.setAlignment(Pos.CENTER_LEFT);
		gridPane.add(weatherModulatorCheckBox, 1, 5);

		// TODO add audio settings class with enum container audio listener
		Label listenerLabel = new Label("Listener");
		gridPane.add(listenerLabel, 0, 6);
		// @formatter:off
		ObservableList<String> options = FXCollections.observableArrayList(
				"Camera", 
				"Marker");
		// @formatter:on
		ComboBox<String> listenerComboBox = new ComboBox<String>(options);
		listenerComboBox.setMaxWidth(Double.MAX_VALUE);
		gridPane.add(listenerComboBox, 1, 6);
	}

	@Override
	public Node getNode()
	{
		return this;
	}

}
