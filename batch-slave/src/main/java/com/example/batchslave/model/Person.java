package com.example.batchslave.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "person")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "email")
    String email;

    @Column(name = "birth_date")
    LocalDate birthDate;

    @Column(name = "created_at")
    LocalDateTime createdAt;

}
