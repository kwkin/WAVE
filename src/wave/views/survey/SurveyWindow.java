package wave.views.survey;

import java.io.IOException;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.infrastructure.models.WaveSession;
import wave.infrastructure.survey.Survey;
import wave.infrastructure.survey.SurveyScenario;

// TODO MVP design
public class SurveyWindow extends BorderPane
{
	private Survey survey;
	
	public SurveyWindow(WaveSession session)
	{
		GridPane buttonPanel = new GridPane();
		buttonPanel.setPadding(new Insets(5, 5, 5, 5));
		buttonPanel.setHgap(5);
		buttonPanel.setVgap(5);
		this.setBottom(buttonPanel);
		ColumnConstraints backColumn = new ColumnConstraints();
		backColumn.setHgrow(Priority.ALWAYS);
		backColumn.setHalignment(HPos.LEFT);
		backColumn.setPercentWidth(25);
		ColumnConstraints repeatColumn = new ColumnConstraints();
		repeatColumn.setHgrow(Priority.ALWAYS);
		repeatColumn.setHalignment(HPos.CENTER);
		backColumn.setPercentWidth(50);
		ColumnConstraints nextColumn = new ColumnConstraints();
		nextColumn.setHgrow(Priority.ALWAYS);
		nextColumn.setHalignment(HPos.RIGHT);
		backColumn.setPercentWidth(25);
		buttonPanel.getColumnConstraints().add(backColumn);
		buttonPanel.getColumnConstraints().add(repeatColumn);
		buttonPanel.getColumnConstraints().add(nextColumn);

		// TODO disable until survey created
		Separator separator = new Separator();
		buttonPanel.add(separator, 0, 0, 3, 1);
		Button repeatSound = new Button("Repeat Sound");
		buttonPanel.add(repeatSound, 1, 1);
		Button nextQuestion = new Button("Next Question");
		buttonPanel.add(nextQuestion, 2, 1);
		nextQuestion.setOnAction((event) ->
		{
			SurveyScenario nextScenario = this.survey.getNextScenario();
			SurveyPanel panel = SurveyPanel.CreatePanel(nextScenario);
			this.setCenter(panel.getNode());
		});
		
		Button startSurvey = new Button("Start Survey");
		this.setCenter(startSurvey);
		startSurvey.setOnAction((event) ->
		{
			try
			{
				this.survey = Survey.CreateSurveyFile(session);
			}
			catch (IOException e)
			{
				// TODO create error dialog
				System.out.println(e.getMessage());
			}
		});
	}
}
