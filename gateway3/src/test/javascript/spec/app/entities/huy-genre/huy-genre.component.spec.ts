import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Gateway3TestModule } from '../../../test.module';
import { HuyGenreComponent } from 'app/entities/huy-genre/huy-genre.component';
import { HuyGenreService } from 'app/entities/huy-genre/huy-genre.service';
import { HuyGenre } from 'app/shared/model/huy-genre.model';

describe('Component Tests', () => {
  describe('HuyGenre Management Component', () => {
    let comp: HuyGenreComponent;
    let fixture: ComponentFixture<HuyGenreComponent>;
    let service: HuyGenreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyGenreComponent]
      })
        .overrideTemplate(HuyGenreComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyGenreComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyGenreService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HuyGenre(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.huyGenres && comp.huyGenres[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
