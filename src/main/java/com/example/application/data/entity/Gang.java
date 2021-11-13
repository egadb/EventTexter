package com.example.application.data.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.example.application.data.AbstractEntity;

@Entity
public class Gang extends AbstractEntity {
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "gang")
    private List<Contact> members = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getMembers() {
        return members;
    }

    public void setMembers(List<Contact> members) {
        this.members = members;
    }
}
