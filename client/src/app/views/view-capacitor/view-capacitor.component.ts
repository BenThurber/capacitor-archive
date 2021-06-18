import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {RestService} from '../../services/rest/rest.service';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';
import {padEndHtml, caseInsensitiveCompare, title} from '../../utilities/text-utils';
import {NgxGalleryOptions, NgxGalleryImage, NgxGalleryAnimation, NgxGalleryImageSize} from 'ngx-gallery-9';
import {Title} from '@angular/platform-browser';
import {ErrorHandlerService} from '../../services/error-handler/error-handler.service';
import {ImageComponent} from '../../components/image/image.component';
import {BreadcrumbService, UpdateBreadcrumb} from '../../services/breadcrumb/breadcrumb.service';
import {InputRichTextComponent} from '../../components/form-controls/input-rich-text/input-rich-text.component';
import {scrollToElement} from '../../utilities/gui-utils';
import {SpringErrorResponse} from '../../models/spring-error-response.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';


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
  capacitorTypeDescriptionPlainText = '';
  capacitorUnit: CapacitorUnit;
  capacitorUnits: Array<CapacitorUnit>;

  similarMenuSelectedOptions: Array<string>;
  formattedCapacitance = CapacitorUnit.formattedCapacitance;
  scrollToElement = scrollToElement;
  Math = Math;

  galleryOptions: NgxGalleryOptions[] = [];
  galleryImages: NgxGalleryImage[] = [];


  constructor(private titleService: Title, private activatedRoute: ActivatedRoute, private restService: RestService,
              public router: Router, private errorHandler: ErrorHandlerService,
              private breadcrumbService: BreadcrumbService, public dynamicRouter: DynamicRouterService) {
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

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName)
      .subscribe({
        next: (capacitorType: CapacitorType) => {
            this.capacitorType = capacitorType;
            this.capacitorTypeDescriptionPlainText = InputRichTextComponent.htmlToText(capacitorType.description);
            this.updateBreadcrumb(capacitorType.companyName, capacitorType.typeName);
          },
        error: err => this.errorHandler.handleGetRequestError(err, 'Error getting CapacitorType')
      });


    this.restService.getAllCapacitorUnitsFromCapacitorType(this.companyName, this.typeName)
      .subscribe((capacitorUnits: Array<CapacitorUnit>) => {

        this.capacitorUnit = capacitorUnits.filter(cu => cu.value === this.value).pop();
        if (this.value && !this.capacitorUnit) {
          const err = new SpringErrorResponse();
          err.status = 404;
          this.errorHandler.handleGetRequestError(err, 'Error getting CapacitorUnit from value');
        }

        this.capacitorUnits = capacitorUnits.sort(CapacitorUnit.compare);
        if (!this.value && this.capacitorUnits.length > 0) {
          this.capacitorUnit = this.capacitorUnits[0];
          this.similarMenuChanged(this.capacitorUnit.value);

        } else if (this.capacitorUnits.length === 0) {
          this.capacitorUnit = new CapacitorUnit();
        }

        this.updateGalleryImages();
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
    this.value = value;
    const cu = this.capacitorUnit;
    this.router.navigate([
      '/capacitor',
      'view',
      cu.companyName,
      cu.typeName,
      cu.value
    ], { replaceUrl: true });
    this.updateBreadcrumb(cu.companyName, cu.typeName);
    this.updateGalleryImages();
  }

  /**
   * Moves the selected item in the similarMenu up or down.
   * @param i how far to move the selected item up or down, positive moves up, negative moves down.
   */
  similarMenuMove(i): void {
    const len = this.capacitorUnits.length;
    let j = this.capacitorUnits.lastIndexOf(this.capacitorUnit);

    i = -i;
    j = (((j + i) % len) + len) % len;       // Wrap if index overflows array bounds

    const value = this.capacitorUnits[j]?.value;
    if (value) {
      this.similarMenuSelectedOptions = [value];
      this.similarMenuChanged(value);
    }
  }

  formatSimilarCapacitor(capacitorUnit: CapacitorUnit): string {
    let str = '';
    str += padEndHtml(CapacitorUnit.formattedCapacitance(capacitorUnit.capacitance, true, true), 7);
    str += ' ';
    str += String(capacitorUnit.voltage > 0 ? capacitorUnit.voltage + 'V' : '');

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


