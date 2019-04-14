package wave.infrastructure.survey;

public class ScenarioQuestion implements SurveyQuestion
{
	private String question;
	
	public ScenarioQuestion(String question)
	{
		this.question = question;
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
