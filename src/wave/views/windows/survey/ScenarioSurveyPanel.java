package wave.views.windows.survey;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import wave.infrastructure.survey.SurveyQuestion;

public class ScenarioSurveyPanel extends BorderPane implements QuestionPanel
{
	private final BooleanProperty isAnswerSelectedProperty;
	private final Label questionLabel;
	private ToggleGroup toggleGroup;

	public ScenarioSurveyPanel(SurveyQuestion scenario)
	{
		this.isAnswerSelectedProperty = new SimpleBooleanProperty();

		this.setPadding(new Insets(10, 10, 10, 10));
		this.questionLabel = new Label(scenario.getQuestion());
		this.questionLabel.setPadding(new Insets(5, 5, 10, 5));
		this.setTop(this.questionLabel);

		GridPane scenarioPane = new GridPane();
		scenarioPane.setPadding(new Insets(5, 5, 5, 5));
		scenarioPane.setHgap(5);
		scenarioPane.setVgap(8);
		this.toggleGroup = new ToggleGroup();
		this.toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
		{
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
			{
				isAnswerSelectedProperty.setValue(true);
			}
		});
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
		RadioButton answer = (RadioButton) this.toggleGroup.getSelectedToggle();
		return answer.getText();
	}

	@Override
	public Node getNode()
	{
		return this;
	}

	@Override
	public boolean getIsAnswerSelected()
	{
		return this.isAnswerSelectedProperty.getValue();
	}

	@Override
	public BooleanProperty isAnswerSelectedProperty()
	{
		return this.isAnswerSelectedProperty;
	}
}
