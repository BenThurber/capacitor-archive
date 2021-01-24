
const unitOptions = {farad: 'F', microFarad: 'uf', nanoFarad: 'nf', picoFarad: 'pf'};

export class CapacitorUnit {

  capacitance: number;
  voltage: number;
  identifier: string;
  value: string;
  notes: string;
  typeName: string;
  companyName: string;

  /**
   * Create a formatted capacitance like 100pf or 50nf.
   * @param noNano if true, returns values like 0.01 uf instead of 10nf.
   * @param capacitance capacitance in pico farads.
   */
  static formattedCapacitance(capacitance: number, noNano: boolean = false): string {
    let unit: string;
    let num: number;
    if (capacitance == null) {
      return '';
    } else if (capacitance < 1000) {
      unit = unitOptions.picoFarad;
      num = capacitance;
    } else if (capacitance < 1000000 && !noNano) {
      unit = unitOptions.nanoFarad;
      num = capacitance / 1000;
    } else if (capacitance < 1000000000000) {
      unit = unitOptions.microFarad;
      num = capacitance / 1000000;
    } else if (capacitance < 1000000000000000) {
      unit = unitOptions.farad;
      num = capacitance / 1000000000000;
    }
    return num + ' ' + unit;
  }

  /**
   * Compares two CapacitorUnits first by capacitance, then by voltage, then by identifier.
   * Intended to be passed in to array.prototype.sort().
   * @param u1 CapacitorUnit 1
   * @param u2 CapacitorUnit 2
   * @return a positive number if u1 > u2, negative number if u1 < u2, or 0 if they are equal.
   */
  static compare(u1: CapacitorUnit, u2: CapacitorUnit): number {
    let idOrder;
    const id1 = u1.identifier && u1.identifier.toLowerCase();
    const id2 = u2.identifier && u2.identifier.toLowerCase();

    if (id1 > id2) {
      idOrder = 1;
    } else if (id1 < id2) {
      idOrder = -1;
    } else {
      idOrder = 0;
    }

    return u2.capacitance - u1.capacitance || u2.voltage - u1.voltage || idOrder;
  }
}
