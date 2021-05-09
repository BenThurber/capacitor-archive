import {Photo} from './file/photo.model';

export const unitOptions = {farad: 'F', microFarad: 'uf', nanoFarad: 'nf', picoFarad: 'pf'};

export class CapacitorUnit {

  capacitance: number;
  voltage: number;
  value: string;
  notes: string;
  length: string;
  diameter: string;
  mountingHoleDiameter: string;
  thickness: string;
  photos: Array<Photo> = [];
  typeName: string;
  companyName: string;


  constructor(capacitorUnit?: CapacitorUnit) {
    if (capacitorUnit) {
      this.capacitance = capacitorUnit.capacitance;
      this.voltage = capacitorUnit.voltage;
      this.value = capacitorUnit.value;
      this.notes = capacitorUnit.notes;
      this.length = capacitorUnit.length;
      this.diameter = capacitorUnit.diameter;
      this.mountingHoleDiameter = capacitorUnit.mountingHoleDiameter;
      this.thickness = capacitorUnit.thickness;
      if (capacitorUnit.photos) {
        this.photos = [];
        capacitorUnit.photos.forEach(photo => this.photos.push(new Photo(photo)));
      }
      this.typeName = capacitorUnit.typeName;
      this.companyName = capacitorUnit.companyName;
    }
  }


  /**
   * Create a formatted capacitance like 100pf or 50nf.
   * @param noNano if true, returns values like 0.01 uf instead of 10nf.
   * @param capacitance capacitance in pico farads.
   * @param noSpace if true, excludes the space between value and unit.
   */
  static formattedCapacitance(capacitance: number, noNano: boolean = false, noSpace: boolean = false): string {
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
    return num + (noSpace ? '' : ' ') + unit;
  }

  /**
   * Compares two CapacitorUnits first by capacitance, then by voltage.
   * Intended to be passed in to array.prototype.sort().
   * @param u1 CapacitorUnit 1
   * @param u2 CapacitorUnit 2
   * @return a positive number if u1 > u2, negative number if u1 < u2, or 0 if they are equal.
   */
  static compare(u1: CapacitorUnit, u2: CapacitorUnit): number {

    return u2.capacitance - u1.capacitance || u2.voltage - u1.voltage;
  }


  /**
   * Takes an ordered Array of photos.  Assigns the index of each Photo in the Array to its order
   * property and adds it to the internal Array<Photo>
   * @param photoArray Ordered Array of Photos
   */
  public setOrderedPhotos(photoArray: Array<Photo>): void {
    this.photos = [];
    for (let i = 0, photo: Photo; i < photoArray.length; i++) {
      photo = new Photo(photoArray[i]);
      photo.order = i;
      this.photos.push(photo);
    }
  }

  /**
   * Returns an Array<Photo> that is sorted by the order property in each photo
   * @return an ordered Array of Photos
   */
  public getOrderedPhotos(): Array<Photo> {
    return [...this.photos].sort((a: Photo, b: Photo) => a.order - b.order);
  }

  /**
   * Formats the capacitor's length and diameter into a compact string
   * @return formatted string of the capacitor's length and diameter
   */
  public getFormattedLengthDiameter(): string {

    const dimensionList = [];

    if (this.length) {
      dimensionList.push('L: ' + this.length.trim());
    }

    if (this.diameter) {
      dimensionList.push('D: ' + this.diameter.trim());
    }

    return dimensionList.join(', ');
  }
}
