import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Gateway3TestModule } from '../../../test.module';
import { DemoblobComponent } from 'app/entities/demoblob/demoblob.component';
import { DemoblobService } from 'app/entities/demoblob/demoblob.service';
import { Demoblob } from 'app/shared/model/demoblob.model';

describe('Component Tests', () => {
  describe('Demoblob Management Component', () => {
    let comp: DemoblobComponent;
    let fixture: ComponentFixture<DemoblobComponent>;
    let service: DemoblobService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [DemoblobComponent]
      })
        .overrideTemplate(DemoblobComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemoblobComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DemoblobService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Demoblob(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.demoblobs && comp.demoblobs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
