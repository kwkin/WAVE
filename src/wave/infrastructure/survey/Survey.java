package wave.infrastructure.survey;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.StringJoiner;
import java.util.stream.Stream;

import wave.infrastructure.WaveSession;
import wave.infrastructure.handlers.ErrorDialog;

public class Survey
{
	protected WaveSession session;
	protected BufferedWriter fileWriter;
	protected Path file;
	protected SurveyForm form;
	protected int scenarioIndex;

	private Survey(WaveSession session, Path file, SurveyForm form)
	{
		this.session = session;
		this.file = file;
		this.form = form;
		this.scenarioIndex = 0;
		try
		{
			this.fileWriter = Files.newBufferedWriter(this.file);
			writeHeader();
		}
		catch (IOException e)
		{
			StringBuilder errorMessage = new StringBuilder("Error: Unable to create survey file. \n\n");
			errorMessage.append(e.getMessage());
			ErrorDialog.show(null, errorMessage.toString());
		}
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
	public static Survey CreateSurveyFile(WaveSession session, SurveyForm form) throws IOException
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
		String surveyFileName = String.format("participant%d_%d-%d-%d_%d-%d-%d.csv", 
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
		return new Survey(session, surveyFile, form);
	}

	public SurveyQuestion getNextScenario()
	{
		if (scenarioIndex == this.form.getScenarios().size())
		{
			throw new IndexOutOfBoundsException("Last scenario already reached.");
		}
		return this.form.getScenario(this.scenarioIndex++);
	}

	public SurveyQuestion getScenario(int index)
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

	public void writeAnswer(int questionNumber, SurveyQuestion scenario, String answer)
	{
		StringJoiner tokenizer = new StringJoiner(",");
		tokenizer.add(Integer.toString(questionNumber));
		tokenizer.add(scenario.getType().toString());
		tokenizer.add(answer);
		tokenizer.add(scenario.getAnswer());
		tokenizer.add(Integer.toString(scenario.getRepeat()));

		LocalDateTime curTime = LocalDateTime.now();
		long curEpoch = curTime.toEpochSecond(ZoneOffset.UTC);
		tokenizer.add(Long.toString(curEpoch));
		try
		{
			this.fileWriter.write(tokenizer.toString());
			this.fileWriter.newLine();
		}
		catch (IOException e)
		{

		}
	}

	public void closeSurveyFile()
	{
		try
		{
			this.fileWriter.close();
		}
		catch (IOException e)
		{
			StringBuilder errorMessage = new StringBuilder("Error: Unable to close survey file. \n\n");
			errorMessage.append(e.getMessage());
			ErrorDialog.show(null, errorMessage.toString());
		}
	}

	private void writeHeader()
	{
		StringJoiner tokenizer = new StringJoiner(",");
		tokenizer.add("question");
		tokenizer.add("type");
		tokenizer.add("answer");
		tokenizer.add("correct");
		tokenizer.add("repeat");
		tokenizer.add("timestamp");
		try
		{
			this.fileWriter.write(tokenizer.toString());
			this.fileWriter.newLine();
		}
		catch (IOException e)
		{

		}
	}
}
