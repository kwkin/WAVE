package wave.views.windows.survey;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import wave.audio.SurveySounds;
import wave.infrastructure.survey.RatingQuestion;
import wave.infrastructure.survey.SurveyQuestion;

public class RatingSurveyPanel extends BorderPane implements QuestionPanel
{
	private final BooleanProperty isAnswerSelectedProperty;
	private final BooleanProperty isSoundPlayedProperty;
	
	private final Label questionLabel;
	private ToggleGroup toggleGroup;
	private SurveySounds currentClip;

	public RatingSurveyPanel(SurveyQuestion question)
	{
		RatingQuestion scenario = (RatingQuestion)question;
		
		this.isAnswerSelectedProperty = new SimpleBooleanProperty();
		this.isSoundPlayedProperty = new SimpleBooleanProperty();

		BorderPane border = new BorderPane();
		this.setTop(border);
		Label selectLabel = new Label("Play the sound to proceed.");
		border.setTop(selectLabel);

		Button soundButton = new Button("Play Sound");
		scenario.setRepeat(0);
		soundButton.setOnAction(value -> 
		{
			scenario.getSound().playAudio();
			if (this.currentClip != null)
			{
				this.currentClip.stopAudio();
			}
			this.currentClip = scenario.getSound();
			int repeated = scenario.getRepeat() + 1;
			scenario.setRepeat(repeated);
			this.isSoundPlayedProperty.setValue(true);
		});
		soundButton.setMaxWidth(Double.MAX_VALUE);
		border.setCenter(soundButton);
		
		this.setPadding(new Insets(10, 10, 10, 10));
		this.questionLabel = new Label(scenario.getQuestion());
		this.questionLabel.setWrapText(true);
		this.questionLabel.setPadding(new Insets(5, 5, 10, 5));
		border.setBottom(this.questionLabel);

		GridPane ratingPane = new GridPane();
		ratingPane.disableProperty().bind(this.isSoundPlayedProperty.not());
		ratingPane.setPadding(new Insets(5, 5, 5, 5));
		ratingPane.setHgap(5);
		ratingPane.setVgap(8);
		this.toggleGroup = new ToggleGroup();
		this.toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
		{
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
			{
				isAnswerSelectedProperty.setValue(true);
			}
		});
		RadioButton rating1 = new RadioButton("1 - Completely unrealistic");
		rating1.setToggleGroup(this.toggleGroup);
		ratingPane.add(rating1, 0, 0);
		RadioButton rating2 = new RadioButton("2 - Unrealistic");
		rating2.setToggleGroup(this.toggleGroup);
		ratingPane.add(rating2, 0, 1);
		RadioButton rating3 = new RadioButton("3 - Neutral");
		rating3.setToggleGroup(this.toggleGroup);
		ratingPane.add(rating3, 0, 2);
		RadioButton rating4 = new RadioButton("4 - Realistic");
		rating4.setToggleGroup(this.toggleGroup);
		ratingPane.add(rating4, 0, 3);
		RadioButton rating5 = new RadioButton("5 - Very Realistic");
		rating5.setToggleGroup(this.toggleGroup);
		ratingPane.add(rating5, 0, 4);
		this.setCenter(ratingPane);
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
		this.currentClip.stopAudio();
	}
}
