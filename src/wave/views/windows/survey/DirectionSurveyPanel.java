package wave.views.windows.survey;

import java.net.MalformedURLException;

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
import wave.infrastructure.core.Wave;
import wave.infrastructure.survey.SurveyScenario;

public class DirectionSurveyPanel extends BorderPane implements QuestionPanel
{
	private final BooleanProperty isAnswerSelectedProperty;
	private final StringProperty degreeProperty;
	private final static int MIN_DEGREE = 0;
	private final static int MAX_DEGREE = 359;

	private final Label questionLabel;
	private final Slider degreeSlider;

	public DirectionSurveyPanel(SurveyScenario scenario)
	{
		this.isAnswerSelectedProperty = new SimpleBooleanProperty();
		this.degreeProperty = new SimpleStringProperty();

		this.setPadding(new Insets(10, 10, 10, 10));
		this.questionLabel = new Label(scenario.getQuestion());
		this.questionLabel.setPadding(new Insets(5, 5, 10, 5));
		this.setTop(this.questionLabel);

		BorderPane interfaceBorder = new BorderPane();
		BorderPane compassBorder = new BorderPane();
		interfaceBorder.setCenter(compassBorder);
		Label degree0 = new Label("0");
		degree0.setTextAlignment(TextAlignment.CENTER);
		degree0.setAlignment(Pos.CENTER);
		degree0.setMaxWidth(Double.MAX_VALUE);
		compassBorder.setTop(degree0);
		Label degree90 = new Label("90");
		degree0.setTextAlignment(TextAlignment.LEFT);
		degree90.setAlignment(Pos.CENTER_LEFT);
		degree90.setMaxWidth(Double.MAX_VALUE);
		degree90.setMaxHeight(Double.MAX_VALUE);
		compassBorder.setRight(degree90);
		Label degree180 = new Label("180");
		degree0.setTextAlignment(TextAlignment.CENTER);
		degree180.setAlignment(Pos.CENTER);
		degree180.setMaxWidth(Double.MAX_VALUE);
		compassBorder.setBottom(degree180);
		Label degree270 = new Label("270");
		degree0.setTextAlignment(TextAlignment.RIGHT);
		degree270.setAlignment(Pos.CENTER_RIGHT);
		degree270.setMaxWidth(Double.MAX_VALUE);
		degree270.setMaxHeight(Double.MAX_VALUE);
		compassBorder.setLeft(degree270);
		this.setCenter(interfaceBorder);
		
		GridPane scenarioPane = new GridPane();
		interfaceBorder.setBottom(scenarioPane);
		scenarioPane.setPadding(new Insets(5, 5, 5, 5));
		
		Label degreeLabel = new Label("Degree: ");
		scenarioPane.add(degreeLabel, 0, 0);

		this.degreeSlider = new Slider();
		this.degreeSlider.setMin(DirectionSurveyPanel.MIN_DEGREE);
		this.degreeSlider.setMax(DirectionSurveyPanel.MAX_DEGREE);
		this.degreeSlider.setBlockIncrement(1);
		this.degreeSlider.setMajorTickUnit(1);
		this.degreeSlider.setMinorTickCount(0);
		this.degreeSlider.setSnapToTicks(true);
		Bindings.bindBidirectional(this.degreeProperty, this.degreeSlider.valueProperty(), new NumberStringConverter());
		scenarioPane.add(degreeSlider, 1, 0);

		Spinner<Integer> spinner = new Spinner<Integer>();
		spinner.setEditable(true);
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
				DirectionSurveyPanel.MIN_DEGREE, DirectionSurveyPanel.MAX_DEGREE, (int) this.degreeSlider.getValue());
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
		scenarioPane.add(spinner, 2, 0);

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
			Image image = new Image(Wave.COMPASS_ICON.toUri().toURL().toString());
			ImageView compass = new ImageView(image);
			compass.rotateProperty().bindBidirectional(this.degreeSlider.valueProperty());
			compassBorder.setCenter(compass);
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
}
