package wave.infrastructure.survey;

import wave.audio.SurveySounds;

public class RatingQuestion implements SurveyQuestion
{
	private String question;
	private SurveySounds sound;
	private String answer;
	private int repeat;
	
	public RatingQuestion(String question, SurveySounds soundA)
	{
		this.question = question;
		this.sound = soundA;
		this.answer = "-";
	}
	
	public SurveySounds getSound()
	{
		return this.sound;
	}
	
	@Override
	public String getQuestion()
	{
		return this.question;
	}

	@Override
	public ScenarioType getType()
	{
		return ScenarioType.RATING;
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
