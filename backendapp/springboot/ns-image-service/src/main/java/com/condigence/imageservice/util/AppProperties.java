package com.condigence.imageservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties("app")
public class AppProperties {

	@Autowired
	private Environment env;

	private String location;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProperty(String pPropertyKey) {
        return env.getProperty(pPropertyKey);
    }

	@Override
	public String toString() {
		return "AppProperties [env=" + env + ", location=" + location + "]";
	}

	/**
	 * Returns a normalized, resolved location path for images.
	 * Resolution order:
	 * 1) APP_LOCATION env var
	 * 2) app.location (bound from properties)
	 * 3) app.base.path system property
	 * 4) literal default
	 * The returned path always uses forward slashes and ends with /neerseva-images
	 */
	public String getResolvedLocation() {
		String resolved = null;
		// 1) env var
		resolved = System.getenv("APP_LOCATION");
		if (resolved == null || resolved.isBlank()) {
			// 2) bound property
			if (this.location != null && !this.location.isBlank()) {
				resolved = this.location;
			}
		}
		if (resolved == null || resolved.isBlank()) {
			// 3) system property
			resolved = System.getProperty("app.base.path");
		}
		if (resolved == null || resolved.isBlank()) {
			// 4) fallback default — keep forward-slash style
			resolved = "D://gitrepo//neer-seva//backendapp//springboot//ns-image-service/neerseva-images";
		}

		// Normalize separators to forward slash
		resolved = resolved.replace('\\', '/');

		// Remove any trailing slash
		while (resolved.endsWith("/")) {
			resolved = resolved.substring(0, resolved.length() - 1);
		}

		return resolved;
	}

	@PostConstruct
	private void init() {
		// Ensure location is set to something usable for other components that read the raw property
		if ((this.location == null || this.location.isBlank()) && System.getenv("APP_LOCATION") != null) {
			this.location = System.getenv("APP_LOCATION");
		}
	}

}
