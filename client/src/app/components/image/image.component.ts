import {Component, Inject, Input, OnInit} from '@angular/core';
import { DOCUMENT } from '@angular/common';

/**
 * This is a component that can be used in the same way as an <img> tag, but displays webp images only if
 * they're supported by the browser, and a fallback format if they're not.  The following are use cases.
 *
 * Explicitly provide paths to both files
 * <app-image webp-src=".../image.webp" fallback-src=".../image.png"></app-image>
 *
 * Provide path to webp and extension of fallback file of same name
 * <app-image src=".../image.webp" fallbackExtension="png"></app-image>
 *
 * Provide path to fallback file.  Implicitly assumes there is a webp file of the same name.
 * <app-image src=".../image.png"></app-image>
 */
@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})
export class ImageComponent implements OnInit {

  public static readonly webpIsSupported: boolean = ImageComponent.supportsWebp();
  webpIsSupported = ImageComponent.webpIsSupported;

  @Input() src: string;
  @Input('webp-src') webpSrc: string;
  @Input('fallback-src') fallbackSrc: string;
  @Input('webp-srcset') webpSrcSet: string;
  @Input('fallback-srcset') fallbackSrcSet: string;
  @Input() sizes: string;
  @Input() width: string;
  @Input() height: string;
  @Input('usemap') useMap: string;
  @Input('ismap') isMap: string;
  @Input() alt: string;
  @Input() style: string;
  @Input() align: string;

  @Input('fallback-extension') fallbackExtension: string;
  @Input() link: any;

  supportedSrc: string;
  supportedSrcSet: string;


  /**
   * checks if the browser supports webp images
   * @return boolean true if supported
   */
  private static supportsWebp(): boolean {
    let supported;
    const loadsWebpOnCanvas = document.createElement('canvas').toDataURL('image/webp').indexOf('data:image/webp') === 0;
    const browser = ImageComponent.getBrowser();

    supported = loadsWebpOnCanvas;

    // If false support, check for Firefox and Edge which don't work with the canvas method.
    if (!loadsWebpOnCanvas) {
      switch (browser.name.toLowerCase()) {
        case 'firefox':
          supported = browser.versionNumber >= 65;
          break;
        case 'edge':
          supported = browser.versionNumber >= 18;
          break;
      }
    }

    return supported;
  }

  /**
   * Gets the browser name and version.
   * @return BrowserData the browser name and version number as a string and a number.
   */
  private static getBrowser(): BrowserData {
    const ua = navigator.userAgent;
    let tem;
    let M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];

    if (/trident/i.test(M[1])){
      tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
      return {name: 'IE', version: (tem[1] || ''), versionNumber: parseFloat((tem[1] || ''))};
    }
    if (M[1] === 'Chrome'){
      tem = ua.match(/\bOPR|Edge\/(\d+)/);
      if (tem != null)   {
        return {name: 'Opera', version: tem[1], versionNumber: parseFloat(tem[1])};
      }
    }
    M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];

    tem = ua.match(/version\/(\d+)/i);
    if ((tem != null)) {
      M.splice(1, 1, tem[1]);
    }

    return {
      name: M[0],
      version: M[1],
      versionNumber: parseFloat(M[1]),
    };
  }


  constructor(@Inject(DOCUMENT) private document: Document) {}

  ngOnInit(): void {

    const srcExtension = this.src && this.src.split('.').pop();
    const srcBaseName = this.src && this.src.replace(/\.[^/.]+$/, '');

    if (!this.fallbackExtension && srcExtension !== 'webp') {
      this.fallbackExtension = srcExtension;
    }

    if (srcBaseName) {

      if (!this.webpSrc) {
        this.webpSrc = srcBaseName + '.webp';
      }

      if (!this.fallbackSrc) {
        this.fallbackSrc = srcBaseName + '.' + this.fallbackExtension.trim().replace('.', '');
      }

    }

    if (!this.webpSrc) {
      this.webpSrc = this.fallbackSrc;
    }
    if (!this.fallbackSrc) {
      this.fallbackSrc = this.webpSrc;
    }

    this.link = this.link === true || this.link === 'true' ? true : null;

    if (this.webpIsSupported === true) {
      this.supportedSrc = this.webpSrc || this.fallbackSrc;
      this.supportedSrcSet = this.webpSrcSet;
    }
    if (this.webpIsSupported === false) {
      this.supportedSrc = this.fallbackSrc || this.webpSrc;
      this.supportedSrcSet = this.fallbackSrcSet;
    }

  }


  navigateToUrl(url: string): void {
    document.location.href = url;
  }

}


export interface BrowserData {
  name: string;
  version: string;
  versionNumber: number;
}
