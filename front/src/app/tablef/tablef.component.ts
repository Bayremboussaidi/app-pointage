/*import { Component, OnInit } from '@angular/core';
import { FilterService } from '../services/filter.service';
    import { Record } from '../models/record.model';
    import * as XLSX from 'xlsx'; // ✅ Import xlsx for Excel
    import * as FileSaver from 'file-saver'; // ✅ Import FileSaver for file download

    @Component({
      selector: 'app-tablef',
      templateUrl: './tablef.component.html',
      styleUrls: ['./tablef.component.css']
    })
    export class TablefComponent implements OnInit {
      records: Record[] = [];
      personnel: number = 0;
      date1: string = '';
      date2: string = '';
      errorMessage: string = '';

      constructor(private filterService: FilterService) {}

      ngOnInit() {
        this.records = this.filterService.getRecords();
      }




      calculateHoursWorked(inTime: string, outTime: string): string {
        if (!inTime || !outTime) return 'N/A';

        const inDate = new Date(inTime);
        const outDate = new Date(outTime);

        if (isNaN(inDate.getTime()) || isNaN(outDate.getTime())) return 'Invalid';

        const diffMs = outDate.getTime() - inDate.getTime(); // Difference in milliseconds
        const diffHours = diffMs / (1000 * 60 * 60); // Convert to hours

        return diffHours.toFixed(2) + ' hrs'; // Return formatted hours
      }

      fetchRecords() {
          if (!this.date1 || !this.date2) {
            this.errorMessage = 'Please fill in all fields.';
            return;
          }

          const requestPayload = {
            personnel: this.personnel,
            date1: new Date(this.date1).toISOString(),
            date2: new Date(this.date2).toISOString()
          };

          if (this.personnel === 0) {
            // ✅ Call fetchAllRecords() when personnel = 0
            this.filterService.fetchAllRecords(requestPayload).subscribe(
              (response) => {
                if (response && response.records.length > 0) {
                  this.filterService.setRecords(response.records);
                  this.records = this.filterService.getRecords();
                  this.errorMessage = '';
                } else {
                  this.records = [];
                  this.errorMessage = 'No records found.';
                }
              },
              (error) => {
                console.error('Error fetching records:', error);
                this.errorMessage = 'An error occurred while fetching records.';
              }
            );
          } else {
            // ✅ Call fetchRecords() when personnel is provided
            requestPayload['personnel'] = this.personnel;
            this.filterService.fetchRecords(requestPayload).subscribe(
              (response) => {
                if (response && response.length > 0) {
                  this.filterService.setRecords(response);
                  this.records = this.filterService.getRecords();
                  this.errorMessage = '';
                } else {
                  this.records = [];
                  this.errorMessage = 'No records found.';
                }
              },
              (error) => {
                console.error('Error fetching records:', error);
                this.errorMessage = 'An error occurred while fetching records.';
              }
            );
          }
        }





      getTotalHoursWorked(): string {
          let totalHours = this.records.reduce((sum, record) => {
            let hoursWorked = parseFloat(this.calculateHoursWorked(record.inTime, record.outTime).replace(' hrs', ''));
            return isNaN(hoursWorked) ? sum : sum + hoursWorked;
          }, 0);

          return totalHours.toFixed(2); // ✅ Return total hours formatted to 2 decimal places
      }



      // Function to Export Data to Excel
      exportToExcel() {
        console.log("Excel Export Triggered");

        if (this.records.length === 0) {
          console.warn("No data to export.");
          return;
        }

        const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.records);
        const workbook: XLSX.WorkBook = { Sheets: { 'Records': worksheet }, SheetNames: ['Records'] };
        const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

        // ✅ Convert and Save as Excel file
        const data: Blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });
        FileSaver.saveAs(data, 'exported_records.xlsx');
      }
}
*/



import { Component, OnInit } from '@angular/core';
import { FilterService } from '../services/filter.service';
import { Record } from '../models/record.model';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-tablef',
  templateUrl: './tablef.component.html',
  styleUrls: ['./tablef.component.css']
})
export class TablefComponent implements OnInit {
  records: Record[] = [];
  personnelSummary: { personnel: number; totalHours: string }[] = []; // ✅ Summary table data
  personnel: number = 0;
  date1: string = '';
  date2: string = '';
  errorMessage: string = '';

