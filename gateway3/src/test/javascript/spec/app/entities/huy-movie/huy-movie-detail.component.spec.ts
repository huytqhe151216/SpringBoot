import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyMovieDetailComponent } from 'app/entities/huy-movie/huy-movie-detail.component';
import { HuyMovie } from 'app/shared/model/huy-movie.model';

describe('Component Tests', () => {
  describe('HuyMovie Management Detail Component', () => {
    let comp: HuyMovieDetailComponent;
    let fixture: ComponentFixture<HuyMovieDetailComponent>;
    const route = ({ data: of({ huyMovie: new HuyMovie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyMovieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HuyMovieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HuyMovieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load huyMovie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.huyMovie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
