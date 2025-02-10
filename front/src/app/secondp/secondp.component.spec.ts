import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SecondpComponent } from './secondp.component';

describe('SecondpComponent', () => {
  let component: SecondpComponent;
  let fixture: ComponentFixture<SecondpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SecondpComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SecondpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
