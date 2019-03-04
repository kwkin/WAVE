package wave.infrastructure.handlers;

import java.util.Optional;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ConfirmCloseEventHandler implements EventHandler<WindowEvent>
{
	protected Stage parent;
	protected String dialogText = "Are you sure you want to exit?";
	protected String confirmText = "Exit";
	protected String cancelText = "Cancel";
	protected boolean shutdownOnClose;

	public ConfirmCloseEventHandler(Stage parent)
	{
		this.parent = parent;
		this.shutdownOnClose = false;
	}

	public void setDialogText(String dialogText)
	{
		this.dialogText = dialogText;
	}

	public String getDialogText()
	{
		return this.confirmText;
	}

	public void setConfirmText(String confirmText)
	{
		this.confirmText = confirmText;
	}

	public String getConfirmText()
	{
		return this.confirmText;
	}

	public void setCancelText(String cancelText)
	{
		this.cancelText = cancelText;
	}

	public String getCancelText()
	{
		return this.cancelText;
	}
	
	public void shutdownOnClose(boolean shutdownOnClose)
	{
		this.shutdownOnClose = shutdownOnClose;
	}

	@Override
	public void handle(WindowEvent event)
	{
		Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
		closeConfirmation.setContentText(this.dialogText);
		closeConfirmation.setHeaderText("Confirm Exit");
		closeConfirmation.initModality(Modality.APPLICATION_MODAL);
		closeConfirmation.initOwner(this.parent);

		// Set position of the window
		double xPosition = this.parent.getX() + this.parent.getWidth() / 2 - closeConfirmation.getWidth() / 2;
		closeConfirmation.setX(xPosition);
		double yPosition = this.parent.getY() + this.parent.getHeight() / 2 - closeConfirmation.getHeight() / 2;
		closeConfirmation.setY(yPosition);

		// Change the text of the ok button
		Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText(this.confirmText);

		// Change the text of the cancel button
		Button cancelButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
		cancelButton.setText(this.cancelText);

		Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
		if (!ButtonType.OK.equals(closeResponse.get()))
		{
			event.consume();
		}
		if (this.shutdownOnClose)
		{
			Platform.exit();
		}
	}

}
