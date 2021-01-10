package com.example.demo.model;

import com.example.demo.payload.request.CapacitorUnitRequest;

import javax.persistence.*;

@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"capacitance", "voltage", "identifier"})
)
@Entity
public class CapacitorUnit {

    private final static int FIELD_LEN = 60;
    private static final int NOTES_LEN = 5000;

    @Id
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
    @JoinColumn(name = "type_name", nullable = false)
    private CapacitorType capacitorType;


    CapacitorUnit(CapacitorUnitRequest capacitorTypeRequest) {
        CapacitorUnitRequest r = capacitorTypeRequest;

        setCapacitance(r.getCapacitance());
        setVoltage(r.getVoltage());
        setIdentifier(r.getIdentifier());
        setNotes(r.getNotes());
    }


    CapacitorUnit() { }


    public Long getId() {
        return id;
    }

    public Long getCapacitance() {
        return capacitance;
    }

    public void setCapacitance(Long capacitance) {
        this.capacitance = capacitance;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public CapacitorType getCapacitorType() {
        return capacitorType;
    }

    public void setCapacitorType(CapacitorType capacitorType) {
        this.capacitorType = capacitorType;
    }
}
