package wave.infrastructure.survey;

import wave.audio.SurveySounds;

public class ScenarioQuestion implements SurveyQuestion
{
	private String question;
	private SurveySounds soundA;
	private SurveySounds soundB;
	private String answer;
	private int repeat;
	
	public ScenarioQuestion(String question, SurveySounds soundA, SurveySounds soundB, String answer)
	{
		this.question = question;
		this.soundA = soundA;
		this.soundB = soundB;
		this.answer = answer;
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

	@Override
	public String getAnswer()
	{
		return this.answer;
	}

	@Override
	public void setRepeat(int repeat)
	{
		this.repeat = repeat;
	}

	@Override
	public int getRepeat()
	{
		return repeat;
	}
}
