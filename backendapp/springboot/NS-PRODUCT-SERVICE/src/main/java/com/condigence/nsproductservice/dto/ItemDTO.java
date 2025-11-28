package com.condigence.nsproductservice.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {

    private Long id;

    private byte[] pic;
    private String name;
    private Integer price;
    private Integer mrp;
    private Integer dispPrice;

    private Integer quantity;
    private String code;
    private Integer discount;
    private String type;
    private String description;
    private Integer capacity;
    private Long brandId;
    private Long imageId;
    private java.util.Date dateCreated;

    /**
     * Tolerant setter accepts either a numeric id or an object like {"id":123} or a string.
     */
    @JsonSetter("imageId")
    public void setImageIdObject(Object imageId) {
        if (imageId == null) {
            this.imageId = null;
            return;
        }
        if (imageId instanceof Number) {
            this.imageId = ((Number) imageId).longValue();
            return;
        }
        if (imageId instanceof String) {
            try {
                this.imageId = Long.parseLong((String) imageId);
                return;
            } catch (NumberFormatException e) {
                // ignore and continue
            }
        }
        if (imageId instanceof Map) {
            Object id = ((Map<?, ?>) imageId).get("id");
            if (id instanceof Number) {
                this.imageId = ((Number) id).longValue();
                return;
            }
            if (id instanceof String) {
                try { this.imageId = Long.parseLong((String) id); return; } catch (NumberFormatException ignored) {}
            }
        }
        // fallback: leave as null
    }

    @JsonSetter("brandId")
    public void setBrandIdObject(Object brandId) {
        if (brandId == null) { this.brandId = null; return; }
        if (brandId instanceof Number) { this.brandId = ((Number) brandId).longValue(); return; }
        if (brandId instanceof String) { try { this.brandId = Long.parseLong((String) brandId); return; } catch (NumberFormatException ignored) {} }
        if (brandId instanceof Map) {
            Object id = ((Map<?, ?>) brandId).get("id");
            if (id instanceof Number) { this.brandId = ((Number) id).longValue(); return; }
            if (id instanceof String) { try { this.brandId = Long.parseLong((String) id); return; } catch (NumberFormatException ignored) {} }
        }
    }

}
