package wave.views.survey;

import javafx.scene.Node;
import wave.infrastructure.survey.ScenarioType;
import wave.infrastructure.survey.SurveyScenario;

public interface SurveyPanel
{
	public String getQuestion();
	public String getAnswer();
	public Node getNode();
	
	/**
	 * Create a survey panel given the survey scenario
	 * @param scenario
	 */
	public static SurveyPanel CreatePanel(SurveyScenario scenario)
	{
		ScenarioType type = scenario.getType();
		SurveyPanel panel = null;
		switch(type)
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
