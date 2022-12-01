package de.darkestnoir.bcm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageCache {
	private static final class LruCache<A, B> extends LinkedHashMap<A, B> {
		private final int maxEntries;

		public LruCache(final int maxEntries) {
			super(maxEntries + 1, 1.0f, true);
			this.maxEntries = maxEntries;
		}

		@Override
		protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
			return super.size() > maxEntries;
		}
	}

	private static final int IMAGE_CACHE_SIZE = 1000;

	private static final ImageCache instance = new ImageCache();

	public static ImageCache getInstance() {
		return instance;
	}

	public static byte[] toByteArray(BufferedImage bi, String format) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		byte[] bytes = baos.toByteArray();
		return bytes;

	}

	private final Map<String, Image> imageCache = new LruCache<>(IMAGE_CACHE_SIZE);
	private final Map<String, String> base64ImageCache = new HashMap<>();

	public Image get(String url) {
		if (!imageCache.containsKey(url)) {
			if (base64ImageCache.containsKey(url)) {
				try {
					return imageCache.put(url, getImageForBase64String(base64ImageCache.get(url)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Image localImage = new Image(url);
			if (!localImage.isError()) {
				imageCache.put(url, localImage);

				try {
					base64ImageCache.put(url, getBase64StringForImage(url));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				return new Image("icons/cloud_off.png");
			}
		}
		return imageCache.get(url);
	}

	public Map<String, String> getBase64ImageCache() {
		return base64ImageCache;
	}

	public String getBase64StringForImage(String url) throws IOException {
		return Base64.getEncoder().encodeToString(toByteArray(getBufferedImage(url), "png"));
	}

	private BufferedImage getBufferedImage(String url) {
		return SwingFXUtils.fromFXImage(get(url), null);
	}

	public Map<String, Image> getImageCache() {
		return imageCache;
	}

	public Image getImageForBase64String(String base64String) throws IOException {
		byte[] byteData = Base64.getDecoder().decode(base64String.getBytes());
		return new Image(new ByteArrayInputStream(byteData));
	}

}