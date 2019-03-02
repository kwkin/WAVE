package wave.views.survey;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import wave.infrastructure.survey.SurveyScenario;

public class ScenarioSurveyPanel extends BorderPane implements SurveyPanel
{
	private final Label questionLabel;
	private ToggleGroup toggleGroup;
	
	public ScenarioSurveyPanel(SurveyScenario scenario)
	{
		this.setPadding(new Insets(10, 10, 10, 10));
		this.questionLabel = new Label(scenario.getQuestion());
		this.questionLabel.setPadding(new Insets(5, 5, 10, 5));
		this.setTop(this.questionLabel);
		
		GridPane scenarioPane = new GridPane();
		scenarioPane.setPadding(new Insets(5, 5, 5, 5));
		scenarioPane.setHgap(5);
		scenarioPane.setVgap(8);
		this.toggleGroup = new ToggleGroup();
		RadioButton firstScenario = new RadioButton("First");
		firstScenario.setToggleGroup(this.toggleGroup);
		RadioButton secondScenario = new RadioButton("Second");
		secondScenario.setToggleGroup(this.toggleGroup);
		scenarioPane.add(firstScenario, 0, 0);
		scenarioPane.add(secondScenario, 0, 1);
		this.setCenter(scenarioPane);
	}

	@Override
	public String getQuestion()
	{
		return this.questionLabel.getText();
	}

	@Override
	public String getAnswer()
	{
		RadioButton answer = (RadioButton)this.toggleGroup.getSelectedToggle();
		return answer.getText();
	}

	@Override
	public Node getNode()
	{
		return this;
	}
}
