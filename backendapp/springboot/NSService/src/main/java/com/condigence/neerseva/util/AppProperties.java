package com.condigence.neerseva.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

	

}
