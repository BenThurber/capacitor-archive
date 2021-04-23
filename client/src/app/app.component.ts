import {Component, HostListener, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {map, filter, mergeMap} from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  defaultTitle = 'Capacitor Archive';

  public constructor(private titleService: Title, private router: Router, private activatedRoute: ActivatedRoute) {
    // Set the title in the browser window
    this.titleService.setTitle(this.defaultTitle);
  }


  ngOnInit(): void {
    preventWindowFileDrop();

    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        map(() => this.activatedRoute),
        map(route => {
          while (route.firstChild) { route = route.firstChild; }
          return route;
        }),
        filter(route => route.outlet === 'primary'),
        mergeMap(route => route.data)
      )
      .subscribe(event => this.titleService.setTitle(event.title || this.defaultTitle));
  }

  /**
   * Prevent Delete/Backspace key from navigating the browser back a page.  This is an issue in Firefox, but not Chrome.
   * Function is triggered anytime a key is pressed.
   * Credit: https://stackoverflow.com/a/54141162
   * @param evt a keyboard event.
   */
  @HostListener('document:keydown', ['$event'])
  onKeyDown(evt: KeyboardEvent): boolean {
    if (
      evt.key === 'Backspace'
    ) {
      let doPrevent = true;
      const types = ['text', 'password', 'file', 'search', 'email', 'number', 'date', 'color', 'datetime', 'datetime-local', 'month',
        'range', 'search', 'tel', 'time', 'url', 'week'];
      const target = (evt.target as HTMLInputElement);

      const disabled = target.disabled || (event.target as HTMLInputElement).readOnly;
      if (!disabled) {
        if (target.isContentEditable) {
          doPrevent = false;
        } else if (target.nodeName === 'INPUT') {
          let type = target.type;
          if (type) {
            type = type.toLowerCase();
          }
          if (types.indexOf(type) > -1) {
            doPrevent = false;
          }
        } else if (target.nodeName === 'TEXTAREA') {
          doPrevent = false;
        }
      }
      if (doPrevent) {
        evt.preventDefault();
        return false;
      }
    }
  }


}

/**
 * This function prevents files from being dropped into the browser window unless they are on
 * an element that specifically supports drop events.  Normal behavior of a file drop event is
 * for the browser to navigate to the dropped file.  This function prevents the user from losing
 * work in, say CapacitorForm, if they drag a photo to the wrong place on the page.
 * Credit: https://stackoverflow.com/a/47980517
 */
function preventWindowFileDrop(): void {
  window.addEventListener('dragover', e => {
    e && e.preventDefault();
  }, false);
  window.addEventListener('drop', e => {
    e && e.preventDefault();
  }, false);
}
