package wave.views.windows;

import java.net.MalformedURLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import wave.WaveApp;
import wave.infrastructure.core.Wave;

public class AboutWindow extends Stage
{
	protected final static String TITLE = "About WAVE";
	protected final static int WINDOW_WIDTH = 480;
	protected final static int WINDOW_HEIGHT = 480;
	
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
			e.printStackTrace();
		}

		TextFlow waveTextFlow = new TextFlow();
		waveTextFlow.setTextAlignment(TextAlignment.CENTER);
		waveTextFlow.setPadding(new Insets(10, 10, 10, 10));
		Text waveText1 = new Text("Weather Auditory and Visual Environment Version " + Wave.VERSION + "\n\n");
		Text waveText2 = new Text("WAVE is an educational tool which integrates 3D audio and visuals on a virtual Earth.\n\n");
		Text waveText3 = new Text("WAVE is developed by Eric Goicochea, Bridget Homer, and Kenneth King for COP4930/6930, 3D Audio Interfaces at UF.");
		waveTextFlow.getChildren().add(waveText1);
		waveTextFlow.getChildren().add(waveText2);
		waveTextFlow.getChildren().add(waveText3);
		border.setBottom(waveTextFlow);
		
		Scene surveyScene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setScene(surveyScene);
	}
}
