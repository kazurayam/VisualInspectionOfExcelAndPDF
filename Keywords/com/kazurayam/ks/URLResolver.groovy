package com.kazurayam.ks

import java.nio.file.Path
import java.nio.file.Paths

public class URLResolver {
	
	static URL resolve(URL base, String relativeUrl) {
		try {
			if (relativeUrl.startsWith("http")) {
				return new URL(relativeUrl);
			} else {
				String protocol = base.getProtocol();
				String host = base.getHost();
				Path bp = Paths.get(base.getPath());
				Path resolved = bp.getParent().resolve(relativeUrl).normalize();
				return new URL(protocol, host, resolved.toString());
			}
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
