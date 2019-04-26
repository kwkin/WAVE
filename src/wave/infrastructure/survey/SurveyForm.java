package wave.infrastructure.survey;

import java.util.ArrayList;
import java.util.List;

import wave.audio.RainSounds;
import wave.audio.SurveySounds;
import wave.audio.ThunderSounds;
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
				new SurveySounds(-1, -1, RainSounds.LIGHT_FADE, 1), 
				new SurveySounds(-1, -1, RainSounds.MEDIUM_FADE, 1), 
				"Sound B"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the rain was heavier in sound A or sound B?", 
				new SurveySounds(-1, -1, RainSounds.HEAVY_FADE, 1), 
				new SurveySounds(-1, -1, RainSounds.MEDIUM_FADE, 1), 
				"Sound A"));

		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.LIGHT_FADE, 1), 
				new SurveySounds(-1, -1, Wind.BREEZE_FADE, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.MEDIUM_FADE, 1), 
				new SurveySounds(-1, -1, Wind.LIGHT_FADE, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.EXTREME_FADE, 1), 
				new SurveySounds(-1, -1, Wind.HIGH_FADE, 1), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, Wind.MEDIUM_FADE, 1), 
				new SurveySounds(-1, -1, Wind.HIGH_FADE, 1), 
				"Sound B"));

		this.scenarios.add(new ScenarioQuestion(
				"Do you think the thunder was closer in sound A or sound B?", 
				new SurveySounds(-1, -1, ThunderSounds.MEDIUM, 1, 10000), 
				new SurveySounds(-1, -1, ThunderSounds.FAR, 1, 10000), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the thunder was closer in sound A or sound B?", 
				new SurveySounds(-1, -1, ThunderSounds.MEDIUM, 1, 10000), 
				new SurveySounds(-1, -1, ThunderSounds.CLOSE, 1, 10000), 
				"Sound B"));

		// 100
		int elevation = 0;
		int heading = 100;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, Wind.MEDIUM_FADE, 1), 
				heading));
		// -55
		heading = -55;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, Wind.LIGHT_FADE, 1), 
				heading));
		// 160
		heading = 160;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, Wind.MEDIUM_FADE, 1), 
				heading));
		// 40
		heading = 40;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, Wind.EXTREME_FADE, 1), 
				heading));
		// -160
		heading = -160;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, Wind.HIGH_FADE, 1), 
				heading));
		// 0
		heading = 0;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, Wind.LIGHT_FADE, 1), 
				heading));
		// 45
		heading = 45;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, Wind.EXTREME_FADE, 1), 
				heading));
		// -140
		heading = -140;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, Wind.LIGHT_FADE, 1), 
				heading));
		// -15
		heading = -15;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, Wind.MEDIUM_FADE, 1), 
				-heading));
		// -100
		heading = -100;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, Wind.EXTREME_FADE, 1), 
				heading));
		
		// -45
		heading = -45;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.FAR, 1), 
				heading));
		// 125
		heading = 125;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.MEDIUM, 1), 
				heading));
		// -30
		heading = -30;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.CLOSE, 1), 
				heading));
		// 145
		heading = 145;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.FAR, 1), 
				heading));
		// -5
		heading = -5;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.FAR, 1), 
				heading));
		// 110
		heading = 110;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.CLOSE, 1), 
				heading));
		// 10
		heading = 10;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.MEDIUM, 1), 
				heading));
		// -105
		heading = -105;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.FAR, 1), 
				heading));
		// -80 degrees
		heading = -80;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.MEDIUM, 1), 
				heading));
		// 100
		heading = 100;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.CLOSE, 1), 
				heading));
		
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, ThunderSounds.FAR, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, ThunderSounds.MEDIUM, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, ThunderSounds.CLOSE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, RainSounds.LIGHT_FADE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, RainSounds.MEDIUM_FADE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, RainSounds.HEAVY_FADE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, Wind.BREEZE_FADE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, Wind.MEDIUM_FADE, 1)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, Wind.EXTREME_FADE, 1)));
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
