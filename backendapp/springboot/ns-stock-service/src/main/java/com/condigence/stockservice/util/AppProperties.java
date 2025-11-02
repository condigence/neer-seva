package com.condigence.stockservice.util;

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
    private Image image = new Image();

    public static class Image {
        private Circuit circuit = new Circuit();

        public Circuit getCircuit() {
            return circuit;
        }

        public void setCircuit(Circuit circuit) {
            this.circuit = circuit;
        }

        public static class Circuit {
            private int failureThreshold = 5;
            private long openMillis = 60000L;
            private boolean resetOnSuccess = true;

            public int getFailureThreshold() {
                return failureThreshold;
            }

            public void setFailureThreshold(int failureThreshold) {
                this.failureThreshold = failureThreshold;
            }

            public long getOpenMillis() {
                return openMillis;
            }

            public void setOpenMillis(long openMillis) {
                this.openMillis = openMillis;
            }

            public boolean isResetOnSuccess() {
                return resetOnSuccess;
            }

            public void setResetOnSuccess(boolean resetOnSuccess) {
                this.resetOnSuccess = resetOnSuccess;
            }
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getProperty(String pPropertyKey) {
        return env.getProperty(pPropertyKey);
    }

    @Override
    public String toString() {
        return "AppProperties [env=" + env + ", location=" + location + "]";
    }

}
