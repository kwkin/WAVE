package wave.infrastructure.survey;

public class SurveyScenario
{
	// TODO add scenario create: sounds, position, etc
	protected ScenarioType type;
	protected String question;

	public SurveyScenario(ScenarioType type, String question)
	{
		this.type = type;
		this.question = question;
	}

	public String getQuestion()
	{
		return this.question;
	}

	public ScenarioType getType()
	{
		return this.type;
	}
}
