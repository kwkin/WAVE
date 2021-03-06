package wave.views.windows.survey;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import wave.infrastructure.survey.ScenarioType;
import wave.infrastructure.survey.SurveyQuestion;

public interface QuestionPanel
{
	public String getQuestion();

	public String getAnswer();

	public Node getNode();

	public boolean getIsAnswerSelected();

	public BooleanProperty isAnswerSelectedProperty();

	public void stopSound();
	
	/**
	 * Create a survey panel given the survey scenario
	 * 
	 * @param scenario
	 */
	public static QuestionPanel CreatePanel(SurveyQuestion scenario)
	{
		ScenarioType type = scenario.getType();
		QuestionPanel panel = null;
		switch (type)
		{
		case DIRECTION:
			panel = new DirectionSurveyPanel(scenario);
			break;
		case RATING:
			panel = new RatingSurveyPanel(scenario);
			break;
		case SCENARIO:
			panel = new ScenarioSurveyPanel(scenario);
			break;
		default:
			break;
		}
		return panel;
	}
}
