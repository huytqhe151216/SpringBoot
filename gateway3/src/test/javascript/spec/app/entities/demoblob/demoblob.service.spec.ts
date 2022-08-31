import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DemoblobService } from 'app/entities/demoblob/demoblob.service';
import { IDemoblob, Demoblob } from 'app/shared/model/demoblob.model';

describe('Service Tests', () => {
  describe('Demoblob Service', () => {
    let injector: TestBed;
    let service: DemoblobService;
    let httpMock: HttpTestingController;
    let elemDefault: IDemoblob;
    let expectedResult: IDemoblob | IDemoblob[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DemoblobService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Demoblob(0, 'AAAAAAA', 'image/png', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should return a list of Demoblob', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            img: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
