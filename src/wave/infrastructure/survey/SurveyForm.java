package wave.infrastructure.survey;

import java.util.ArrayList;
import java.util.List;

public class SurveyForm
{
	protected List<SurveyQuestion> scenarios;

	public SurveyForm()
	{
		this.scenarios = new ArrayList<SurveyQuestion>();

		//@formatter:off
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the rain was heavier in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the rain was heavier in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the rain was heavier in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the rain was heavier in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the rain was heavier in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the rain was heavier in sound A or sound B?"));
//
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the wind speed was higher in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the wind speed was higher in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the wind speed was higher in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the wind speed was higher in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the wind speed was higher in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the wind speed was higher in sound A or sound B?"));
//
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the thunder was closer in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the thunder was closer in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the thunder was closer in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the thunder was closer in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the thunder was closer in sound A or sound B?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.SCENARIO, "Do you think the thunder was closer in sound A or sound B?"));
//		
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the wind come from?"));
//		
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.DIRECTION, "What direction did the thunder originate from?"));
//		
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this thunder audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this thunder audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this thunder audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this rain audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this rain audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this rain audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this wind audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this wind audio clip?"));
//		this.scenarios.add(new SurveyQuestion(ScenarioType.RATING, "On a scale of 1-5, 1 being not at all realistic and 5 being completely realistic, how realistic is this wind audio clip?"));
		//@formatter:on
	}

	public List<SurveyQuestion> getScenarios()
	{
		return this.scenarios;
	}

	public SurveyQuestion getScenario(int index)
	{
		return this.scenarios.get(index);
	}
}
