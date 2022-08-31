import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyActorDetailComponent } from 'app/entities/huy-actor/huy-actor-detail.component';
import { HuyActor } from 'app/shared/model/huy-actor.model';

describe('Component Tests', () => {
  describe('HuyActor Management Detail Component', () => {
    let comp: HuyActorDetailComponent;
    let fixture: ComponentFixture<HuyActorDetailComponent>;
    const route = ({ data: of({ huyActor: new HuyActor(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyActorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HuyActorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HuyActorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load huyActor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.huyActor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
