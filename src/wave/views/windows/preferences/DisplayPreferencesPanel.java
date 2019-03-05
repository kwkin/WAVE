package wave.views.windows.preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class DisplayPreferencesPanel extends BorderPane implements PreferencesPanel
{
	public DisplayPreferencesPanel()
	{
		this.setPadding(new Insets(10, 10, 10, 10));

		Label settingsLabel = new Label("Display Settings");
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

		Label systemLabel = new Label("System Display");
		gridPane.add(systemLabel, 0, 0);
		// @formatter:off
		ObservableList<String> systemOptions = FXCollections.observableArrayList(
				"Metric", 
				"Imerial");
		// @formatter:on
		ComboBox<String> systemComboBox = new ComboBox<String>(systemOptions);
		systemComboBox.setMaxWidth(Double.MAX_VALUE);
		gridPane.add(systemComboBox, 1, 0);

		Label angleLabel = new Label("Angle Display");
		gridPane.add(angleLabel, 0, 1);
		// @formatter:off
		ObservableList<String> angleOptions = FXCollections.observableArrayList(
				"DMS", 
				"DD",
				"DM");
		// @formatter:on
		ComboBox<String> angleComboBox = new ComboBox<String>(angleOptions);
		angleComboBox.setMaxWidth(Double.MAX_VALUE);
		gridPane.add(angleComboBox, 1, 1);
	}

	@Override
	public Node getNode()
	{
		return this;
	}

}