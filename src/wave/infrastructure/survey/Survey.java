package wave.infrastructure.survey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import wave.infrastructure.WaveSession;

public class Survey
{
	protected WaveSession session;
	protected Path file;
	protected SurveyForm form;
	protected int scenarioIndex;
	
	private Survey(WaveSession session, Path file)
	{
		this.session = session;
		this.file = file;
		// TODO replace DI
		this.form = new SurveyForm();
		
		// TODO find better way other than initializing to -1
		this.scenarioIndex = -1;
	}

	/**
	 * Create a answer file for the survey.
	 * 
	 * The answer file will contain the participant number and the current data and
	 * time.
	 * 
	 * @return The path to the created survey file.
	 * @throws IOException If there was an error creating the survey file
	 */
	public static Survey CreateSurveyFile(WaveSession session) throws IOException
	{
		Path surveyDirectory = Paths.get("data", "survey");
		long numDirectories = 0;
		try (Stream<Path> files = Files.list(surveyDirectory))
		{
			numDirectories = files.count();
		}
		catch (IOException e)
		{

		}
		LocalDateTime curTime = LocalDateTime.now();
		// @formatter:off
		String surveyFileName = String.format("participant%d_%d-%d-%d_%d-%d-%d.xml", 
				numDirectories, 
				curTime.getYear(),
				curTime.getMonthValue(), 
				curTime.getDayOfMonth(), 
				curTime.getHour(), 
				curTime.getMinute(),
				curTime.getSecond());
		// @formatter:on
		Path surveyFile = Paths.get(surveyDirectory.toString(), surveyFileName);
		if (!Files.exists(surveyFile))
		{
			surveyFile = Files.createFile(surveyFile);
		}
		else
		{
			StringBuilder message = new StringBuilder("Unable to create Survey file. File ");
			message.append(surveyFile.toAbsolutePath());
			message.append(" already exists.");
			throw new IOException(message.toString());
		}
		Survey survey = new Survey(session, surveyFile);
		return survey;
	}
	
	public SurveyScenario getNextScenario()
	{
		// TODO handle last scenario case
		if (scenarioIndex < this.form.getScenarios().size() - 1)
		{
			this.scenarioIndex++;
		}
		return this.form.getScenario(this.scenarioIndex);
	}
	
	public SurveyScenario getScenario(int index)
	{
		this.scenarioIndex = index;
		return this.form.getScenario(this.scenarioIndex);
	}
	
	public int getScenarioIndex()
	{
		return this.scenarioIndex;
	}
	
	public int getScenarioCount()
	{
		return this.form.getScenarios().size();
	}
}
