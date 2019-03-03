package wave.infrastructure.survey;

public enum ScenarioType
{
	SCENARIO("Scenario"),
	DIRECTION("Direction"),
	RATING("Rating");
	
	private String name;
	
	ScenarioType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
