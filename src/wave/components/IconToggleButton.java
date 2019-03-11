package wave.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconToggleButton extends ToggleButton
{
	protected ImageView selectedImage;
	protected ImageView unSelectedImage;
	
	public IconToggleButton(Image selectedImage, Image unSelectedImage)
	{
		super();
		this.selectedImage = new ImageView(selectedImage);
		this.selectedImage.setPreserveRatio(true);
		this.unSelectedImage = new ImageView(unSelectedImage);
		this.unSelectedImage.setPreserveRatio(true);
		this.setIconSize(128, 128);
		this.setGraphic(this.unSelectedImage);
		this.selectedProperty().addListener(new IconChangeListener());
	}
	
	public IconToggleButton(String text, Image selectedImage, Image unSelectedImage)
	{
		super(text);
		this.selectedImage = new ImageView(selectedImage);
		this.selectedImage.setPreserveRatio(true);
		this.unSelectedImage = new ImageView(unSelectedImage);
		this.unSelectedImage.setPreserveRatio(true);
		this.setIconSize(128, 128);
		this.setGraphic(this.unSelectedImage);
		this.selectedProperty().addListener(new IconChangeListener());
	}
	
	public void setIconSize(int width, int height)
	{
		this.selectedImage.setFitHeight(width);
		this.selectedImage.setFitHeight(height);
		this.unSelectedImage.setFitHeight(width);
		this.unSelectedImage.setFitHeight(height);
	}
	
	protected class IconChangeListener implements ChangeListener<Boolean>
	{
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
		{
			if (newValue)
			{
				setGraphic(selectedImage);
			}
			else
			{
				setGraphic(unSelectedImage);
			}
		}
	}
}
