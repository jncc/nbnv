/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.importer.orglogo;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Matt Debont
 */
public class ImageWrangler {
    private String logoDir;
    private String[] knownTypes = {"png", "gif", "bmp", "jpg", "jpeg"};
    
    public ImageWrangler(String logoDir) {
        this.logoDir = logoDir;
    }
    
    public BufferedImage wrangleImage(String path, int maxHeight, int maxWidth) {
        try {
            return process(path, maxHeight, maxWidth);
        } catch (IOException ex) {
            System.err.println("Could not open image: " + path + " trying alternative file types");
            for (String type : knownTypes) {
                try {
                    BufferedImage attempt = process(path.split("\\.")[0].concat("." + type), maxHeight, maxWidth);
                    if (attempt != null) {
                        return attempt;
                    }
                } catch (IOException ext) {
                    
                }
            }
            return null;
        }
    }
    
    private BufferedImage process(String path, int maxHeight, int maxWidth) throws IOException{
        BufferedImage img = ImageIO.read(new File(logoDir + "\\" + path));
        return getScaledInstance(img, maxWidth, maxHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }
    
    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
     public BufferedImage getScaledInstance(BufferedImage img,
                                           int targetWidth,
                                           int targetHeight,
                                           Object hint)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
            BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage)img;
        int w, h;
        // Use multi-step technique: start with original size, then
        // scale down in multiple passes with drawImage()
        // until the target size is reached
        w = img.getWidth();
        h = img.getHeight();
        
        do {
            if (w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w > targetWidth && h > targetHeight);

        return ret;
    }
}
