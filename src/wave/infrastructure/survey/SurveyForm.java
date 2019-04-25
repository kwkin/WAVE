package wave.infrastructure.survey;

import java.util.ArrayList;
import java.util.List;

import wave.audio.Rain;
import wave.audio.SurveySounds;
import wave.audio.Thunder;
import wave.audio.Wind;

public class SurveyForm
{
	protected List<SurveyQuestion> scenarios;

	public SurveyForm()
	{
		this.scenarios = new ArrayList<SurveyQuestion>();

		//@formatter:off
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the rain was heavier in sound A or sound B?", 
				new SurveySounds(-1, -1, Rain.LIGHT, 1), 
				new SurveySounds(-1, -1, Rain.MEDIUM, 1), 
				"Sound B"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the rain was heavier in sound A or sound B?", 
				new SurveySounds(-1, -1, Rain.HEAVY, 1), 
				new SurveySounds(-1, -1, Rain.MEDIUM, 1), 
				"Sound A"));

		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.LIGHT, 1), 
				new SurveySounds(-1, -1, Wind.BREEZE, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.MEDIUM, 1), 
				new SurveySounds(-1, -1, Wind.LIGHT, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.EXTREME, 1), 
				new SurveySounds(-1, -1, Wind.HIGH, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.MEDIUM, 1), 
				new SurveySounds(-1, -1, Wind.HIGH, 1), 
				"Sound B"));

		this.scenarios.add(new ScenarioQuestion(
				"Do you think the thunder was closer in sound A or sound B?", 
				new SurveySounds(-1, -1, Thunder.MEDIUM, 1, 10000), 
				new SurveySounds(-1, -1, Thunder.FAR, 1, 10000), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the thunder was closer in sound A or sound B?", 
				new SurveySounds(-1, -1, Thunder.MEDIUM, 1, 10000), 
				new SurveySounds(-1, -1, Thunder.CLOSE, 1, 10000), 
				"Sound B"));

		// 100
		int rElev = 24;
		int direction = 24;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, rElev, Wind.MEDIUM, 1), 
				100));
		// -55
		direction = 2;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, Wind.LIGHT, 1), 
				-55));
		// 160
		direction = 16;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, rElev, Wind.MEDIUM, 1), 
				160));
		// 40
		direction = 20;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, Wind.EXTREME, 1), 
				40));
		// -160
		direction = 8;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, rElev, Wind.HIGH, 1), 
				-160));
		// 0
		direction = 12;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, Wind.LIGHT, 1), 
				0));
		// 45
		direction = 21;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, Wind.EXTREME, 1), 
				45));
		// -140
		direction = 4;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, rElev, Wind.LIGHT, 1), 
				-140));
		// -15
		direction = 9;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, -1, Wind.MEDIUM, 1), 
				-15));
		// -100
		direction = 0;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(direction, rElev, Wind.EXTREME, 1), 
				-100));
		
		// -45
		direction = 3;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, Thunder.FAR, 1), 
				-45));
		// 125
		direction = 22;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, rElev, Thunder.MEDIUM, 1), 
				125));
		// -30
		direction = 6;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, Thunder.CLOSE, 1), 
				-30));
		// 145
		direction = 19;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, rElev, Thunder.FAR, 1), 
				145));
		// -5
		direction = 11;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, Thunder.FAR, 1), 
				-5));
		// 110
		direction = 16;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, rElev, Thunder.CLOSE, 1), 
				110));
		// 10
		direction = 14;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, Thunder.MEDIUM, 1), 
				40));
		// -105
		direction = 9;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, rElev, Thunder.FAR, 1), 
				-105));
		// -80 degrees
		direction = 0;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, -1, Thunder.MEDIUM, 1), 
				-80));
		// 100
		direction = 24;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(direction, rElev, Thunder.CLOSE, 1), 
				100));
		
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, Thunder.FAR, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, Thunder.MEDIUM, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, Thunder.CLOSE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, Rain.LIGHT, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, Rain.MEDIUM, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, Rain.HEAVY, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, Wind.BREEZE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, Wind.MEDIUM, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, Wind.EXTREME, 1)));
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
