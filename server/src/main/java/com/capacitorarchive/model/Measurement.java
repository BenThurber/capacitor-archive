package com.capacitorarchive.model;

import com.capacitorarchive.utility.NumberParser;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Measurement {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final int MEASUREMENT_LEN = 20;

    public enum Unit {MM, CM, IN}

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final int UM_PER_INCH = 25400;

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "unit", nullable = false)
    private Unit unit;

    @Setter(AccessLevel.NONE)
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
    private void generateMeasurementUm() {

        BigDecimal number;
        try {
            number = NumberParser.parse(this.measurementStr);
        } catch (NumberFormatException e) {
            return;
        }

        switch (this.unit) {
            case MM:
                this.measurementUm = number.scaleByPowerOfTen(3).intValue();
                break;

            case CM:
                this.measurementUm = number.scaleByPowerOfTen(4).intValue();
                break;

            case IN:
                this.measurementUm = number.multiply(new BigDecimal(UM_PER_INCH)).intValue();
                break;
        }
    }

    public void setMeasurementStr(String measurementStr) {
        this.measurementStr = measurementStr;
        generateMeasurementUm();
    }

    public Measurement() { }

    public Measurement(String measurementStr, Unit unit) {
        setMeasurementStr(measurementStr);
        setUnit(unit);
    }

}
