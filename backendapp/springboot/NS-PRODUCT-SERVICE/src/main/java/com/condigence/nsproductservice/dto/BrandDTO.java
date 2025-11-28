package com.condigence.nsproductservice.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandDTO {

    private Long id;

    private String name;

    private Long imageId;

    private byte[] pic;

    private Long createdByUser;

    private String createdDate;

    @JsonSetter("imageId")
    public void setImageIdObject(Object imageId) {
        if (imageId == null) { this.imageId = null; return; }
        if (imageId instanceof Number) { this.imageId = ((Number) imageId).longValue(); return; }
        if (imageId instanceof String) { try { this.imageId = Long.parseLong((String) imageId); return; } catch (NumberFormatException ignored) {} }
        if (imageId instanceof Map) {
            Object id = ((Map<?, ?>) imageId).get("id");
            if (id instanceof Number) { this.imageId = ((Number) id).longValue(); return; }
            if (id instanceof String) { try { this.imageId = Long.parseLong((String) id); return; } catch (NumberFormatException ignored) {} }
        }
    }

    @JsonSetter("createdByUser")
    public void setCreatedByUserObject(Object createdByUser) {
        if (createdByUser == null) { this.createdByUser = null; return; }
        if (createdByUser instanceof Number) { this.createdByUser = ((Number) createdByUser).longValue(); return; }
        if (createdByUser instanceof String) { try { this.createdByUser = Long.parseLong((String) createdByUser); return; } catch (NumberFormatException ignored) {} }
        if (createdByUser instanceof Map) {
            Object id = ((Map<?, ?>) createdByUser).get("id");
            if (id instanceof Number) { this.createdByUser = ((Number) id).longValue(); return; }
            if (id instanceof String) { try { this.createdByUser = Long.parseLong((String) id); return; } catch (NumberFormatException ignored) {} }
        }
    }

}
