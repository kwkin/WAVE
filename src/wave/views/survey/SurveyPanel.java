package wave.views.survey;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import wave.infrastructure.WaveSession;
import wave.infrastructure.survey.Survey;
import wave.infrastructure.survey.SurveyForm;
import wave.infrastructure.survey.SurveyScenario;

public class SurveyPanel extends BorderPane
{
	protected WaveSession session;
	protected SurveyWindow parent;
	protected Survey survey;
	protected QuestionPanel currentPanel;
	protected SurveyScenario currentScenario;
	protected boolean isSurveyStarted;
	protected boolean isSurveyCompleted;

	private final StringProperty questionTextProperty;
	private final StringProperty nextScenarioTextProperty;
	private Button nextScenarioButton;
	private Button replaySoundButton;
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
		TextFlow titleText = new TextFlow();
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setPadding(new Insets(10, 10, 10, 10));
		Text text1 = new Text();
		text1.textProperty().bind(this.questionTextProperty);
		this.questionTextProperty.setValue("Thank you for participating in the WAVE survey!");
		titleText.getChildren().add(text1);
		titlePane.setCenter(titleText);
		titlePane.setBottom(new Separator());
		this.setTop(titlePane);

		// Center content
		TextFlow initialText = new TextFlow();
		initialText.setTextAlignment(TextAlignment.CENTER);
		initialText.setPadding(new Insets(10, 10, 10, 10));

		// @formatter:off
		Text text2 = new Text("In this experiment, you will be asked several questions related to the audio design of WAVE. ");
		Text text3 = new Text("The questions mainly consist of listening to various weather scenarios, and selecting an answer based upon the perceived audio. ");
		Text text4 = new Text("You must select an answer before proceeding to the next scenario. ");
		Text text5 = new Text("The survey will take approximately 15 minutes to complete, but you may leave at any time. \n\n");
		Text text6 = new Text("Click ");
		Text text7 = new Text("Start Survey ");
		Text text8 = new Text("when you are ready to proceed. ");
		// @formatter:on
		text7.setStyle("-fx-font-weight: bold");
		initialText.getChildren().add(text2);
		initialText.getChildren().add(text3);
		initialText.getChildren().add(text4);
		initialText.getChildren().add(text5);
		initialText.getChildren().add(text6);
		initialText.getChildren().add(text7);
		initialText.getChildren().add(text8);
		this.setCenter(initialText);

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
			this.replaySoundButton = new Button("Repeat Sound");
			this.buttonPanel.add(this.replaySoundButton, 0, 1);
			this.nextScenarioButton.setOnAction((event) ->
			{
				this.getNextScenario();
			});
			this.getNextScenario();
		}
		catch (IOException e)
		{
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Error");
			errorAlert.initModality(Modality.APPLICATION_MODAL);
			errorAlert.initOwner(this.parent);
			StringBuilder errorMessage = new StringBuilder("Error: Unable to create survey file. \n\n");
			errorMessage.append(e.getMessage());
			errorAlert.setContentText(errorMessage.toString());

			double xPosition = this.parent.getX() + this.parent.getWidth() / 2 - errorAlert.getWidth() / 2;
			errorAlert.setX(xPosition);
			double yPosition = this.parent.getY() + this.parent.getHeight() / 2 - errorAlert.getHeight() / 2;
			errorAlert.setY(yPosition);

			errorAlert.show();
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
		this.buttonPanel.getChildren().remove(this.replaySoundButton);

		TextFlow completionText = new TextFlow();
		completionText.setTextAlignment(TextAlignment.CENTER);
		completionText.setPadding(new Insets(10, 10, 10, 10));

		// @formatter:off
		Text text1 = new Text("You may now leave the testing area.");
		// @formatter:on

		completionText.getChildren().add(text1);
		this.setCenter(completionText);
		this.isSurveyCompleted = true;
		this.nextScenarioButton.setOnAction((event) ->
		{
			this.parent.surveyCompleted();
		});
	}
}