  constructor(private filterService: FilterService) {}

  ngOnInit() {
    this.records = this.filterService.getRecords();
  }

  calculateHoursWorked(inTime: string, outTime: string): string {
    if (!inTime || !outTime) return 'N/A';

    const inDate = new Date(inTime);
    const outDate = new Date(outTime);

    if (isNaN(inDate.getTime()) || isNaN(outDate.getTime())) return 'Invalid';

    const diffMs = outDate.getTime() - inDate.getTime();
    const diffHours = diffMs / (1000 * 60 * 60);

    return diffHours.toFixed(2) + ' hrs';
  }

  fetchRecords() {
    if (!this.date1 || !this.date2) {
      this.errorMessage = 'Please fill in all fields.';
      return;
    }

    const requestPayload = {
      personnel: this.personnel,
      date1: new Date(this.date1).toISOString(),
      date2: new Date(this.date2).toISOString()
    };

    if (this.personnel === 0) {
      // ✅ Fetch all personnel records
      this.filterService.fetchAllRecords(requestPayload).subscribe(
        (response) => {
          if (response && response.records.length > 0) {
            this.records = response.records;
            this.errorMessage = '';

            // ✅ Generate summary table for total hours per personnel
            this.generatePersonnelSummary();
          } else {
            this.records = [];
            this.personnelSummary = [];
            this.errorMessage = 'No records found.';
          }
        },
        (error) => {
          console.error('Error fetching records:', error);
          this.errorMessage = 'An error occurred while fetching records.';
        }
      );
    } else {
      // ✅ Fetch specific personnel records
      requestPayload['personnel'] = this.personnel;
      this.filterService.fetchRecords(requestPayload).subscribe(
        (response) => {
          if (response && response.length > 0) {
            this.records = response;
            this.personnelSummary = []; // ✅ Clear summary table for single personnel search
            this.errorMessage = '';
          } else {
            this.records = [];
            this.errorMessage = 'No records found.';
          }
        },
        (error) => {
          console.error('Error fetching records:', error);
          this.errorMessage = 'An error occurred while fetching records.';
        }
      );
    }
  }

  // ✅ Generate total hours per personnel (only for fetchAll)
  generatePersonnelSummary() {
    const summaryMap = new Map<number, { name: string; totalHours: number }>();

    this.records.forEach((record) => {
      const personnelNumber = record.nombreDuPersonnel;
      const personnelName = record.prenom; // ✅ Get name from records
      const hoursWorked = parseFloat(this.calculateHoursWorked(record.inTime, record.outTime).replace(' hrs', ''));

      if (!isNaN(hoursWorked)) {
        if (!summaryMap.has(personnelNumber)) {
          summaryMap.set(personnelNumber, { name: personnelName, totalHours: 0 });
        }
        summaryMap.get(personnelNumber)!.totalHours += hoursWorked;
      }
    });

    // ✅ Convert Map to array with name & total hours
    this.personnelSummary = Array.from(summaryMap, ([personnel, data]) => ({
      personnel,
      name: data.name, // ✅ Include name
      totalHours: data.totalHours.toFixed(2) // Format to 2 decimal places
    }));
  }


  getTotalHoursWorked(): string {
    let totalHours = this.records.reduce((sum, record) => {
      let hoursWorked = parseFloat(this.calculateHoursWorked(record.inTime, record.outTime).replace(' hrs', ''));
      return isNaN(hoursWorked) ? sum : sum + hoursWorked;
    }, 0);

    return totalHours.toFixed(2);
  }

  exportToExcel() {
    console.log("Excel Export Triggered");

    if (this.records.length === 0) {
      console.warn("No data to export.");
      return;
    }

    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.records);
    const workbook: XLSX.WorkBook = { Sheets: { 'Records': worksheet }, SheetNames: ['Records'] };
    const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

    const data: Blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });
    FileSaver.saveAs(data, 'exported_records.xlsx');
  }
}


















