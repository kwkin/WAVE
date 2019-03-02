package wave;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wave.infrastructure.WaveSession;
import wave.views.WaveWindow;

public class WaveApp extends Application
{
	private static final String APP_NAME = "Weather Auditory and Visual Environment";
	private static Stage primaryStage;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	public static Stage getStage()
	{
		return WaveApp.primaryStage;
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		WaveApp.primaryStage = primaryStage;
		WaveApp.primaryStage.setTitle(APP_NAME);
		WaveSession session = new WaveSession();
		WaveWindow window = new WaveWindow(session);
		WaveApp.primaryStage.setScene(new Scene(window, 1280, 720));		
		WaveApp.primaryStage.setOnCloseRequest(event ->
		{
			session.shutdown();
		});
		WaveApp.primaryStage.show();
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
