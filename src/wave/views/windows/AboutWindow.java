package wave.views.windows;

import java.net.MalformedURLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import wave.WaveApp;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.FXThemeLoader;

public class AboutWindow extends Stage
{
	protected final static String TITLE = "About WAVE";
	protected final static int WINDOW_WIDTH = 560;
	protected final static int WINDOW_HEIGHT = 560;
	
	public AboutWindow()
	{
		this.setResizable(false);
		this.setTitle(TITLE);
		this.setX(WaveApp.getStage().getX() + 200);
		this.setY(WaveApp.getStage().getY() + 100);

		BorderPane border = new BorderPane();
		border.setPadding(new Insets(10, 10, 10, 10));
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

		BorderPane borderText = new BorderPane();
		border.setBottom(borderText);
		StringBuilder waveText = new StringBuilder("Weather Auditory and Visual Environment Version ");
		waveText.append(Wave.VERSION);
		waveText.append("\n\n");
		waveText.append("WAVE is developed by Eric Goicochea, Bridget Homer, and Kenneth King for COP4930/6930, 3D Audio Interfaces at UF.\n\n");
		Label waveTextFlow = new Label(waveText.toString());
		waveTextFlow.setTextAlignment(TextAlignment.CENTER);
		waveTextFlow.setPadding(new Insets(10, 10, 10, 10));
		waveTextFlow.setWrapText(true);
		borderText.setCenter(waveTextFlow);

		StringBuilder dataText = new StringBuilder("The humidity and land temperature datasets are provided by Nasa Earth Observations (NEO). The rain dataset is provided by Nasa Precipitation Measurment Missions (PMM). The wind dataset is converted from the National Centers for Environmental Information (NOAA).");
		Label dataTextFlow = new Label(dataText.toString());
		Font defaultFont = dataTextFlow.getFont();
		Font dataTextFont = new Font(defaultFont.getFamily(), 8);
		dataTextFlow.setFont(dataTextFont);
		dataTextFlow.setTextAlignment(TextAlignment.CENTER);
		dataTextFlow.setPadding(new Insets(10, 10, 10, 10));
		dataTextFlow.setWrapText(true);
		borderText.setBottom(dataTextFlow);
		
		Scene surveyScene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setScene(surveyScene);
		FXThemeLoader.applyDefaultTheme(this);
	}
}
