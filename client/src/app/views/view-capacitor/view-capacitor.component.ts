import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {RestService} from '../../services/rest/rest.service';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';
import {padEndHtml, caseInsensitiveCompare} from '../../utilities/text-utils';
import {Manufacturer} from '../../models/manufacturer.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {NgxGalleryOptions, NgxGalleryImage, NgxGalleryAnimation, NgxGalleryImageSize} from 'ngx-gallery-9';

@Component({
  selector: 'app-view-capacitor',
  templateUrl: './view-capacitor.component.html',
  styleUrls: ['./view-capacitor.component.css']
})
export class ViewCapacitorComponent implements OnInit {

  readonly caseInsensitiveCompare = caseInsensitiveCompare;

  @ViewChild('similarMenu') similarMenu: ElementRef;

  companyName: string;
  typeName: string;
  value: string;

  capacitorType: CapacitorType;
  capacitorUnit: CapacitorUnit;
  capacitorUnits: Array<CapacitorUnit>;
  manufacturer: Manufacturer;

  formattedCapacitance = CapacitorUnit.formattedCapacitance;

  galleryOptions: NgxGalleryOptions[] = [];
  galleryImages: NgxGalleryImage[] = [];


  constructor(private activatedRoute: ActivatedRoute, private restService: RestService, public dynamicRouter: DynamicRouterService) {
    this.companyName = this.activatedRoute.snapshot.paramMap.get('companyName');
    this.typeName = this.activatedRoute.snapshot.paramMap.get('typeName');
    this.value = this.activatedRoute.snapshot.paramMap.get('value');
  }

  ngOnInit(): any {

    this.galleryOptions = [
      {
        previewCloseOnClick: true,
        previewCloseOnEsc: true,
        previewKeyboardNavigation: true,
        previewZoom: true,
        previewZoomStep: 0.4,
        previewZoomMax: 4,
        previewDownload: true,
        previewAnimation: false,
        width: '100%',
        height: '100%',
        thumbnailsColumns: 4,
        imageInfinityMove: true,
        imageSize: NgxGalleryImageSize.Contain,
        imageAnimation: NgxGalleryAnimation.Slide,
      }
    ];


    if (this.value) {
      this.restService.getCapacitorUnitByValue(this.companyName, this.typeName, this.value)
        .subscribe((capacitorUnit: CapacitorUnit) => {
          this.capacitorUnit = new CapacitorUnit(capacitorUnit);
          this.updateGalleryImages();
          // Set focus on the similar menu
          setTimeout(() => this.similarMenu.nativeElement.focus(), 100);
        });
    }

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName)
      .subscribe((capacitorType: CapacitorType) => this.capacitorType = capacitorType);

    this.restService.getManufacturerByName(this.companyName).subscribe(
      (manufacturer: Manufacturer) => this.manufacturer = manufacturer);

    return this.restService.getAllCapacitorUnitsFromCapacitorType(this.companyName, this.typeName)
      .subscribe((capacitorUnits: Array<CapacitorUnit>) => {
        this.capacitorUnits = capacitorUnits.map(cu => new CapacitorUnit(cu)).sort(CapacitorUnit.compare);
        if (!this.value && this.capacitorUnits.length > 0) {
          this.capacitorUnit = this.capacitorUnits[0];
        } else if (this.capacitorUnits.length === 0) {
          this.capacitorUnit = new CapacitorUnit();
        }
        this.updateGalleryImages();
        // Set focus on the similar menu
        setTimeout(() => this.similarMenu.nativeElement.focus(), 100);
      });
  }


  similarMenuChanged(value): void {
    this.capacitorUnit = this.capacitorUnits.filter(u => u.value === value).pop();
    this.updateGalleryImages();
  }

  formatSimilarCapacitor(capacitorUnit: CapacitorUnit): string {
    let str = '';
    str += padEndHtml(CapacitorUnit.formattedCapacitance(capacitorUnit.capacitance, true)
      .replace(' ', ''), 9);
    str += padEndHtml(String(capacitorUnit.voltage > 0 ? capacitorUnit.voltage + 'V' : ''), 8);
    str += capacitorUnit.identifier ? capacitorUnit.identifier : '';

    return str;
  }


  updateGalleryImages(): void {
    this.galleryImages = [];
    if (!this.capacitorUnit) {
      return;
    }
    if (this.capacitorUnit.photos.length === 0) {
      this.galleryImages.push({
        big: '../../../assets/no-capacitor-images-no-arrow.jpg',
        medium: '../../../assets/no-capacitor-images.jpg',
        small: '',
      });
    } else {
      for (const photo of this.capacitorUnit.getOrderedPhotos()) {
        this.galleryImages.push({
          big: photo.url,
          medium: photo.url,
          small: photo.getThumbnailUrl(),
        });
      }
    }
  }

}


