package wave.views;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.BorderPane;
import wave.models.WaveSession;

public class WaveWindow extends BorderPane
{
	public WaveWindow(WaveSession session)
	{		
		SwingNode swingNode = new SwingNode();
		SwingUtilities.invokeLater(() ->
		{
			swingNode.setContent((JComponent) session.getWorldWindow());
		});
		this.setCenter(swingNode);
		
		StatusBar statusBar = new StatusBar();
		statusBar.setEventSource(session.getWorldWindow());
		this.setBottom(statusBar);
	}
}
