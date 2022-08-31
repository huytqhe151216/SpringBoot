import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Gateway3TestModule } from '../../../test.module';
import { HuyActorComponent } from 'app/entities/huy-actor/huy-actor.component';
import { HuyActorService } from 'app/entities/huy-actor/huy-actor.service';
import { HuyActor } from 'app/shared/model/huy-actor.model';

describe('Component Tests', () => {
  describe('HuyActor Management Component', () => {
    let comp: HuyActorComponent;
    let fixture: ComponentFixture<HuyActorComponent>;
    let service: HuyActorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyActorComponent]
      })
        .overrideTemplate(HuyActorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyActorComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyActorService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HuyActor(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.huyActors && comp.huyActors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
