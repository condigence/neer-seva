import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CarasualComponent } from './carasual.component';

describe('OrderComponent', () => {
  let component: CarasualComponent;
  let fixture: ComponentFixture<CarasualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CarasualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarasualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
