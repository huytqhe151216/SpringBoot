import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Gateway3TestModule } from '../../../test.module';
import { HuyMovieComponent } from 'app/entities/huy-movie/huy-movie.component';
import { HuyMovieService } from 'app/entities/huy-movie/huy-movie.service';
import { HuyMovie } from 'app/shared/model/huy-movie.model';

describe('Component Tests', () => {
  describe('HuyMovie Management Component', () => {
    let comp: HuyMovieComponent;
    let fixture: ComponentFixture<HuyMovieComponent>;
    let service: HuyMovieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyMovieComponent]
      })
        .overrideTemplate(HuyMovieComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyMovieComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyMovieService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HuyMovie(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.huyMovies && comp.huyMovies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
