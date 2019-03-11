package wave.components;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class IconToggleButton extends ToggleButton
{
	protected static final String DEFAULT_STYLE_CLASS = "icon-button";

	protected StackPane images;
	protected ImageView selectedImage;
	protected ImageView unSelectedImage;

	public IconToggleButton(Image selectedImage, Image unSelectedImage)
	{
		super();
		initialize(selectedImage, unSelectedImage);
	}

	public IconToggleButton(String text, Image selectedImage, Image unSelectedImage)
	{
		super(text);
		initialize(selectedImage, unSelectedImage);
	}

	public void setIconSize(int width, int height)
	{
		this.selectedImage.setFitHeight(width);
		this.selectedImage.setFitHeight(height);
		this.unSelectedImage.setFitHeight(width);
		this.unSelectedImage.setFitHeight(height);
	}

	protected void initialize(Image selectedImage, Image unSelectedImage)
	{
		this.images = new StackPane();
		this.unSelectedImage = new ImageView(unSelectedImage);
		this.unSelectedImage.setPreserveRatio(true);
		this.images.getChildren().add(this.unSelectedImage);
		this.selectedImage = new ImageView(selectedImage);
		this.selectedImage.setPreserveRatio(true);
		this.selectedImage.setOpacity(0);
		this.images.getChildren().add(this.selectedImage);
		this.setIconSize(64, 64);
		this.setGraphic(this.images);
		this.selectedProperty().addListener(new IconChangeListener());
		getStyleClass().add(IconToggleButton.DEFAULT_STYLE_CLASS);
	}

	protected class IconChangeListener implements ChangeListener<Boolean>
	{
		protected final FadeTransition toSelected;
		protected final FadeTransition fromSelected;

		public IconChangeListener()
		{
			this.toSelected = new FadeTransition(Duration.millis(250), selectedImage);
			this.toSelected.setFromValue(0);
			this.toSelected.setToValue(1);
			this.toSelected.setAutoReverse(true);

			this.fromSelected = new FadeTransition(Duration.millis(100), selectedImage);
			this.fromSelected.setFromValue(1);
			this.fromSelected.setToValue(0);
			this.fromSelected.setAutoReverse(true);
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
		{
			if (newValue)
			{
				this.toSelected.play();
			}
			else
			{
				this.fromSelected.play();
			}
		}
	}
}
