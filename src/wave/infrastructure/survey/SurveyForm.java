package wave.infrastructure.survey;

import java.util.ArrayList;
import java.util.List;

import wave.audio.RainSounds;
import wave.audio.SurveySounds;
import wave.audio.ThunderSounds;
import wave.audio.WindSounds;

public class SurveyForm
{
	protected List<SurveyQuestion> scenarios;

	public SurveyForm()
	{
		this.scenarios = new ArrayList<SurveyQuestion>();

		//@formatter:off
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the rain was heavier in sound A or sound B?", 
				new SurveySounds(-1, -1, RainSounds.LIGHT_FADE), 
				new SurveySounds(-1, -1, RainSounds.MEDIUM_FADE), 
				"Sound B"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the rain was heavier in sound A or sound B?", 
				new SurveySounds(-1, -1, RainSounds.HEAVY_FADE), 
				new SurveySounds(-1, -1, RainSounds.MEDIUM_FADE), 
				"Sound A"));

		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, WindSounds.LIGHT_FADE), 
				new SurveySounds(-1, -1, WindSounds.BREEZE_FADE), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, WindSounds.MEDIUM_FADE), 
				new SurveySounds(-1, -1, WindSounds.LIGHT_FADE), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, WindSounds.EXTREME_FADE), 
				new SurveySounds(-1, -1, WindSounds.HIGH_FADE), 
				"Sound A"));
		this.scenarios.add(new ScenarioQuestion(
				"Do you think the wind was stronger in sound A or sound B?", 
				new SurveySounds(-1, -1, WindSounds.MEDIUM_FADE), 
				new SurveySounds(-1, -1, WindSounds.HIGH_FADE), 
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
				new SurveySounds(heading, elevation, WindSounds.MEDIUM_FADE), 
				heading));
		// -55
		heading = -55;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, WindSounds.LIGHT_FADE), 
				heading));
		// 160
		heading = 160;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, WindSounds.MEDIUM_FADE), 
				heading));
		// 40
		heading = 40;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, WindSounds.EXTREME_FADE), 
				heading));
		// -160
		heading = -160;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, WindSounds.HIGH_FADE), 
				heading));
		// 0
		heading = 0;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, WindSounds.LIGHT_FADE), 
				heading));
		// 45
		heading = 45;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, WindSounds.EXTREME_FADE), 
				heading));
		// -140
		heading = -140;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, WindSounds.LIGHT_FADE), 
				heading));
		// -15
		heading = -15;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, -1, WindSounds.MEDIUM_FADE), 
				-heading));
		// -100
		heading = -100;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the wind come from?", 
				new SurveySounds(heading, elevation, WindSounds.EXTREME_FADE), 
				heading));
		
		// -45
		heading = -45;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.FAR), 
				heading));
		// 125
		heading = 125;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.MEDIUM), 
				heading));
		// -30
		heading = -30;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.CLOSE), 
				heading));
		// 145
		heading = 145;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.FAR), 
				heading));
		// -5
		heading = -5;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.FAR), 
				heading));
		// 110
		heading = 110;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.CLOSE), 
				heading));
		// 10
		heading = 10;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.MEDIUM), 
				heading));
		// -105
		heading = -105;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.FAR), 
				heading));
		// -80 degrees
		heading = -80;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, -1, ThunderSounds.MEDIUM), 
				heading));
		// 100
		heading = 100;
		this.scenarios.add(new DirectionQuestion(
				"What direction did the thunder originate from?", 
				new SurveySounds(heading, elevation, ThunderSounds.CLOSE), 
				heading));
		
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, ThunderSounds.FAR)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, ThunderSounds.MEDIUM)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this thunder audio clip?", 
				new SurveySounds(-1, -1, ThunderSounds.CLOSE)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, RainSounds.LIGHT_FADE)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, RainSounds.MEDIUM_FADE)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this rain audio clip?", 
				new SurveySounds(-1, -1, RainSounds.HEAVY_FADE)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, WindSounds.BREEZE_FADE)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, WindSounds.MEDIUM_FADE)));
		this.scenarios.add(new RatingQuestion(
				"On a scale of 1-5, how realistic is this wind audio clip?", 
				new SurveySounds(-1, -1, WindSounds.EXTREME_FADE)));
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
