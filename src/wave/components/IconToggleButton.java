package wave.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class IconToggleButton extends ToggleButton
{

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
		this.images.getChildren().add(this.selectedImage);
		if (this.isSelected())
		{
			this.selectedImage.setVisible(true);
			this.unSelectedImage.setVisible(false);
		}
		else
		{
			this.selectedImage.setVisible(false);
			this.unSelectedImage.setVisible(true);
		}
		this.setIconSize(32, 32);
		this.setMaxSize(32, 32);
		this.setGraphic(this.images);
		this.selectedProperty().addListener(new IconChangeListener());
	}

	protected class IconChangeListener implements ChangeListener<Boolean>
	{

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
		{
			if (newValue)
			{
				selectedImage.setVisible(true);
				unSelectedImage.setVisible(false);
			}
			else
			{
				selectedImage.setVisible(false);
				unSelectedImage.setVisible(true);
			}
		}
	}
}
