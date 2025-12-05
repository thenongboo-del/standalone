package com.sbproject.standalone.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class Dealer extends Member {

    private LocalDate hireDate = LocalDate.now();

    private List<Consultation> consultList = new ArrayList<>();
}
