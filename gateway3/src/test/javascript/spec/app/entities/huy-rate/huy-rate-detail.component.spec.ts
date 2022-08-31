import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyRateDetailComponent } from 'app/entities/huy-rate/huy-rate-detail.component';
import { HuyRate } from 'app/shared/model/huy-rate.model';

describe('Component Tests', () => {
  describe('HuyRate Management Detail Component', () => {
    let comp: HuyRateDetailComponent;
    let fixture: ComponentFixture<HuyRateDetailComponent>;
    const route = ({ data: of({ huyRate: new HuyRate(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyRateDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HuyRateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HuyRateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load huyRate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.huyRate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
