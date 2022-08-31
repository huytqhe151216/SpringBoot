import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { HuyActorService } from 'app/entities/huy-actor/huy-actor.service';
import { IHuyActor, HuyActor } from 'app/shared/model/huy-actor.model';

describe('Service Tests', () => {
  describe('HuyActor Service', () => {
    let injector: TestBed;
    let service: HuyActorService;
    let httpMock: HttpTestingController;
    let elemDefault: IHuyActor;
    let expectedResult: IHuyActor | IHuyActor[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(HuyActorService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new HuyActor(0, 'AAAAAAA', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dob: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a HuyActor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dob: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dob: currentDate
          },
          returnedFromService
        );

        service.create(new HuyActor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a HuyActor', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            dob: currentDate.format(DATE_TIME_FORMAT),
            nationality: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dob: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of HuyActor', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            dob: currentDate.format(DATE_TIME_FORMAT),
            nationality: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dob: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a HuyActor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
