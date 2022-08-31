import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { Gateway3TestModule } from '../../../test.module';
import { DemoblobDetailComponent } from 'app/entities/demoblob/demoblob-detail.component';
import { Demoblob } from 'app/shared/model/demoblob.model';

describe('Component Tests', () => {
  describe('Demoblob Management Detail Component', () => {
    let comp: DemoblobDetailComponent;
    let fixture: ComponentFixture<DemoblobDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ demoblob: new Demoblob(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Gateway3TestModule],
        declarations: [DemoblobDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DemoblobDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DemoblobDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load demoblob on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.demoblob).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
