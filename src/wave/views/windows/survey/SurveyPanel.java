package wave.views.windows.survey;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.ErrorDialog;
import wave.infrastructure.survey.Survey;
import wave.infrastructure.survey.SurveyForm;
import wave.infrastructure.survey.SurveyQuestion;

public class SurveyPanel extends BorderPane
{
	protected WaveSession session;
	protected SurveyWindow parent;
	protected Survey survey;
	protected QuestionPanel currentPanel;
	protected SurveyQuestion currentScenario;
	protected boolean isSurveyStarted;
	protected boolean isSurveyCompleted;

	private final StringProperty questionTextProperty;
	private final StringProperty nextScenarioTextProperty;
	private Button nextScenarioButton;
	private GridPane buttonPanel;

	public SurveyPanel(WaveSession session, SurveyWindow parent)
	{
		this.session = session;
		this.parent = parent;
		this.isSurveyStarted = false;
		this.isSurveyCompleted = false;
		this.questionTextProperty = new SimpleStringProperty();
		this.nextScenarioTextProperty = new SimpleStringProperty();

		// Top content
		BorderPane titlePane = new BorderPane();
		this.questionTextProperty.setValue("Thank you for participating in the WAVE survey!");
		Label titleText = new Label();
		titleText.textProperty().bind(this.questionTextProperty);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setPadding(new Insets(10, 10, 10, 10));
		titlePane.setCenter(titleText);
		titlePane.setBottom(new Separator());
		this.setTop(titlePane);

		// Center content
		BorderPane centerBorder = new BorderPane();
		centerBorder.setPadding(new Insets(10, 10, 20, 10));
		Image icon;
		try
		{
			icon = new Image(Wave.MAIN_ICON.toUri().toURL().toString());
			ImageView image = new ImageView(icon);
			image.setFitWidth(icon.getWidth());
			image.setFitHeight(icon.getHeight());
			image.setPreserveRatio(true);
			centerBorder.setCenter(image);
		}
		catch (MalformedURLException e)
		{
			
		}

		// @formatter:off
		StringBuilder initialText = new StringBuilder("In this experiment, you will be asked several questions related to the audio design of WAVE. ");
		initialText.append("The questions mainly consist of listening to various weather scenarios, and selecting an answer based upon the perceived audio. ");
		initialText.append("You must select an answer before proceeding to the next scenario. ");
		initialText.append("The survey will take approximately 15 minutes to complete, but you may leave at any time. \n\n");
		initialText.append("Click \"Start Survey\" when you are ready to proceed.");
		Label initialLabel = new Label(initialText.toString());
		initialLabel.setTextAlignment(TextAlignment.CENTER);
		initialLabel.setPadding(new Insets(10, 10, 10, 10));
		initialLabel.setWrapText(true);
		// @formatter:on
		centerBorder.setBottom(initialLabel);
		this.setCenter(centerBorder);

		// Bottom Content
		this.buttonPanel = new GridPane();
		this.buttonPanel.setPadding(new Insets(5, 5, 5, 5));
		this.buttonPanel.setHgap(5);
		this.buttonPanel.setVgap(5);
		this.setBottom(this.buttonPanel);

		ColumnConstraints repeatColumn = new ColumnConstraints();
		repeatColumn.setHgrow(Priority.ALWAYS);
		repeatColumn.setHalignment(HPos.LEFT);
		this.buttonPanel.getColumnConstraints().add(repeatColumn);
		ColumnConstraints nextColumn = new ColumnConstraints();
		nextColumn.setHgrow(Priority.ALWAYS);
		nextColumn.setHalignment(HPos.RIGHT);
		this.buttonPanel.getColumnConstraints().add(nextColumn);

		Separator separator = new Separator();
		this.buttonPanel.add(separator, 0, 0, 2, 1);
		this.nextScenarioButton = new Button();
		this.nextScenarioButton.textProperty().bind(this.nextScenarioTextProperty);
		this.nextScenarioTextProperty.setValue("Start Survey");
		this.buttonPanel.add(this.nextScenarioButton, 1, 1);
		this.nextScenarioButton.setOnAction((event) ->
		{
			startSurvey();
		});
	}

	protected void startSurvey()
	{
		try
		{
			SurveyForm form = new SurveyForm();
			this.survey = Survey.CreateSurveyFile(this.session, form);
			this.nextScenarioTextProperty.setValue("Next Scenario");
			this.isSurveyStarted = true;
			this.nextScenarioButton.setOnAction((event) ->
			{
				this.currentPanel.stopSound();
				this.getNextScenario();
			});
			this.getNextScenario();
		}
		catch (IOException e)
		{
			StringBuilder errorMessage = new StringBuilder("Error: Unable to create survey file. \n\n");
			errorMessage.append(e.getMessage());
			ErrorDialog.show(this.parent, errorMessage.toString());
		}
	}

	protected void getNextScenario()
	{
		// Save answer
		int questionIndex = this.survey.getScenarioIndex();
		if (this.currentPanel != null)
		{
			String answer = this.currentPanel.getAnswer();
			this.survey.writeAnswer(questionIndex, this.currentScenario, answer);
		}
		if (questionIndex == this.survey.getScenarioCount())
		{
			this.SurveyCompleted();
		}
		else
		{

			// Next scenario
			this.currentScenario = this.survey.getNextScenario();
			QuestionPanel panel = QuestionPanel.CreatePanel(this.currentScenario);
			this.currentPanel = panel;
			this.nextScenarioButton.disableProperty().unbind();
			this.nextScenarioButton.disableProperty().bind(this.currentPanel.isAnswerSelectedProperty().not());
			StringBuilder questionText = new StringBuilder("Question ");
			questionText.append(this.survey.getScenarioIndex());
			questionText.append(" of ");
			questionText.append(this.survey.getScenarioCount());
			this.questionTextProperty.setValue(questionText.toString());
			this.setCenter(panel.getNode());
		}
	}

	protected void SurveyCompleted()
	{
		this.survey.closeSurveyFile();
		this.questionTextProperty.setValue("You have completed the survey!");
		this.nextScenarioTextProperty.setValue("Close Window");

		TextFlow completionText = new TextFlow();
		completionText.setTextAlignment(TextAlignment.CENTER);
		completionText.setPadding(new Insets(10, 10, 10, 10));

		Label text1 = new Label("You may now leave the testing area.");

		completionText.getChildren().add(text1);
		this.setCenter(completionText);
		this.isSurveyCompleted = true;
		this.nextScenarioButton.setOnAction((event) ->
		{
			this.parent.surveyCompleted();
		});
	}
}