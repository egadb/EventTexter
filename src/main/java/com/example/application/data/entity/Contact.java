package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.example.application.data.AbstractEntity;
import com.example.application.data.Validator.ValidPhoneNumber;

@Entity
public class Contact extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @ManyToOne
    @JoinColumn(name = "gang_id")
    @NotNull
    @JsonIgnoreProperties({"members"})
    private Gang gang;

    @Lob
    @JoinColumn(name = "checkbox_id")
    @NotNull
    @JsonIgnoreProperties(ignoreUnknown=true)
    private Checkbox selected;


    //@ValidPhoneNumber(message="Please enter a valid phone number")
    @NotEmpty
    private String phone = "";

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gang getGang() {
        return gang;
    }

    public void setGang(Gang gang) {
        this.gang = gang;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Checkbox getSelected() {
        return selected;
    }

    public void setSelected(Checkbox selected) {
        this.selected = selected;
    }
}
