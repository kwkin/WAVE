package wave.views.windows.survey;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import wave.audio.SurveySounds;
import wave.infrastructure.core.Wave;
import wave.infrastructure.survey.DirectionQuestion;
import wave.infrastructure.survey.SurveyQuestion;

public class DirectionSurveyPanel extends BorderPane implements QuestionPanel
{
	private final BooleanProperty isAnswerSelectedProperty;
	private final BooleanProperty isSoundPlayedProperty;
	
	private final StringProperty degreeProperty;
	private final static int MIN_DEGREE = -179;
	private final static int MAX_DEGREE = 180;

	private final Label questionLabel;
	private final Slider degreeSlider;
	private SurveySounds currentClip;

	public DirectionSurveyPanel(SurveyQuestion question)
	{
		DirectionQuestion scenario = (DirectionQuestion)question;

		this.isAnswerSelectedProperty = new SimpleBooleanProperty();
		this.isSoundPlayedProperty = new SimpleBooleanProperty();
		this.degreeProperty = new SimpleStringProperty();
		
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
		this.questionLabel.disableProperty().bind(this.isSoundPlayedProperty.not());
		border.setBottom(this.questionLabel);

		GridPane compassGrid = new GridPane();
		compassGrid.setPadding(new Insets(5, 5, 5, 5));
		compassGrid.setHgap(5);
		compassGrid.setVgap(5);
		compassGrid.disableProperty().bind(this.isSoundPlayedProperty.not());
		try
		{
			BorderPane computerBorder = new BorderPane();
			Path computerIcon = Paths.get("data", "icons", "computer.png");
			Image computerImage = new Image(computerIcon.toUri().toURL().toString());
			ImageView computerImageView = new ImageView(computerImage);
			computerImageView.setSmooth(true);
			computerImageView.maxWidth(16);
			computerImageView.maxHeight(16);
			computerImageView.setScaleX(0.5);
			computerImageView.setScaleY(0.5);
			compassGrid.setAlignment(Pos.BOTTOM_CENTER);
			computerBorder.setCenter(computerImageView);
			compassGrid.add(computerBorder, 1, 0);
		}
		catch (MalformedURLException e1)
		{

		}
		this.setCenter(compassGrid);
		compassGrid.setAlignment(Pos.CENTER);
		Label degree0 = new Label("0");
		degree0.setTextAlignment(TextAlignment.CENTER);
		degree0.setAlignment(Pos.BOTTOM_CENTER);
		degree0.setMaxWidth(Double.MAX_VALUE);
		compassGrid.add(degree0, 1, 1);
		BorderPane.setAlignment(degree0, Pos.BOTTOM_LEFT);
		Label degree90 = new Label("90");
		degree90.setTextAlignment(TextAlignment.LEFT);
		degree90.setAlignment(Pos.CENTER_LEFT);
		degree90.setMaxWidth(Double.MAX_VALUE);
		degree90.setMaxHeight(Double.MAX_VALUE);
		compassGrid.add(degree90, 2, 2);
		Label degree180 = new Label("180");
		degree180.setTextAlignment(TextAlignment.CENTER);
		degree180.setAlignment(Pos.CENTER);
		degree180.setMaxWidth(Double.MAX_VALUE);
		compassGrid.add(degree180, 1, 3);
		Label degree270 = new Label("-90");
		degree270.setTextAlignment(TextAlignment.RIGHT);
		degree270.setAlignment(Pos.CENTER_RIGHT);
		degree270.setMaxWidth(Double.MAX_VALUE);
		degree270.setMaxHeight(Double.MAX_VALUE);
		compassGrid.add(degree270, 0, 2);

		GridPane controlsGrid = new GridPane();
		controlsGrid.disableProperty().bind(this.isSoundPlayedProperty.not());
		controlsGrid.setAlignment(Pos.CENTER);
		controlsGrid.setMinWidth(196);
		this.setBottom(controlsGrid);
		Label degreeLabel = new Label("Degree");
		degreeLabel.setTextAlignment(TextAlignment.RIGHT);
		controlsGrid.add(degreeLabel, 0, 0);

		this.degreeSlider = new Slider();
		this.degreeSlider.setMin(DirectionSurveyPanel.MIN_DEGREE);
		this.degreeSlider.setMax(DirectionSurveyPanel.MAX_DEGREE);
		this.degreeSlider.setBlockIncrement(1);
		this.degreeSlider.setMajorTickUnit(1);
		this.degreeSlider.setMinorTickCount(0);
		this.degreeSlider.setSnapToTicks(true);
		this.degreeSlider.setMaxWidth(Double.MAX_VALUE);
		Bindings.bindBidirectional(this.degreeProperty, this.degreeSlider.valueProperty(), new NumberStringConverter());
		controlsGrid.add(this.degreeSlider, 1, 0);

		Spinner<Integer> spinner = new Spinner<Integer>();
		spinner.setEditable(true);
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
				DirectionSurveyPanel.MIN_DEGREE, DirectionSurveyPanel.MAX_DEGREE, (int) this.degreeSlider.getValue());
		spinner.setMaxWidth(Double.MAX_VALUE);
		Bindings.bindBidirectional(this.degreeProperty, valueFactory.valueProperty(), new StringConverter<Integer>()
		{
			@Override
			public String toString(Integer integer)
			{
				return integer.toString();
			}

			@Override
			public Integer fromString(String string)
			{
				return (int) Math.round(Double.parseDouble(string));
			}
		});

		// Commit to property when spinner loses focus
		spinner.focusedProperty().addListener((observable, oldValue, newValue) ->
		{
			if (!newValue)
			{
				spinner.increment(0);
			}
		});
		spinner.setValueFactory(valueFactory);
		controlsGrid.add(spinner, 1, 1);

		this.degreeProperty.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				isAnswerSelectedProperty.setValue(true);
			}
		});

		try
		{
			Image compassImage = new Image(Wave.COMPASS_ICON.toUri().toURL().toString());
			ImageView compassImageView = new ImageView(compassImage);
			compassImageView.setPreserveRatio(true);
			compassImageView.rotateProperty().bindBidirectional(this.degreeSlider.valueProperty());
			compassGrid.add(compassImageView, 1, 2);
		}
		catch (MalformedURLException e)
		{

		}
	}

	@Override
	public String getQuestion()
	{
		return this.questionLabel.getText();
	}

	@Override
	public String getAnswer()
	{
		return this.degreeProperty.getValue();
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
