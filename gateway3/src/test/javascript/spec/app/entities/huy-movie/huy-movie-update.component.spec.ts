import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyMovieUpdateComponent } from 'app/entities/huy-movie/huy-movie-update.component';
import { HuyMovieService } from 'app/entities/huy-movie/huy-movie.service';
import { HuyMovie } from 'app/shared/model/huy-movie.model';

describe('Component Tests', () => {
  describe('HuyMovie Management Update Component', () => {
    let comp: HuyMovieUpdateComponent;
    let fixture: ComponentFixture<HuyMovieUpdateComponent>;
    let service: HuyMovieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyMovieUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HuyMovieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyMovieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyMovieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HuyMovie(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new HuyMovie();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
