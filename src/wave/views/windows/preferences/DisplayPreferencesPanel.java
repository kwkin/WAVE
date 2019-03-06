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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import wave.infrastructure.core.AngleFormat;
import wave.infrastructure.core.MeasurementSystem;
import wave.infrastructure.preferences.Preferences;

public class DisplayPreferencesPanel extends BorderPane implements PreferencesPanel
{
	private final ComboBox<MeasurementSystem> systemComboBox;
	private final ComboBox<AngleFormat> angleComboBox;
	private final CheckBox enablePerformancePanel;
	private final CheckBox enableLayerPanel;
	private final CheckBox enableNetwork;
	
	public DisplayPreferencesPanel(Preferences preferences)
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
		ObservableList<MeasurementSystem> systemOptions = FXCollections.observableArrayList(MeasurementSystem.values());
		this.systemComboBox = new ComboBox<MeasurementSystem>(systemOptions);
		this.systemComboBox.valueProperty().bindBidirectional(preferences.lengthUnitDisplayProperty());
		this.systemComboBox.setMaxWidth(Double.MAX_VALUE);
		gridPane.add(this.systemComboBox, 1, 0);

		TextFlow systemTextFlow = new TextFlow();
		Text systemText1 = new Text(
				"System display will change the the measurement system used when displaying altitude, elevation, distance, etc.");
		Text systemText2 = new Text(
				"The metric system will use meters and kilometers, while the Imerial system will use feet and miles.");
		systemTextFlow.getChildren().add(systemText1);
		systemTextFlow.getChildren().add(systemText2);
		gridPane.add(systemTextFlow, 1, 1, 2, 1);

		Label angleLabel = new Label("Angle Display");
		gridPane.add(angleLabel, 0, 2);
		ObservableList<AngleFormat> angleOptions = FXCollections.observableArrayList(AngleFormat.values());
		this.angleComboBox = new ComboBox<AngleFormat>(angleOptions);
		this.angleComboBox.valueProperty().bindBidirectional(preferences.angleUnitDisplayProperty());
		this.angleComboBox.setMaxWidth(Double.MAX_VALUE);
		gridPane.add(this.angleComboBox, 1, 2);

		TextFlow angleTextFlow = new TextFlow();
		Text angleText1 = new Text(
				"Angle format will change the display format used when representing latitude and longitude.");
		Text angleText2 = new Text("The DMS format will display the degree, minutes and seconds.");
		Text angleText3 = new Text(
				"The DM format will display the degrees and minutes. Minutes will have a precision of four decimal places.");
		Text angleText4 = new Text("The DD format will display the degrees with a precision of four decimal places.");
		angleTextFlow.getChildren().add(angleText1);
		angleTextFlow.getChildren().add(angleText2);
		angleTextFlow.getChildren().add(angleText3);
		angleTextFlow.getChildren().add(angleText4);
		gridPane.add(angleTextFlow, 1, 3, 2, 1);

		Label performanceLabel = new Label("Performance Panel");
		gridPane.add(performanceLabel, 0, 4);
		this.enablePerformancePanel = new CheckBox("");
		this.enablePerformancePanel.selectedProperty().bindBidirectional(preferences.enablePerformancePanelProperty());
		this.enablePerformancePanel.setAlignment(Pos.CENTER_LEFT);
		gridPane.add(this.enablePerformancePanel, 1, 4);

		TextFlow performancePanelFlow = new TextFlow();
		Text performancePanelText1 = new Text("Toggles the visibility of the performance panel.");
		Text performancePanelText2 = new Text(
				"The performance panel displays runtime statistics, such as frames per second and memory usage of indiivdual layers.");
		performancePanelFlow.getChildren().add(performancePanelText1);
		performancePanelFlow.getChildren().add(performancePanelText2);
		gridPane.add(performancePanelFlow, 1, 5, 2, 1);

		Label layerLabel = new Label("Layer Panel");
		gridPane.add(layerLabel, 0, 6);
		this.enableLayerPanel = new CheckBox("");
		this.enableLayerPanel.selectedProperty().bindBidirectional(preferences.enableLayerPanelProperty());
		this.enableLayerPanel.setAlignment(Pos.CENTER_LEFT);
		gridPane.add(this.enableLayerPanel, 1, 6);

		TextFlow layerPanelFlow = new TextFlow();
		Text layerPanelText1 = new Text("Toggles the visibility of the layer panel.");
		Text layerPanelText2 = new Text(
				"The layer panel toggles all WW layers, including all weather layers, image layers, and interface control overlays.");
		layerPanelFlow.getChildren().add(layerPanelText1);
		layerPanelFlow.getChildren().add(layerPanelText2);
		gridPane.add(layerPanelFlow, 1, 7, 2, 1);

		Label networkLabel = new Label("Network Connection");
		gridPane.add(networkLabel, 0, 8);
		this.enableNetwork = new CheckBox("");
		this.enableNetwork.selectedProperty().bindBidirectional(preferences.enableNetworkProperty());
		this.enableNetwork.setAlignment(Pos.CENTER_LEFT);
		gridPane.add(this.enableNetwork, 1, 8);

		TextFlow networkConnectionFlow = new TextFlow();
		Text networkConnectionText1 = new Text("Toggles the visibility of the performance panel.");
		Text networkConnectionText2 = new Text(
				"The performance panel displays runtime statistics, such as frames per second and memory usage of indiivdual layers.");
		networkConnectionFlow.getChildren().add(networkConnectionText1);
		networkConnectionFlow.getChildren().add(networkConnectionText2);
		gridPane.add(networkConnectionFlow, 1, 9, 2, 1);
	}

	@Override
	public Node getNode()
	{
		return this;
	}
}