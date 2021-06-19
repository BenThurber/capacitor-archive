package com.capacitorarchive.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Measurement {

    private final int MEASUREMENT_LEN = 20;

    public enum Unit {MM, CM, IN};

    private final int UM_PER_INCH = 25400;

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "unit", nullable = false)
    private Unit unit;

    @Column(name = "measurement_str", length = MEASUREMENT_LEN, nullable = false)
    private String measurementStr;

    @Setter(AccessLevel.NONE)
    @Column(name = "measurement_um", nullable = false)
    private Integer measurementUm;

    /**
     * Converts the measurement string into micro meters and saves the result in measurementUm.
     */
    @PrePersist
    @PreUpdate
    private void convertMeasurementStr() {

        // Temporary, This needs to be improved.
        // Also what if its not a whole number?  parseFloat would work but be imprecise!
        int number = Integer.parseInt(this.measurementStr);

        switch (this.unit) {
            case MM:
                this.measurementUm = number * 1000;
                break;

            case CM:
                this.measurementUm = number * 10000;
                break;

            case IN:
                this.measurementUm = number * UM_PER_INCH;
                break;

            default:
                return;
        }
    }

}
