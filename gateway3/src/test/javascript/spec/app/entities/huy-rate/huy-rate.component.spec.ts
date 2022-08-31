import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Gateway3TestModule } from '../../../test.module';
import { HuyRateComponent } from 'app/entities/huy-rate/huy-rate.component';
import { HuyRateService } from 'app/entities/huy-rate/huy-rate.service';
import { HuyRate } from 'app/shared/model/huy-rate.model';

describe('Component Tests', () => {
  describe('HuyRate Management Component', () => {
    let comp: HuyRateComponent;
    let fixture: ComponentFixture<HuyRateComponent>;
    let service: HuyRateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyRateComponent]
      })
        .overrideTemplate(HuyRateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyRateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyRateService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HuyRate(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.huyRates && comp.huyRates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
