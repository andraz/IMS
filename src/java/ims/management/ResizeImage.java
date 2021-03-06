/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.management;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *This class provides static methods for resizing of images
 * @author andrazhribernik
 */
public class ResizeImage {
    /**
     * Return resized originalImage.
     * @param originalImage Image that we would like to resize
     * @param type type of resized image (i.e. png, jpg, etc.)
     * @param targetWidth width of resized image
     * @return 
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int type, int targetWidth){
        double ratio = targetWidth / (double)originalImage.getWidth();
        int targetHeight = ((Double)(ratio * originalImage.getHeight())).intValue();
	BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	g.dispose();
 
	return resizedImage;
    }
}
