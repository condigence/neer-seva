package com.condigence.nsnotificationservice.dto;

public class MessageDTO {
    private Long id;
    private String toNumber;
    private String fromNumber;
    private String message;

    public MessageDTO() {}

    public MessageDTO(Long id, String toNumber, String fromNumber, String message) {
        this.id = id;
        this.toNumber = toNumber;
        this.fromNumber = fromNumber;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

