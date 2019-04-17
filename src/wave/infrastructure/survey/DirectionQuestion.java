package wave.infrastructure.survey;

import wave.audio.SurveySounds;

public class DirectionQuestion implements SurveyQuestion
{
	private String question;
	private SurveySounds sound;
	private int direction;
	private String answer;
	private int repeat;
	
	public DirectionQuestion(String question, SurveySounds sound, int direction)
	{
		this.question = question;
		this.sound = sound;
		this.answer = Integer.toString(direction);
		this.direction = direction;
	}
	
	public int direction()
	{
		return this.direction;
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
		return ScenarioType.DIRECTION;
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
