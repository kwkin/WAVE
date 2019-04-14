package wave.infrastructure.survey;

import wave.audio.SurveySounds;

public class ScenarioQuestion implements SurveyQuestion
{
	// TODO move thunder under an interface
	private String question;
	private SurveySounds soundA;
	private SurveySounds soundB;
	
	public ScenarioQuestion(String question, SurveySounds soundA, SurveySounds soundB)
	{
		this.question = question;
		this.soundA = soundA;
		this.soundB = soundB;
	}
	
	public SurveySounds getSoundA()
	{
		return this.soundA;
	}
	
	public SurveySounds getSoundB()
	{
		return this.soundB;
	}
	
	@Override
	public String getQuestion()
	{
		return this.question;
	}

	@Override
	public ScenarioType getType()
	{
		return ScenarioType.SCENARIO;
	}
}
