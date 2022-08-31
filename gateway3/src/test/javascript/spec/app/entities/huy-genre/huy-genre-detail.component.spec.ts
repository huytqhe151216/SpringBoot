import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyGenreDetailComponent } from 'app/entities/huy-genre/huy-genre-detail.component';
import { HuyGenre } from 'app/shared/model/huy-genre.model';

describe('Component Tests', () => {
  describe('HuyGenre Management Detail Component', () => {
    let comp: HuyGenreDetailComponent;
    let fixture: ComponentFixture<HuyGenreDetailComponent>;
    const route = ({ data: of({ huyGenre: new HuyGenre(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyGenreDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HuyGenreDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HuyGenreDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load huyGenre on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.huyGenre).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
