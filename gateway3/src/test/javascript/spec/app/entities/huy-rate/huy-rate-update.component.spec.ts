import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Gateway3TestModule } from '../../../test.module';
import { HuyRateUpdateComponent } from 'app/entities/huy-rate/huy-rate-update.component';
import { HuyRateService } from 'app/entities/huy-rate/huy-rate.service';
import { HuyRate } from 'app/shared/model/huy-rate.model';

describe('Component Tests', () => {
  describe('HuyRate Management Update Component', () => {
    let comp: HuyRateUpdateComponent;
    let fixture: ComponentFixture<HuyRateUpdateComponent>;
    let service: HuyRateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [HuyRateUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HuyRateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HuyRateUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HuyRateService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HuyRate(123);
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
        const entity = new HuyRate();
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
