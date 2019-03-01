package wave;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wave.models.WaveSession;
import wave.views.WaveWindow;

public class Wave extends Application
{
	private static final String APP_NAME = "Weather Auditory and Visual Environment";
	private static Stage primaryStage;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	public static Stage getStage()
	{
		return Wave.primaryStage;
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		Wave.primaryStage = primaryStage;
		Wave.primaryStage.setTitle(APP_NAME);
		WaveSession session = new WaveSession();
		WaveWindow window = new WaveWindow(session);
		Wave.primaryStage.setScene(new Scene(window, 1280, 720));

//		Path darkThemeCss = Paths.get("data", "css", "modena_dark.css");
//		this.loadTheme(scene, darkThemeCss);
		
		Wave.primaryStage.setOnCloseRequest(event ->
		{
			session.shutdown();
		});
		Wave.primaryStage.show();
	}
	
	public void loadTheme(Path stylesheet)
	{
		try
		{
			Scene scene = getStage().getScene();
			if (Files.exists(stylesheet))
			{
				scene.getStylesheets().clear();
				scene.getStylesheets().add("file:///" + stylesheet.toAbsolutePath().toString().replace("\\", "/"));
			}
		}
		catch(Exception e)
		{
			
		}
	}

	static
	{
		WorldWind.setOfflineMode(true);
		System.setProperty("sun.awt.noerasebackground", "true");
		Path configFile = Paths.get("data", "config", "wave.xml");
		if (Files.exists(configFile))
		{
			Configuration.insertConfigurationDocument(configFile.toAbsolutePath().toString());
		}
	}
}
