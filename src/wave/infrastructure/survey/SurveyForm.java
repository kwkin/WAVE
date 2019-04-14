package wave.infrastructure.survey;

import java.util.ArrayList;
import java.util.List;

import wave.audio.SurveySounds;

public class SurveyForm
{
	protected List<SurveyQuestion> scenarios;

	public SurveyForm()
	{
		this.scenarios = new ArrayList<SurveyQuestion>();

		//@formatter:off
//		this.scenarios.add(new ScenarioQuestion("Do you think the rain was heavier in sound A or sound B?", sound1A, sound1B));
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
		SurveySounds sound13A = new SurveySounds(-1, -1, SurveySounds.FAR, 1, 10000);
		SurveySounds sound13B = new SurveySounds(-1, -1, SurveySounds.MEDIUM, 1, 10000);
		this.scenarios.add(new ScenarioQuestion("Do you think the thunder was closer in sound A or sound B?", sound13A, sound13B));
		SurveySounds sound14A = new SurveySounds(-1, -1, SurveySounds.CLOSE, 1, 10000);
		SurveySounds sound14B = new SurveySounds(-1, -1, SurveySounds.MEDIUM, 1, 10000);
		this.scenarios.add(new ScenarioQuestion("Do you think the thunder was closer in sound A or sound B?", sound14A, sound14B));
		SurveySounds sound15A = new SurveySounds(-1, -1, SurveySounds.CLOSE, 1, 10000);
		SurveySounds sound15B = new SurveySounds(-1, -1, SurveySounds.FAR, 1, 10000);
		this.scenarios.add(new ScenarioQuestion("Do you think the thunder was closer in sound A or sound B?", sound15A, sound15B));
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
