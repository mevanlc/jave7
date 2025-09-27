package net.disy.commons.swing.image;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.disy.commons.core.io.IOUtilities;
import net.disy.commons.core.util.Ensure;

public class ImageProvider implements IImageProvider {
    private final String rootPath;
    private static Image fallbackImage = null;
    private static final String FALLBACK_IMAGE_PATH = "de/jave/missing_resource_fallback.gif";

    public ImageProvider(String rootPath) {
        Ensure.ensureNotNull("RootPath is null.", rootPath);
        this.rootPath = rootPath;
    }

    @Override
    public Image getImage(String relativePath) {
        return this.getImage(relativePath, false);
    }

    @Override
    public Image getAnimatedImage(String relativePath) {
        return this.getImage(relativePath, true);
    }

    private Image getImage(String relativePath, boolean isAnimated) {
        try {
            InputStream inputStream = this.getInputStream(relativePath);
            try {
//                InputStream teeStream = IOUtilities.tmpTee(inputStream, relativePath);
                return this.loadImage(isAnimated, inputStream, relativePath);
            } finally {
                IOUtilities.close(inputStream);
            }
        } catch (ImageLoadingException e) {
            // Resource not found, use fallback
            System.err.println("WARNING: Couldn't find " + this.rootPath + "/" + relativePath + " -- using ugly X fallback image.");
            return getFallbackImage();
        }
    }

    private Image getFallbackImage() {
        if (fallbackImage == null) {
            try {
                InputStream fallbackStream = this.getClass().getClassLoader().getResourceAsStream(FALLBACK_IMAGE_PATH);
                if (fallbackStream != null) {
                    fallbackImage = ImageLoader.getMemoryImageWithoutCaching(fallbackStream);
                    IOUtilities.close(fallbackStream);
                } else {
                    System.err.println("ERROR: Even the fallback image is missing! " + FALLBACK_IMAGE_PATH);
                    // Create a simple 1x1 gray pixel as last resort
                    fallbackImage = java.awt.Toolkit.getDefaultToolkit().createImage(new byte[]{(byte)128, (byte)128, (byte)128});
                }
            } catch (Exception e) {
                System.err.println("ERROR: Failed to load fallback image: " + e.getMessage());
                // Create a simple 1x1 gray pixel as last resort
                fallbackImage = java.awt.Toolkit.getDefaultToolkit().createImage(new byte[]{(byte)128, (byte)128, (byte)128});
            }
        }
        return fallbackImage;
    }

    private Image loadImage(boolean isAnimated, InputStream inputStream, String resourcePath) {
        try {
            return isAnimated ? ImageLoader.getImageWithoutCaching(inputStream) : ImageLoader.getMemoryImageWithoutCaching(inputStream);
        } catch (IOException var4) {
            throw new ImageLoadingException("Cannot open image [" + resourcePath + "]: " + var4.getMessage(), var4);
        } finally {
            IOUtilities.close(inputStream);
        }
    }

    private InputStream getInputStream(String relativePath) {
        Ensure.ensureNotNull("RelativePath to image is null.", relativePath);
        String resourceName = this.rootPath + "/" + relativePath;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new ImageLoadingException("Cannot find image resource: " + resourceName);
        } else {
            return inputStream;
        }
    }

    @Override
    public Icon getImageIcon(String relativePath) {
        Image image = this.getImage(relativePath);
        return image == null ? null : new ImageIcon(image);
    }

    @Override
    public Icon getAnimatedImageIcon(String relativePath) {
        Image image = this.getAnimatedImage(relativePath);
        return image == null ? null : new ImageIcon(image);
    }
}
