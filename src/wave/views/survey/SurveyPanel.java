package wave.views.survey;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import wave.infrastructure.WaveSession;
import wave.infrastructure.survey.Survey;
import wave.infrastructure.survey.SurveyScenario;

// TODO add survey completion dialog
// TODO write results of survey to file
public class SurveyPanel extends BorderPane
{
	protected Survey survey;
	protected QuestionPanel currentPanel;
	protected Button repeatSound;

	private final StringProperty questionTextProperty;
	private WaveSession session;
	private Button nextScenario;
	private boolean isSurveyStarted;
	private GridPane buttonPanel;

	public SurveyPanel(WaveSession session)
	{
		this.session = session;
		this.isSurveyStarted = false;
		this.questionTextProperty = new SimpleStringProperty();

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
		Text text2 = new Text(
				"In this experiment, you will be asked several questions related to the audio design of WAVE. ");
		initialText.getChildren().add(text2);
		Text text3 = new Text(
				"The questions mainly consist of listening to various weather scenarios, and selecting an answer based upon the perceived audio. ");
		initialText.getChildren().add(text3);
		Text text4 = new Text("You must select an answer before proceeding to the next scenario. ");
		initialText.getChildren().add(text4);
		Text text5 = new Text(
				"The survey will take approximately 15 minutes to complete, but you may leave at any time. \n\n");
		initialText.getChildren().add(text5);
		Text text6 = new Text("Click ");
		initialText.getChildren().add(text6);
		Text text7 = new Text("Start Survey ");
		text7.setStyle("-fx-font-weight: bold");
		initialText.getChildren().add(text7);
		Text text8 = new Text("when you are ready to proceed. ");
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
		this.nextScenario = new Button("Start Survey");
		this.buttonPanel.add(this.nextScenario, 1, 1);
		this.nextScenario.setOnAction((event) ->
		{
			nextScenario();
		});
	}

	protected void startSurvey()
	{
		try
		{
			this.survey = Survey.CreateSurveyFile(this.session);
			this.nextScenario.setText("Next Scenario");
			this.isSurveyStarted = true;
		}
		catch (IOException e)
		{
			// TODO create error dialog
			System.out.println(e.getMessage());
		}
	}

	protected void nextScenario()
	{
		if (!this.isSurveyStarted)
		{
			startSurvey();
			Button repeatSound = new Button("Repeat Sound");
			this.buttonPanel.add(repeatSound, 0, 1);
		}
		int questionIndex = this.survey.getScenarioIndex();
		// Write answer
		if (this.currentPanel != null)
		{
			String answer = this.currentPanel.getAnswer();
			this.survey.writeAnswer(questionIndex, answer);
		}
		if (questionIndex == this.survey.getScenarioCount() - 1)
		{
			this.survey.closeSurveyFile();
			System.out.println("You completed the survey.");
		}
		else
		{
			// Update window for next scenario
			SurveyScenario nextScenario = this.survey.getNextScenario();
			QuestionPanel panel = QuestionPanel.CreatePanel(nextScenario);
			this.currentPanel = panel;
			this.nextScenario.disableProperty().unbind();
			this.nextScenario.disableProperty().bind(this.currentPanel.isAnswerSelectedProperty().not());
			StringBuilder questionText = new StringBuilder("Question ");
			questionText.append(this.survey.getScenarioIndex() + 1);
			questionText.append(" of ");
			questionText.append(this.survey.getScenarioCount());
			this.questionTextProperty.setValue(questionText.toString());
			this.setCenter(panel.getNode());
		}

	}
}