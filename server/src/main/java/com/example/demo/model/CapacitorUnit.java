package com.example.demo.model;

import com.example.demo.payload.request.CapacitorUnitRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"capacitance", "voltage", "identifier"})
)
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class CapacitorUnit {

    private final static int FIELD_LEN = 60;
    private static final int NOTES_LEN = 5000;

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * This is the capacitance measured in pico-farads
     */
    @Column(name = "capacitance", nullable = false)
    private Long capacitance;

    @Column(name = "voltage")
    private Integer voltage;

    @Column(name = "identifier", length = FIELD_LEN)
    private String identifier;

    @Column(name = "notes", length = NOTES_LEN)
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "capacitor_type_id", nullable = false)
    private CapacitorType capacitorType;


    public CapacitorUnit(CapacitorUnitRequest capacitorTypeRequest) {
        CapacitorUnitRequest r = capacitorTypeRequest;

        setCapacitance(r.getCapacitance());
        setVoltage(r.getVoltage());
        setIdentifier(r.getIdentifier());
        setNotes(r.getNotes());
    }


    public CapacitorUnit() { }



}
