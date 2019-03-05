package wave.views.windows.survey;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import wave.infrastructure.survey.SurveyScenario;

//TODO add circle graph that changes based upon the slider degree
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

		GridPane scenarioPane = new GridPane();
		scenarioPane.setPadding(new Insets(5, 5, 5, 5));
		this.setCenter(scenarioPane);

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
