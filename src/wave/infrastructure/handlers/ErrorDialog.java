package wave.infrastructure.handlers;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorDialog
{
	public static void show(Stage parent, String message)
	{
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setHeaderText("Error");
		errorAlert.initModality(Modality.APPLICATION_MODAL);
		errorAlert.setContentText(message);

		if (parent != null)
		{
			errorAlert.initOwner(parent);
			double xPosition = parent.getX() + parent.getWidth() / 2 - errorAlert.getWidth() / 2;
			errorAlert.setX(xPosition);
			double yPosition = parent.getY() + parent.getHeight() / 2 - errorAlert.getHeight() / 2;
			errorAlert.setY(yPosition);
		}

		errorAlert.show();
	}
}
