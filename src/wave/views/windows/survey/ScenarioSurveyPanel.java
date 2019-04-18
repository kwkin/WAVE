package wave.views.windows.survey;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import wave.audio.SurveySounds;
import wave.infrastructure.survey.ScenarioQuestion;
import wave.infrastructure.survey.SurveyQuestion;

public class ScenarioSurveyPanel extends BorderPane implements QuestionPanel
{
	private final BooleanProperty isAnswerSelectedProperty;
	private final BooleanProperty bothSoundsPlayedProperty;
	private final Label questionLabel;
	private ToggleGroup toggleGroup;

	private int soundAPressed;
	private int soundBPressed;
	private SurveySounds currentClip;
	
	public ScenarioSurveyPanel(SurveyQuestion question)
	{
		ScenarioQuestion scenario = (ScenarioQuestion)question;
		
		this.bothSoundsPlayedProperty = new SimpleBooleanProperty();
		this.isAnswerSelectedProperty = new SimpleBooleanProperty();

		ColumnConstraints leftColumn = new ColumnConstraints();
		leftColumn.setHgrow(Priority.ALWAYS);
		leftColumn.setHalignment(HPos.CENTER);
		leftColumn.setPercentWidth(50);

		ColumnConstraints rightColumn = new ColumnConstraints();
		rightColumn.setHgrow(Priority.ALWAYS);
		rightColumn.setHalignment(HPos.CENTER);
		rightColumn.setPercentWidth(50);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.getColumnConstraints().addAll(leftColumn, rightColumn);
		this.setTop(grid);
		Label selectLabel = new Label("Select both sounds to proceed.");
		grid.add(selectLabel, 0, 0, 2, 1);
		
		Button soundAButton = new Button("Sound A");
		scenario.setRepeat(0);
		this.soundAPressed = 0;
		soundAButton.setOnAction(value -> 
		{
			scenario.getSoundA().play();
			if (this.currentClip != null)
			{
				this.currentClip.stop();
			}
			this.currentClip = scenario.getSoundA();
			this.soundAPressed++;
			int repeated = scenario.getRepeat() + 1;
			scenario.setRepeat(repeated);
			if (this.soundAPressed > 0 && this.soundBPressed > 0)
			{
				this.bothSoundsPlayedProperty.setValue(true);
			}
		});
		soundAButton.setMaxWidth(Double.MAX_VALUE);
		grid.add(soundAButton, 0, 1);
		Button soundBButton = new Button("Sound B");
		this.soundBPressed = 0;
		soundBButton.setOnAction(value -> 
		{
			scenario.getSoundB().play();
			if (this.currentClip != null)
			{
				this.currentClip.stop();
			}
			this.currentClip = scenario.getSoundB();
			this.soundBPressed++;
			int repeated = scenario.getRepeat() + 1;
			scenario.setRepeat(repeated);
			if (this.soundAPressed > 0 && this.soundBPressed > 0)
			{
				this.bothSoundsPlayedProperty.setValue(true);
			}
		});
		soundBButton.setMaxWidth(Double.MAX_VALUE);
		grid.add(soundBButton, 1, 1);
		
		this.setPadding(new Insets(10, 10, 10, 10));
		this.questionLabel = new Label(scenario.getQuestion());
		this.questionLabel.setWrapText(true);
		this.questionLabel.setPadding(new Insets(5, 5, 10, 5));
		this.questionLabel.disableProperty().bind(this.bothSoundsPlayedProperty.not());
		grid.add(this.questionLabel, 0, 2, 2, 1);

		GridPane scenarioPane = new GridPane();
		scenarioPane.setPadding(new Insets(5, 5, 5, 5));
		scenarioPane.setHgap(5);
		scenarioPane.setVgap(8);
		this.toggleGroup = new ToggleGroup();
		this.toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
		{
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
			{
				isAnswerSelectedProperty.setValue(true);
			}
		});
		RadioButton firstScenario = new RadioButton("Sound A");
		firstScenario.disableProperty().bind(this.bothSoundsPlayedProperty.not());
		firstScenario.setToggleGroup(this.toggleGroup);
		RadioButton secondScenario = new RadioButton("Sound B");
		secondScenario.disableProperty().bind(this.bothSoundsPlayedProperty.not());
		secondScenario.setToggleGroup(this.toggleGroup);
		scenarioPane.add(firstScenario, 0, 0);
		scenarioPane.add(secondScenario, 0, 1);
		this.setCenter(scenarioPane);
	}

	@Override
	public String getQuestion()
	{
		return this.questionLabel.getText();
	}

	@Override
	public String getAnswer()
	{
		RadioButton answer = (RadioButton) this.toggleGroup.getSelectedToggle();
		return answer.getText();
	}

	@Override
	public Node getNode()
	{
		return this;
	}

	@Override
	public boolean getIsAnswerSelected()
	{
		return this.isAnswerSelectedProperty.getValue();
	}

	@Override
	public BooleanProperty isAnswerSelectedProperty()
	{
		return this.isAnswerSelectedProperty;
	}

	@Override
	public void stopSound()
	{
		this.currentClip.stop();
	}
}
