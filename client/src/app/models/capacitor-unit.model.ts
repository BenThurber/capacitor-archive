
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

  static compare(u1: CapacitorUnit, u2: CapacitorUnit): number {

    if (u1.value > u2.value) {
      return -1;
    }
    if (u1.value < u2.value) {
      return 1;
    }
    // names must be equal
    return 0;
  }
}
