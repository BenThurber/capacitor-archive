import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {RestService} from '../../services/rest/rest.service';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';
import {padEndHtml, caseInsensitiveCompare, title} from '../../utilities/text-utils';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {NgxGalleryOptions, NgxGalleryImage, NgxGalleryAnimation, NgxGalleryImageSize} from 'ngx-gallery-9';
import {Title} from '@angular/platform-browser';
import {ErrorHandlerService} from '../../services/error-handler/error-handler.service';
import {ImageComponent} from '../../components/image/image.component';
import {BreadcrumbService, UpdateBreadcrumb} from '../../services/breadcrumb/breadcrumb.service';

@Component({
  selector: 'app-view-capacitor',
  templateUrl: './view-capacitor.component.html',
  styleUrls: ['./view-capacitor.component.css', '../../styles/animations.css']
})
export class ViewCapacitorComponent implements OnInit, UpdateBreadcrumb {

  readonly caseInsensitiveCompare = caseInsensitiveCompare;

  @ViewChild('similarMenu') similarMenu: ElementRef;

  companyName: string;
  typeName: string;
  value: string;

  capacitorType: CapacitorType;
  capacitorTypeNames: Array<string> = [];
  capacitorUnit: CapacitorUnit;
  capacitorUnits: Array<CapacitorUnit>;

  formattedCapacitance = CapacitorUnit.formattedCapacitance;

  galleryOptions: NgxGalleryOptions[] = [];
  galleryImages: NgxGalleryImage[] = [];


  constructor(private titleService: Title, private activatedRoute: ActivatedRoute, private restService: RestService,
              public dynamicRouter: DynamicRouterService, private errorHandler: ErrorHandlerService,
              private breadcrumbService: BreadcrumbService) {
    this.companyName = this.activatedRoute.snapshot.paramMap.get('companyName');
    this.typeName = this.activatedRoute.snapshot.paramMap.get('typeName');
    this.value = this.activatedRoute.snapshot.paramMap.get('value');
  }

  ngOnInit(): any {
    this.titleService.setTitle('Viewing ' + title(this.typeName) + (this.value ? ' ' + this.value : ''));

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
        .subscribe({
          next: (capacitorUnit: CapacitorUnit) => {
            this.capacitorUnit = capacitorUnit;
            this.updateGalleryImages();
            // Set focus on the similar menu
            setTimeout(() => this.similarMenu && this.similarMenu.nativeElement.focus(), 100);
          },
          error: err => this.errorHandler.handleGetRequestError(err, 'Error getting CapacitorUnit')
        });
    }

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName)
      .subscribe({
        next: (capacitorType: CapacitorType) => {
            this.capacitorType = capacitorType;
            this.updateBreadcrumb(capacitorType.companyName, capacitorType.typeName);
          },
        error: err => this.errorHandler.handleGetRequestError(err, 'Error getting CapacitorType')
      });


    this.restService.getAllCapacitorUnitsFromCapacitorType(this.companyName, this.typeName)
      .subscribe((capacitorUnits: Array<CapacitorUnit>) => {
        this.capacitorUnits = capacitorUnits.sort(CapacitorUnit.compare);
        if (!this.value && this.capacitorUnits.length > 0) {
          this.capacitorUnit = this.capacitorUnits[0];
          this.similarMenuChanged(this.capacitorUnit.value);

        } else if (this.capacitorUnits.length === 0) {
          this.capacitorUnit = new CapacitorUnit();
          this.updateGalleryImages();
        }

        // Set focus on the similar menu
        setTimeout(() => this.similarMenu && this.similarMenu.nativeElement.focus(), 100);
      });


    this.restService.getAllTypes(this.companyName).subscribe(
      (capacitorTypeNames: Array<CapacitorType>) => this.capacitorTypeNames = capacitorTypeNames.map(ct => ct.typeName));
  }


  updateBreadcrumb(companyName: string, typeName: string): void {
    this.breadcrumbService.change([
      {name: companyName,
        url: ['/manufacturer', 'view', companyName]
      },
      {name: typeName,
        url: ['/capacitor', 'view', companyName, typeName]
      },
    ]);
  }

  similarMenuChanged(value): void {
    this.capacitorUnit = this.capacitorUnits.filter(u => u.value === value).pop();
    const cu = this.capacitorUnit;
    this.dynamicRouter.router.navigate([
      '/capacitor',
      'view',
      cu.companyName,
      cu.typeName,
      cu.value
    ], { replaceUrl: true });
    this.updateBreadcrumb(cu.companyName, cu.typeName);
    this.updateGalleryImages();
  }

  formatSimilarCapacitor(capacitorUnit: CapacitorUnit): string {
    let str = '';
    str += padEndHtml(CapacitorUnit.formattedCapacitance(capacitorUnit.capacitance, true, true), 9);
    str += padEndHtml(String(capacitorUnit.voltage > 0 ? capacitorUnit.voltage + 'V' : ''), 8);

    return str;
  }


  updateGalleryImages(): void {
    this.galleryImages = [];
    if (!this.capacitorUnit) {  // Don't show anything until capacitorUnit has loaded
      return;
    }
    if (this.capacitorUnit.photos.length === 0) {
      this.galleryImages.push({
        big: ImageComponent.webpIsSupported ?
          '../../../assets/no-capacitor-images-no-arrow.webp' : '../../../assets/no-capacitor-images-no-arrow.jpg',

        medium: ImageComponent.webpIsSupported ?
          '../../../assets/no-capacitor-images.webp' : '../../../assets/no-capacitor-images.jpg',

        small: '',
      });
    } else {
      for (const photo of this.capacitorUnit.getOrderedPhotos()) {
        this.galleryImages.push({
          big: photo.url,
          medium: photo.getThumbnail(1000, 500).url,
          small: photo.getSmallestThumbnail().url,
        });
      }
    }
  }

}


