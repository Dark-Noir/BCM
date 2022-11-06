package de.darkestnoir.bcm;

import java.util.LinkedHashMap;
import java.util.Map;

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

	private final Map<String, Image> imageCache = new LruCache<>(IMAGE_CACHE_SIZE);

	public void addImage(String url, Image image) {
		imageCache.put(url, image);
	}

	public Image get(String url) {
		if (!imageCache.containsKey(url)) {
			Image localImage = new Image(url);
			if (!localImage.isError()) {
				imageCache.put(url, localImage);
			} else {
				return new Image("icons/cloud_off.png");
			}
		}
		return imageCache.get(url);
	}

	public Map<String, Image> getImageCache() {
		return imageCache;
	}

}