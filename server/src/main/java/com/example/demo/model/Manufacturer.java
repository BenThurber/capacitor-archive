package com.example.demo.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class Manufacturer {

    private final static int FIELD_LEN = 60;
    private final static int SUMMARY_LEN = 5000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "company_name", length = FIELD_LEN, nullable = false)
    private String companyName;

    @Column(name = "open-year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short openYear;

    @Column(name = "close-year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short closeYear;

    @Column(name = "bio", length = SUMMARY_LEN)
    private String bio;



}
