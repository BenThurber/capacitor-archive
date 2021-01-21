package com.example.demo.model;

import com.example.demo.payload.request.CapacitorTypeRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"manufacturer_id", "type_name_lower"})
)
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class CapacitorType {

    private final static int FIELD_LEN = 60;
    private static final int DESCRIPTION_LEN = 5000;

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type_name", length = FIELD_LEN, nullable = false)
    private String typeName;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "type_name_lower", length = FIELD_LEN, nullable = false)
    private String typeNameLower;

    /**This allows type_name to be case insensitive unique*/
    @PrePersist
    @PreUpdate
    private void prepare() {
        this.typeNameLower = typeName == null ? null : typeName.toLowerCase();
    }

    @Column(name = "start_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short startYear;

    @Column(name = "end_year", columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short endYear;

    @Column(name = "description", length = DESCRIPTION_LEN)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "construction_name", nullable = false)
    private Construction construction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "capacitorType", cascade = CascadeType.ALL)
    private List<CapacitorUnit> capacitorUnits = new ArrayList<>();


    public CapacitorType(CapacitorTypeRequest capacitorTypeRequest) {
        CapacitorTypeRequest r = capacitorTypeRequest;

        setTypeName(r.getTypeName());
        setStartYear(r.getStartYear());
        setEndYear(r.getEndYear());
        setDescription(r.getDescription());
    }

    public void edit(CapacitorTypeRequest capacitorTypeRequest) {
        edit(capacitorTypeRequest, null);
    }

    public void edit(CapacitorTypeRequest capacitorTypeRequest, Construction construction) {
        CapacitorTypeRequest r = capacitorTypeRequest;

        setTypeName(r.getTypeName());
        setStartYear(r.getStartYear());
        setEndYear(r.getEndYear());
        setDescription(r.getDescription());

        if (construction != null) {
            setConstruction(construction);
        }
    }


    public CapacitorType() {}



}
