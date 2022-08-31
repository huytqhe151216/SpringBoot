import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyGenreUpdateComponent } from 'app/entities/huy-genre/huy-genre-update.component';
import { HuyGenreService } from 'app/entities/huy-genre/huy-genre.service';
import { HuyGenre } from 'app/shared/model/huy-genre.model';

describe('Component Tests', () => {
  describe('HuyGenre Management Update Component', () => {
    let comp: HuyGenreUpdateComponent;
    let fixture: ComponentFixture<HuyGenreUpdateComponent>;
    let service: HuyGenreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyGenreUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HuyGenreUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyGenreUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyGenreService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HuyGenre(123);
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
        const entity = new HuyGenre();
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
