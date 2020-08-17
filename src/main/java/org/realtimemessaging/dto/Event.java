package org.realtimemessaging.dto;

import org.realtimemessaging.controller.validation.EventConstraint;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@EventConstraint
public class Event implements Serializable {

    private String actionType;

    private String customerAddress;

    @NotBlank(message = "Text is mandatory")
    private String text;

    public Event() {
    }

    public Event(String customerAddress, String text, String actionType) {
        this.customerAddress = customerAddress;
        this.text = text;
        this.actionType = actionType;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return String.format("{customer address = %s, text = %s, action type = %s}", getCustomerAddress(), getText(), getActionType());
    }

}

