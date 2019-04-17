package wave.infrastructure.survey;

public interface SurveyQuestion
{
	public String getAnswer();
	
	public String getQuestion();

	public ScenarioType getType();
	
	public void setRepeat(int repeat);
	
	public int getRepeat();
}
