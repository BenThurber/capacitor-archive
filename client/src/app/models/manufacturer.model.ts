interface ManufacturerData {
  companyName: string;
  openYear: number;
  closeYear: number;
  summary: string;
}

export class Manufacturer {

  id: number;
  companyName: string;
  openYear: number;
  closeYear: number;
  summary: string;

  insertData(data: ManufacturerData): void {
    this.companyName = data.companyName;
    this.openYear = data.openYear;
    this.closeYear = data.closeYear;
    this.summary = data.summary;
  }
}
