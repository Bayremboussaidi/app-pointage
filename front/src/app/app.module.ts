import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Angular Material imports
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';

// Angular Modules
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

// PrimeNG Modules
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

//Components
import { HeaderComponent } from './header/header.component';
import { TopComponent } from './top/top.component';
import { TableComponent } from './table/table.component';
//import { RowComponent } from './row/row.component';
import { ToolbarModule } from 'primeng/toolbar';
import { FileUploadModule } from 'primeng/fileupload';
import { KeyFilterModule } from 'primeng/keyfilter';
import { FilterComponent } from './filter/filter.component';
//import { TabletestComponent } from './tabletest/tabletest.component';


import { CalendarModule } from 'primeng/calendar';
import { SecondpComponent } from './secondp/secondp.component';
import { TablefComponent } from './tablef/tablef.component';




//import { JwtInterceptor } from './jwt.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    TopComponent,
    TableComponent,
    FilterComponent,
    SecondpComponent,
    TablefComponent
    //TabletestComponent
   // RowComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    InputTextModule,

    // Angular Material modules
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatCardModule,
    MatSelectModule,
    MatOptionModule,
    MatTableModule,
    MatDialogModule,


    // PrimeNG Modules
    TableModule,
    ToastModule,
    TagModule,
    DropdownModule,
    ButtonModule,
    InputTextModule,
    ToolbarModule,
    FileUploadModule,
    KeyFilterModule,
    CalendarModule,
    TableModule,
    BrowserAnimationsModule
  ],
  /*providers: [{
    provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true
  }]*/
  bootstrap: [AppComponent]
})
export class AppModule { }
