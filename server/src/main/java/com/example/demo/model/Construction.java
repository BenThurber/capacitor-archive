package com.example.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Construction {


    @Id
    @Column(name = "construction_name", length = 50, nullable = false)
    private String constructionName;

    @Column(name = "construction_name_lower", length = 50, nullable = false, unique = true)
    private String constructionNameLower;

    /**This allows company_name to be case insensitive unique*/
    @PrePersist
    @PreUpdate
    private void prepare() {
        this.constructionNameLower = constructionName == null ? null : constructionName.toLowerCase();
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "construction", cascade = CascadeType.ALL)
    private List<CapacitorType> capacitorTypes = new ArrayList<>();


    public Construction(String constructionName) {
        this.constructionName = constructionName;
    }

    public Construction() { }



}
