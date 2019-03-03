package wave.infrastructure.survey;

public enum ScenarioType
{
	// @formatter:off
	SCENARIO("Scenario"),
	DIRECTION("Direction"),
	RATING("Rating");
	// @formatter:on

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
