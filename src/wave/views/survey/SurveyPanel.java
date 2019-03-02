package wave.views.survey;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

		this.buttonPanel = new GridPane();
		this.buttonPanel.setPadding(new Insets(5, 5, 5, 5));
		this.buttonPanel.setHgap(5);
		this.buttonPanel.setVgap(5);
		this.setBottom(this.buttonPanel);

		ColumnConstraints questionColumn = new ColumnConstraints();
		questionColumn.setHgrow(Priority.ALWAYS);
		questionColumn.setHalignment(HPos.LEFT);
		questionColumn.setPercentWidth(25);
		this.buttonPanel.getColumnConstraints().add(questionColumn);
		ColumnConstraints repeatColumn = new ColumnConstraints();
		repeatColumn.setHgrow(Priority.ALWAYS);
		repeatColumn.setHalignment(HPos.CENTER);
		repeatColumn.setPercentWidth(50);
		this.buttonPanel.getColumnConstraints().add(repeatColumn);
		ColumnConstraints nextColumn = new ColumnConstraints();
		nextColumn.setHgrow(Priority.ALWAYS);
		nextColumn.setHalignment(HPos.RIGHT);
		nextColumn.setPercentWidth(25);
		this.buttonPanel.getColumnConstraints().add(nextColumn);

		// TODO disable until survey created
		Separator separator = new Separator();
		this.buttonPanel.add(separator, 0, 0, 3, 1);
		Label questionLabel = new Label();
		questionLabel.setTextAlignment(TextAlignment.CENTER);
		questionLabel.minWidth(100);
		questionLabel.textProperty().bind(this.questionTextProperty);
		this.buttonPanel.add(questionLabel, 0, 1);
		this.nextScenario = new Button("Start Survey");
		this.buttonPanel.add(this.nextScenario, 2, 1);
		this.nextScenario.setOnAction((event) ->
		{
			nextScenario();
		});

		TextFlow initialText = new TextFlow();
		initialText.setTextAlignment(TextAlignment.CENTER);
		initialText.setPadding(new Insets(10, 10, 10, 10));
		Text text1 = new Text("Thank you for participating in the WAVE survey! \n\n");
		initialText.getChildren().addAll(text1);
		Text text2 = new Text(
				"In this experiment, you will be asked several questions related to the audio design of WAVE. ");
		initialText.getChildren().addAll(text2);
		Text text3 = new Text(
				"The questions mainly consist of listening to various weather scenarios, and selecting an answer based upon the perceived audio. ");
		initialText.getChildren().addAll(text3);
		Text text4 = new Text("You must select an answer before proceeding to the next scenario. ");
		initialText.getChildren().addAll(text4);
		Text text5 = new Text(
				"The survey will take approximately 15 minutes to complete, but you may leave at any time. \n\n");
		initialText.getChildren().addAll(text5);
		Text text6 = new Text("Click ");
		initialText.getChildren().addAll(text6);
		Text text7 = new Text("Start Survey ");
		text7.setStyle("-fx-font-weight: bold");
		initialText.getChildren().addAll(text7);
		Text text8 = new Text("when you are ready to proceed. ");
		initialText.getChildren().addAll(text8);
		this.setCenter(initialText);
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
			this.buttonPanel.add(repeatSound, 1, 1);
		}
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