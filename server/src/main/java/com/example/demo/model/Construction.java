package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Construction that = (Construction) o;
        return constructionName.equals(that.constructionName) &&
                constructionNameLower.equals(that.constructionNameLower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constructionName, constructionNameLower);
    }
}
