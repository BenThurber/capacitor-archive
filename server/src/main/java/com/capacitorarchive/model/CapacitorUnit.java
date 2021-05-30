package com.capacitorarchive.model;

import com.capacitorarchive.payload.request.CapacitorUnitRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"capacitor_type_id", "value"})
)
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class CapacitorUnit implements Comparable<CapacitorUnit> {
                                       // Long  'C' Int  'V'
    private final static int VALUE_LENGTH = 20 + 1 + 10 + 1;
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
    @Column(name = "value", length = VALUE_LENGTH, nullable = false)
    private String value;

    @PrePersist
    @PreUpdate
    private void prepare() {
        Long capacitanceNonNull = capacitance != null ? capacitance : 0L;
        Integer voltageNonNull = voltage != null ? voltage : 0;
        this.value = String.format("%dC%dV", capacitanceNonNull, voltageNonNull);
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

    /**
     * Returns the Photo that has the lowest order property and has at least one thumbnail.
     * If there are no photos with thumbnails, it returns the Photo with the lowest order property.
     * @return Photo with lowest order
     */
    public Photo getPrimaryPhoto() {
        if (getPhotos() == null) {return null; }

        List<Photo> photosWithThumbnails = getPhotos()
                .stream()
                .filter(p -> p.getThumbnails() != null && p.getThumbnails().size() > 0)
                .collect(Collectors.toList());

        Collection<Photo> photos = photosWithThumbnails.size() > 0 ? photosWithThumbnails : getPhotos();

        return photos.stream().min(Comparator.comparing(Photo::getOrder)).orElse(null);
    }

    /**
     * Compare function used for sorting
     * @param other CapacitorUnit to compare to
     * @return integer corresponding to the difference between the two units
     */
    @Override
    public int compareTo(CapacitorUnit other) {
        long capacitance1 = this.getCapacitance() == null ? 0 : this.getCapacitance();
        long capacitance2 = other.getCapacitance() == null ? 0 : other.getCapacitance();
        int voltage1 = this.getVoltage() == null ? 0 : this.getVoltage();
        int voltage2 = other.getVoltage() == null ? 0 : other.getVoltage();

        int comparison = Long.compare(capacitance1, capacitance2);
        if (comparison != 0) return comparison;

        return voltage1 - voltage2;
    }


    @Override
    public String toString() {
        List<String> strList = new ArrayList<>();

        strList.add(CapacitorUnit.formatCapacitance(getCapacitance()));
        if (getVoltage() != null && getVoltage() > 0) {
            strList.add(getVoltage() + "V");
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
