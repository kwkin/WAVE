package wave.views.windows;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import wave.WaveApp;
import wave.audio.AudioFiles;
import wave.audio.SurveySounds;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.FXThemeLoader;

public class PlaybackWindow extends Stage
{
	protected final static String TITLE = "Audio Playback";
	protected final static int WINDOW_WIDTH = 560;
	protected final static int WINDOW_HEIGHT = 700;

	private final ComboBox<AudioFiles> audioFileComboBox;
	private final StringProperty degreeProperty;
	private final static int MIN_DEGREE = -179;
	private final static int MAX_DEGREE = 180;
	private final Slider degreeSlider;
	private SurveySounds currentSound;

	public PlaybackWindow()
	{
		this.setResizable(false);
		this.setTitle(TITLE);
		this.setX(WaveApp.getStage().getX() + 200);
		this.setY(WaveApp.getStage().getY() + 100);
		BorderPane border = new BorderPane();
		Image icon;
		try
		{
			icon = new Image(Wave.MAIN_ICON.toUri().toURL().toString());
			ImageView image = new ImageView(icon);
			image.setFitWidth(icon.getWidth());
			image.setFitHeight(icon.getHeight());
			image.setPreserveRatio(true);
			border.setCenter(image);
		}
		catch (MalformedURLException e)
		{

		}
		this.audioFileComboBox = new ComboBox<AudioFiles>();
		this.audioFileComboBox.getItems().addAll(AudioFiles.values());
		this.audioFileComboBox.setValue(AudioFiles.WIND_MEDIUM);
		border.setTop(this.audioFileComboBox);

		this.degreeProperty = new SimpleStringProperty();

		GridPane compassGrid = new GridPane();
		compassGrid.setPadding(new Insets(5, 5, 5, 5));
		compassGrid.setHgap(5);
		compassGrid.setVgap(5);
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
		border.setCenter(compassGrid);
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

		int gridRow = 0;
		GridPane controlsGrid = new GridPane();
		controlsGrid.setHgap(10);
		controlsGrid.setVgap(10);
		controlsGrid.setAlignment(Pos.CENTER);
		controlsGrid.setMinWidth(196);
		border.setBottom(controlsGrid);
		Label degreeLabel = new Label("Degree");
		degreeLabel.setTextAlignment(TextAlignment.RIGHT);
		controlsGrid.add(degreeLabel, 0, gridRow);

		this.degreeSlider = new Slider();
		this.degreeSlider.setMin(PlaybackWindow.MIN_DEGREE);
		this.degreeSlider.setMax(PlaybackWindow.MAX_DEGREE);
		this.degreeSlider.setBlockIncrement(1);
		this.degreeSlider.setMajorTickUnit(1);
		this.degreeSlider.setMinorTickCount(0);
		this.degreeSlider.setSnapToTicks(true);
		this.degreeSlider.setMaxWidth(Double.MAX_VALUE);
		Bindings.bindBidirectional(this.degreeProperty, this.degreeSlider.valueProperty(), new NumberStringConverter());
		controlsGrid.add(this.degreeSlider, 1, gridRow);
		gridRow++;

		Spinner<Integer> spinner = new Spinner<Integer>();
		spinner.setEditable(true);
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
				PlaybackWindow.MIN_DEGREE, PlaybackWindow.MAX_DEGREE, (int) this.degreeSlider.getValue());
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
		spinner.focusedProperty().addListener((observable, oldValue, newValue) ->
		{
			if (!newValue)
			{
				spinner.increment(0);
			}
		});
		spinner.setValueFactory(valueFactory);
		controlsGrid.add(spinner, 1, gridRow);
		gridRow++;

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
		Button playButton = new Button("Play Sound");
		playButton.setMaxWidth(Double.MAX_VALUE);
		playButton.setOnAction((action) -> 
		{
			playAudio();
		});
		controlsGrid.add(playButton, 1, gridRow);
		gridRow++;

		Scene surveyScene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setScene(surveyScene);
		FXThemeLoader.applyDefaultTheme(this);
	}
	
	private void playAudio()
	{
		Path currentAudio = this.audioFileComboBox.getValue().getPath();
		double direction = this.degreeSlider.getValue();
		if (this.currentSound != null)
		{
			this.currentSound.stopAudio();
		}
		this.currentSound = new SurveySounds((int)direction, 0, currentAudio);
		this.currentSound.playAudio();
	}
}
