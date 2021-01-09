import {Component, Input, OnInit} from '@angular/core';
const fs = require('fs');

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})
export class ImageComponent implements OnInit {

  @Input() src: string;
  @Input('webp-src') webpSrc: string;
  @Input('fallback-src') fallbackSrc: string;
  @Input('webp-srcset') webSrcSet: string;
  @Input('fallback-srcset') fallbackSrcSet: string;
  @Input() sizes: string;
  @Input() width: string;
  @Input() height: string;
  @Input('usemap') useMap: string;
  @Input('ismap') isMap: string;
  @Input() alt: string;

  webpIsSupported: boolean = null;

  /**
   * checks if the browser supports webp images
   * @return boolean true if supported
   */
  static supportsWebp(): boolean {
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
  static getBrowser(): BrowserData {
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


  constructor() {}

  ngOnInit(): void {
    const baseSrc = this.src && this.src.replace(/\.[^/.]+$/, '');

    if (baseSrc) {

      if (!this.webpSrc && fs.existsSync(baseSrc + '.webp')) {
        this.webpSrc = baseSrc + '.webp';
      }

      if (!this.fallbackSrc) {
        for (const extension of ['png', 'jpg', 'jpeg', 'gif', 'jfif']) {
          if (fs.existsSync(baseSrc + '.' + extension)) {
            this.fallbackSrc = baseSrc + '.' + extension;
            break;
          }
        }
      }

    }

    if (!this.webpSrc) {
      this.webpSrc = this.fallbackSrc;
    }

    this.webpIsSupported = ImageComponent.supportsWebp();

    console.log(this.webpSrc);
    console.log(this.fallbackSrc);
  }

}


export interface BrowserData {
  name: string;
  version: string;
  versionNumber: number;
}
