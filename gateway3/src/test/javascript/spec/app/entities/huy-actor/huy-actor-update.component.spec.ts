import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyActorUpdateComponent } from 'app/entities/huy-actor/huy-actor-update.component';
import { HuyActorService } from 'app/entities/huy-actor/huy-actor.service';
import { HuyActor } from 'app/shared/model/huy-actor.model';

describe('Component Tests', () => {
  describe('HuyActor Management Update Component', () => {
    let comp: HuyActorUpdateComponent;
    let fixture: ComponentFixture<HuyActorUpdateComponent>;
    let service: HuyActorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyActorUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HuyActorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyActorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyActorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HuyActor(123);
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
        const entity = new HuyActor();
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
