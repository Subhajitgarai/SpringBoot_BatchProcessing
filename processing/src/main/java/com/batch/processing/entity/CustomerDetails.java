package com.batch.processing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetails {
    @Id
    @Column(name = "CUSTOMER_ID")
    private int id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "CONTACT")
    private String contactNo;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "DOB")
    private String dob;
    @Column(name = "FULL_NAME")
    private String fullName;
}
