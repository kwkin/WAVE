package wave.views.survey;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import wave.models.survey.SurveyScenario;

// TODO replace rating with better input (stars?)
public class RatingSurveyPanel extends BorderPane implements SurveyPanel
{
	private final Label questionLabel;
	private ToggleGroup toggleGroup;
	
	public RatingSurveyPanel(SurveyScenario scenario)
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
		RadioButton rating1 = new RadioButton("1");
		rating1.setToggleGroup(this.toggleGroup);
		scenarioPane.add(rating1, 0, 0);
		RadioButton rating2 = new RadioButton("2");
		rating2.setToggleGroup(this.toggleGroup);
		scenarioPane.add(rating2, 0, 1);
		RadioButton rating3 = new RadioButton("3");
		rating3.setToggleGroup(this.toggleGroup);
		scenarioPane.add(rating3, 0, 2);
		RadioButton rating4 = new RadioButton("4");
		rating4.setToggleGroup(this.toggleGroup);
		scenarioPane.add(rating4, 0, 3);
		RadioButton rating5 = new RadioButton("5");
		rating5.setToggleGroup(this.toggleGroup);
		scenarioPane.add(rating5, 0, 4);
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
