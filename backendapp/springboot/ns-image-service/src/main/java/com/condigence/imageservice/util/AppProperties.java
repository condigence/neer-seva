package com.condigence.imageservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties("app")
public class AppProperties {

    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);

	private String location;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "AppProperties [location=" + location + "]";
	}

	/**
	 * Returns a normalized, resolved location path for images.
	 * Resolution order:
	 * 1) app.location (bound from properties) - preferred
	 * 2) APP_LOCATION env var
	 * 3) app.base.path system property (appended with /neerseva-images)
	 * 4) null (no fallback hard-coded)
	 * The returned path always uses forward slashes and has no trailing slash
	 */
	public String getResolvedLocation() {
		// 1) prefer bound property from application.yml
		if (this.location != null && !this.location.isBlank()) {
			return normalize(this.location);
		}

		// 2) env var
		String resolved = System.getenv("APP_LOCATION");
		if (resolved != null && !resolved.isBlank()) {
			return normalize(resolved);
		}

		// 3) system property base path
		String base = System.getProperty("app.base.path");
		if (base != null && !base.isBlank()) {
			String candidate = base.replace('\\', '/') + "/neerseva-images";
			return normalize(candidate);
		}

		// 4) no hard-coded fallback; return null to indicate not configured
		return null;
	}

	private String normalize(String raw) {
		String resolved = raw.replace('\\', '/');
		// Remove any trailing slash
		while (resolved.endsWith("/")) {
			resolved = resolved.substring(0, resolved.length() - 1);
		}
		return resolved;
	}

	@PostConstruct
	private void postConstruct() {
		String resolved = getResolvedLocation();
		if (resolved == null || resolved.isBlank()) {
			logger.warn("app.location is not configured. Set app.location in application.yml or APP_LOCATION env var or -Dapp.base.path.");
		} else {
			logger.info("Resolved app.location={}", resolved);
		}
	}

}
