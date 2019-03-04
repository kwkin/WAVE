package wave.infrastructure.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GLAnimatorControl;

import com.jogamp.opengl.util.FPSAnimator;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import wave.infrastructure.WaveSession;

public class GlobeSpinAnimation implements RenderingListener, MouseListener, MouseWheelListener, KeyListener
{
	protected final BooleanProperty isAnimatingProperty;
	protected WaveSession session;
	protected GLAnimatorControl animator;
	protected double rotationDegreesPerSecond;
	protected long lastTime;

	private WorldWindow eventSource;

	public GlobeSpinAnimation(WaveSession session, double animationSpeed)
	{
		this.session = session;
		this.rotationDegreesPerSecond = animationSpeed;
		this.isAnimatingProperty = new SimpleBooleanProperty();
		this.lastTime = System.currentTimeMillis();
		this.animator = new FPSAnimator((WorldWindowGLJPanel) session.getWorldWindow(), 60);
		this.animator.start();
		this.setIsAnimating(true);
	}

	public void setEventSource(WorldWindow newEventSource)
	{
		if (this.eventSource != null)
		{
			this.eventSource.removeRenderingListener(this);
		}
		if (newEventSource != null)
		{
			newEventSource.addRenderingListener(this);
		}
		this.eventSource = newEventSource;
	}

	public boolean isAnimating()
	{
		return this.isAnimatingProperty.getValue();
	}

	public void setIsAnimating(boolean isAnimating)
	{
		this.isAnimatingProperty.setValue(isAnimating);
		WorldWindowGLJPanel panel = (WorldWindowGLJPanel) session.getWorldWindow();
		if (isAnimating)
		{
			this.animator.start();
			panel.addMouseListener(this);
			panel.addMouseWheelListener(this);
			panel.addKeyListener(this);
		}
		else
		{
			this.animator.stop();
			this.session.getWorldWindow().getView().stopAnimations();
			panel.removeMouseListener(this);
			panel.removeMouseWheelListener(this);
			panel.removeKeyListener(this);
		}
	}

	public BooleanProperty isAnimatingProperty()
	{
		return this.isAnimatingProperty;
	}

	@Override
	public void stageChanged(RenderingEvent event)
	{
		if (!event.getStage().equals(RenderingEvent.BEFORE_RENDERING)
				|| this.session.getModel().getGlobe() == null
				|| !this.isAnimating()
				|| this.session.getWorldWindow() == null)
		{
			return;
		}
		View currentView = this.session.getWorldWindow().getView();
		if (currentView.getEyePosition() == null)
		{
			return;
		}
		currentView.getViewInputHandler();
		long now = System.currentTimeMillis();
		double secondsElapsed = (now - this.lastTime) * 1.0e-3;
		;
		double degreesToRotate = this.rotationDegreesPerSecond * secondsElapsed;
		this.lastTime = now;

		double latitude = currentView.getEyePosition().getLatitude().degrees;
		double longitude = Angle
				.normalizedDegreesLongitude(currentView.getEyePosition().getLongitude().degrees + degreesToRotate);
		double altitude = currentView.getEyePosition().getAltitude();

		Position newPosition = Position.fromDegrees(latitude, longitude, altitude);
		currentView.stopAnimations();
		currentView.setEyePosition(newPosition);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.setIsAnimating(false);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.setIsAnimating(false);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		this.setIsAnimating(false);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		this.setIsAnimating(false);
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		this.setIsAnimating(false);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		this.setIsAnimating(false);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		this.setIsAnimating(false);
	}
}
