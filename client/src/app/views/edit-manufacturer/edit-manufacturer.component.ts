import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {Title} from '@angular/platform-browser';
import {title} from '../../utilities/text-utils';
import {ErrorHandlerService} from '../../services/error-handler/error-handler.service';

@Component({
  selector: 'app-edit-manufacturer',
  templateUrl: './edit-manufacturer.component.html',
  styleUrls: ['./edit-manufacturer.component.css']
})
export class EditManufacturerComponent implements OnInit {

  companyName: string;
  manufacturer$: Manufacturer;

  constructor(private titleService: Title, private restService: RestService, private activatedRoute: ActivatedRoute,
              private changeDetectorRef: ChangeDetectorRef, private errorHandler: ErrorHandlerService) {
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
  }

  ngOnInit(): Subscription {
    this.titleService.setTitle('Editing ' + title(this.companyName));

    return this.restService.getManufacturerByName(this.companyName).subscribe({
      next: manufacturer => {
        this.manufacturer$ = manufacturer;
        this.changeDetectorRef.detectChanges();
      },
      error: err => this.errorHandler.handleGetRequestError(err, 'Could not get Manufacturer to edit.'),
    });
  }

}
