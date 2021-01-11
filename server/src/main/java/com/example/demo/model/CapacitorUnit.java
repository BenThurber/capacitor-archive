package com.example.demo.model;

import com.example.demo.payload.request.CapacitorUnitRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    @Setter(AccessLevel.NONE)
    @Column(name = "voltage")
    private Integer voltage;

    @Setter(AccessLevel.NONE)
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


    public void setVoltage(Integer voltage) {
        if (voltage != null && voltage <= 0) {
            voltage = null;
        }
        this.voltage = voltage;
    }

    public void setIdentifier(String identifier) {
        if (identifier != null && identifier.trim().equals("")) {
            identifier = null;
        }
        this.identifier = identifier;
    }


    @Override
    public String toString() {
        List<String> strList = new ArrayList<>();

        strList.add(CapacitorUnit.formatCapacitance(getCapacitance()));
        if (getVoltage() != null && getVoltage() > 0) {
            strList.add(getVoltage() + "V");
        }
        if (getIdentifier() != null && !getIdentifier().equals("")) {
            strList.add(getIdentifier());
        }
        return String.join(" ", strList);
    }


    /**
     * Converts a Long of capacitance in pico-farads to a formatted string.
     * @param capacitance in pico-farads
     * @return formatted capacitance of the form: {number}{"pf" | "nf" | "uf" | "F"}.
     */
    public static String formatCapacitance(Long capacitance) {

        final DecimalFormat f = new DecimalFormat("0.##");

        if (capacitance < 1000L) {
            return capacitance + "pf";
        } else if (capacitance < 1000000L) {
            return f.format(capacitance / 1000d) + "nf";
        } else if (capacitance < 1000000000000L) {
            return f.format(capacitance / 1000000d) + "uf";
        } else {
            return f.format(capacitance / 1000000000000d) + "F";
        }
    }



}
