package com.example.demo.model;

import com.example.demo.payload.request.CapacitorUnitRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.*;

@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"capacitor_type_id", "value"})
)
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class CapacitorUnit implements Comparable<CapacitorUnit> {

    private final static int IDENTIFIER_LEN = 60;

                                       // Long  'C' Int  'V'  VARCHAR
    private final static int VALUE_LENGTH = 20 + 1 + 10 + 1 + IDENTIFIER_LEN;
    private static final int NOTES_LEN = 5000;
    private static final int DIMENSION_LEN = 25;

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
    @Column(name = "identifier", length = IDENTIFIER_LEN)
    private String identifier;

    @Setter(AccessLevel.NONE)
    @Column(name = "value", length = VALUE_LENGTH, nullable = false)
    private String value;

    @PrePersist
    @PreUpdate
    private void prepare() {
        Long capacitanceNonNull = capacitance != null ? capacitance : 0L;
        Integer voltageNonNull = voltage != null ? voltage : 0;
        String identifierNonNull = identifier != null ? identifier : "";
        this.value = String.format("%dC%dV%s", capacitanceNonNull, voltageNonNull, identifierNonNull);
    }

    @Column(name = "notes", length = NOTES_LEN)
    private String notes;

    @Column(name = "length", length = DIMENSION_LEN)
    private String length;

    @Column(name = "diameter", length = DIMENSION_LEN)
    private String diameter;

    @Column(name = "mounting_hole_diameter", length = DIMENSION_LEN)
    private String mountingHoleDiameter;

    @Column(name = "thickness", length = DIMENSION_LEN)
    private String thickness;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "capacitor_type_id", nullable = false)
    private CapacitorType capacitorType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "capacitorUnit", cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})  // All except persist
    private Set<Photo> photos = new HashSet<>();


    public CapacitorUnit(CapacitorUnitRequest capacitorUnitRequest) {
        edit(capacitorUnitRequest);
    }


    public CapacitorUnit() { }


    public void edit(CapacitorUnitRequest capacitorUnitRequest) {
        CapacitorUnitRequest r = capacitorUnitRequest;

        setCapacitance(r.getCapacitance());
        setVoltage(r.getVoltage());
        setIdentifier(r.getIdentifier());
        setNotes(r.getNotes());

        setLength(r.getLength());
        setDiameter(r.getDiameter());
        setMountingHoleDiameter(r.getMountingHoleDiameter());
        setThickness(r.getThickness());
    }


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

    /**
     * Get the smallest thumbnail of the Photo that has the lowest order property.
     * If the Photo doesn't have thumbnails, tries the next photo and so on.
     * @return Thumbnail of the primary capacitor photo
     */
    public Thumbnail getPrimaryThumbnail() {
        Photo firstPhotoWithThumbnail = this.getPrimaryPhoto();

        if (firstPhotoWithThumbnail == null) {
            return null;
        }

        return firstPhotoWithThumbnail.getThumbnails()
                .stream()
                .min(Comparator.comparing(Thumbnail::getSize))
                .orElse(null);
    }

    /**
     * the Photo that has the lowest order property and has at least one thumbnail
     * @return Photo with >=1 thumbnails
     */
    public Photo getPrimaryPhoto() {
        return this.getPhotos()
                .stream()
                .filter(p -> p.getThumbnails() != null && p.getThumbnails().size() > 0)
                .min(Comparator.comparing(Photo::getOrder))
                .orElse(null);
    }

    /**
     * Special compare function used for sorting
     * @param other CapacitorUnit to compare to
     * @return integer corresponding to the difference between the two units
     */
    @Override
    public int compareTo(CapacitorUnit other) {
        long capacitance1 = this.getCapacitance() == null ? 0 : this.getCapacitance();
        long capacitance2 = other.getCapacitance() == null ? 0 : other.getCapacitance();
        int voltage1 = this.getVoltage() == null ? 0 : this.getVoltage();
        int voltage2 = other.getVoltage() == null ? 0 : other.getVoltage();
        String identifier1 = this.getIdentifier() == null ? "" : this.getIdentifier();
        String identifier2 = other.getIdentifier() == null ? "" : other.getIdentifier();

        int comparison = Long.compare(capacitance1, capacitance2);
        if (comparison != 0) return comparison;

        comparison = voltage1 - voltage2;
        if (comparison != 0) return comparison;

        // identifier1 and identifier2 are purposely reversed
        return identifier2.compareTo(identifier1);
    }


    @Override
    public String toString() {
        List<String> strList = new ArrayList<>();

        strList.add(CapacitorUnit.formatCapacitance(getCapacitance()));
        if (getVoltage() != null && getVoltage() > 0) {
            strList.add(getVoltage() + "V");
        }
        if (getIdentifier() != null && !getIdentifier().equals("")) {
            strList.add("'" + getIdentifier() + "'");
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
