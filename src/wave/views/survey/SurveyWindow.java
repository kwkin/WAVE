package wave.views.survey;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import wave.WaveApp;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.ConfirmCloseEventHandler;
import wave.infrastructure.handlers.FXThemeLoader;

public class SurveyWindow extends Stage
{
	protected final static String TITLE = "WAVE Survey";
	protected final static int WINDOW_WIDTH = 480;
	protected final static int WINDOW_HEIGHT = 480;
	
	protected final WaveSession session;

	public SurveyWindow(WaveSession session)
	{
		this.session = session;
		try
		{
			FXThemeLoader.setIcon(this, Wave.MAIN_ICON);
		}
		catch (IOException e)
		{

		}
		
		SurveyPanel panel = new SurveyPanel(session, this);
		Scene surveyScene = new Scene(panel, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setResizable(false);
		this.setTitle(TITLE);
		this.setScene(surveyScene);
		this.setX(WaveApp.getStage().getX() + 200);
		this.setY(WaveApp.getStage().getY() + 100);

		ConfirmCloseEventHandler closeHandler = new ConfirmCloseEventHandler(this);
		closeHandler.setDialogText("The survey is not complete. Are you sure you want to exit?");
		closeHandler.setConfirmText("Exit");
		closeHandler.setCancelText("Continue Survey");
		this.setOnCloseRequest(closeHandler);
	}

	public void surveyCompleted()
	{
		this.setOnCloseRequest(null);
		this.close();
	}

	@Override
	public void hide()
	{
		this.session.setIsTakingSurvey(false);
		super.hide();
	}
}
