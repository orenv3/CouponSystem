import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowMyCompanyComponent } from './show-my-company.component';

describe('ShowMyCompanyComponent', () => {
  let component: ShowMyCompanyComponent;
  let fixture: ComponentFixture<ShowMyCompanyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowMyCompanyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowMyCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
