package com.example.batchslave.repository;

import com.example.batchslave.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
