package com.kazurayam.ks

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

public class URLDownloader {
	
	static long download(URL url, Path path) throws IOException {
		InputStream inputStream = url.openStream();
		long size = Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		return size;
	}
}
