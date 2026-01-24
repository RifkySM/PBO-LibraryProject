package com.example.pbolibraryproject.models;

import java.time.LocalDate;

public class Member extends Person {
    private String email;
    private String phone;
    private LocalDate joinDate;
    private boolean isActive;

    public Member(String id, String name, String email, String phone, LocalDate joinDate) {
        super(id, name);
        this.email = email;
        this.phone = phone;
        this.joinDate = joinDate;
        this.isActive = true;
    }

    public Member(String id, String name, String email) {
        this(id, name, email, "", LocalDate.now());
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getMemberId() {
        return getId();
    }

    @Override
    public String toString() {
        return getName() + " [" + getId() + "]";
    }
}
