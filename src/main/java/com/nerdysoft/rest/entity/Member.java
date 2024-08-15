package com.nerdysoft.rest.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "member")
public class Member {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "membership_date")
    private LocalDate membershipDate;

    public Member() {
    }

    public Member(int id, String firstName, String lastName, LocalDate membershipDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membershipDate = membershipDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", membershipDate=" + membershipDate +
                '}';
    }
}
