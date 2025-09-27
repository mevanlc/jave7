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

    public ImageProvider(String rootPath) {
        Ensure.ensureNotNull("RootPath is null.", rootPath);
        ClassLoader cl = this.getClass().getClassLoader();
        System.err.println("ClassLoader of ImageProvider: " + cl + " (" + cl.getClass().getName() + ")");
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
        InputStream inputStream = this.getInputStream(relativePath);
        try {
            InputStream teeStream = IOUtilities.tmpTee(inputStream, relativePath);
            return this.loadImage(isAnimated, teeStream);
        } finally {
            IOUtilities.close(inputStream);
        }
    }

    private Image loadImage(boolean isAnimated, InputStream inputStream) {
        try {
            return isAnimated ? ImageLoader.getImageWithoutCaching(inputStream) : ImageLoader.getMemoryImageWithoutCaching(inputStream);
        } catch (IOException var4) {
            throw new ImageLoadingException("Cannot open image: " + var4.getMessage(), var4);
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
