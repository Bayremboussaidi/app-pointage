import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablefComponent } from './tablef.component';

describe('TablefComponent', () => {
  let component: TablefComponent;
  let fixture: ComponentFixture<TablefComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TablefComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TablefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
