package com.condigence.nsuserservice.dto;

import java.util.Arrays;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {

    private Long id;

    private String name;

    private String contact;

    private String email;

    private String otp;

    private String type;

    private String fullName;

    private Long imageId;

    private byte[] pic;

    private String description;

    private Long addressId;

    private boolean isActive;

    // Accept flexible JSON shapes for imageId: a number, a string, or an object like {"id":123} or {"imageId":123}
    @JsonSetter("imageId")
    public void setImageId(Object imageId) {
        if (imageId == null) {
            this.imageId = null;
            return;
        }
        try {
            if (imageId instanceof Number) {
                this.imageId = ((Number) imageId).longValue();
                return;
            }
            if (imageId instanceof String) {
                String s = (String) imageId;
                if (s.trim().isEmpty()) {
                    this.imageId = null;
                    return;
                }
                this.imageId = Long.valueOf(s);
                return;
            }
            if (imageId instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) imageId;
                Object id = null;
                if (m.containsKey("id")) id = m.get("id");
                else if (m.containsKey("imageId")) id = m.get("imageId");
                else if (m.containsKey("image_id")) id = m.get("image_id");
                if (id instanceof Number) {
                    this.imageId = ((Number) id).longValue();
                    return;
                }
                if (id instanceof String) {
                    String s = (String) id;
                    if (!s.trim().isEmpty()) {
                        this.imageId = Long.valueOf(s);
                        return;
                    }
                }
            }
            // Fallback: try to parse from toString
            String s = imageId.toString();
            if (s != null && !s.trim().isEmpty()) {
                this.imageId = Long.valueOf(s.trim());
                return;
            }
        } catch (Exception ex) {
            // If parsing fails, leave imageId as null (avoid throwing Jackson exceptions)
            this.imageId = null;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", name=" + name + ", contact=" + contact + ", email=" + email + ", otp=" + otp
                + ", type=" + type + ", fullName=" + fullName + ", imageId=" + imageId + ", pic=" + Arrays.toString(pic)
                + ", description=" + description + ", addressId=" + addressId + ", isActive=" + isActive + "]";
    }



}
