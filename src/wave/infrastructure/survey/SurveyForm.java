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
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the rain was heavier in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.RAIN_0, 1), 
				new SurveySounds(-1, -1, SurveySounds.RAIN_1, 1), 
				"Sound B"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the rain was heavier in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.RAIN_2, 1), 
				new SurveySounds(-1, -1, SurveySounds.RAIN_1, 1), 
				"Sound A"));

		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.WIND_1, 1), 
				new SurveySounds(-1, -1, SurveySounds.WIND_0, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.WIND_2, 1), 
				new SurveySounds(-1, -1, SurveySounds.WIND_1, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.WIND_4, 1), 
				new SurveySounds(-1, -1, SurveySounds.WIND_3, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.WIND_2, 1), 
				new SurveySounds(-1, -1, SurveySounds.WIND_3, 1), 
				"Sound B"));

		this.scenarios.add(new ScenarioQuestion(
				"Do you think the thunder was closer in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.THUNDER_1, 1, 10000), 
				new SurveySounds(-1, -1, SurveySounds.THUNDER_0, 1, 10000), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the thunder was closer in sound A or sound B?", 
				new SurveySounds(-1, -1, SurveySounds.THUNDER_1, 1, 10000), 
				new SurveySounds(-1, -1, SurveySounds.THUNDER_2, 1, 10000), 
				"Sound B"));

		// 0
		int direction = 12;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_1, 1), 
				direction));
		// 55
		direction = 24;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_2, 1), 
				direction));
		// -55
		direction = 2;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_0, 1), 
				direction));
		// 20
		direction = 16;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_2, 1), 
				direction));
		// 40
		direction = 20;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_4, 1), 
				direction));
		// -20
		direction = 8;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_3, 1), 
				direction));
		// 45
		direction = 21;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_0, 1), 
				direction));
		// -40
		direction = 4;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_1, 1), 
				direction));
		// -15
		direction = 9;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_2, 1), 
				direction));
		// -80
		direction = 0;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, SurveySounds.WIND_4, 1), 
				direction));
		
		// -45
		direction = 3;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_0, 1), 
				direction));
		// 55
		direction = 22;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_1, 1), 
				direction));
		// -30
		direction = 6;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_2, 1), 
				direction));
		// 35
		direction = 19;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_0, 1), 
				direction));
		// -5
		direction = 11;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_0, 1), 
				direction));
		// 15
		direction = 16;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_2, 1), 
				direction));
		// 10
		direction = 14;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_1, 1), 
				direction));
		// -15
		direction = 9;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_0, 1), 
				direction));
		// -80 degrees
		direction = 0;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_1, 1), 
				direction));
		// 80
		direction = 24;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, SurveySounds.THUNDER_2, 1), 
				direction));
		
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.THUNDER_0, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.THUNDER_1, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.THUNDER_2, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.RAIN_0, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.RAIN_1, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.RAIN_2, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.WIND_0, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.WIND_2, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, SurveySounds.WIND_4, 1)));
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
