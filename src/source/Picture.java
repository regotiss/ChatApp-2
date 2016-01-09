package source;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Picture extends BufferedImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Picture(int width, int height, int imageType) {
		super(width, height, imageType);
	}

	
}
