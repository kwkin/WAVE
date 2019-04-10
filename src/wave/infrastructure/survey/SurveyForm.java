package wave.infrastructure.survey;

import java.util.ArrayList;
import java.util.List;

public class SurveyForm
{
	protected List<SurveyScenario> scenarios;

	public SurveyForm()
	{
		this.scenarios = new ArrayList<SurveyScenario>();

		// TODO complete scenarios
		SurveyScenario scenario1 = new SurveyScenario(ScenarioType.SCENARIO, "This is question 1");
		this.scenarios.add(scenario1);
		SurveyScenario scenario2 = new SurveyScenario(ScenarioType.DIRECTION, "What direction do you think the thunder came from?");
		this.scenarios.add(scenario2);
		SurveyScenario scenario3 = new SurveyScenario(ScenarioType.RATING, "This is question 3");
		this.scenarios.add(scenario3);
	}

	public List<SurveyScenario> getScenarios()
	{
		return this.scenarios;
	}

	public SurveyScenario getScenario(int index)
	{
		return this.scenarios.get(index);
	}
}
