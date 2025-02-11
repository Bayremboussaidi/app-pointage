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
  personnelSummary: { id: string; name: string; totalHours: string }[] = []; // ✅ Changed 'idd' → 'id'
  id: string = '';  // ✅ Changed 'idd' → 'id'
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
        id: this.id.trim(),
        date1: new Date(this.date1).toISOString(),
        date2: new Date(this.date2).toISOString()
    };

    if (!this.id.trim()) {
        // ✅ Fetch all personnel records
        this.filterService.fetchAllRecords(requestPayload).subscribe(
            (response) => {
                if (response && response.records.length > 0) {
                    this.records = response.records.map((record: any) => ({
                        id: record.idd,  // ✅ Fix: Convert 'idd' to 'id'
                        prenom: record.prenom,
                        inTime: record.inTime,
                        outTime: record.outTime
                    }));

                    this.errorMessage = '';
                    this.generatePersonnelSummary(); // ✅ Regenerate summary
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
        this.filterService.fetchRecords(requestPayload).subscribe(
            (response) => {
                if (response && response.length > 0) {
                    this.records = response.map((record: any) => ({
                        id: record.idd,  // ✅ Fix: Convert 'idd' to 'id'
                        prenom: record.prenom,
                        inTime: record.inTime,
                        outTime: record.outTime
                    }));

                    this.personnelSummary = [];
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
    const summaryMap = new Map<string, { name: string; totalHours: number }>();

    this.records.forEach((record) => {
      const personnelId = record.id;  // ✅ Changed 'idd' → 'id'
      const personnelName = record.prenom; // ✅ Get name from records
      const hoursWorked = parseFloat(this.calculateHoursWorked(record.inTime, record.outTime).replace(' hrs', ''));

      if (!isNaN(hoursWorked)) {
        if (!summaryMap.has(personnelId)) {
          summaryMap.set(personnelId, { name: personnelName, totalHours: 0 });
        }
        summaryMap.get(personnelId)!.totalHours += hoursWorked;
      }
    });

    // ✅ Convert Map to array with name & total hours
    this.personnelSummary = Array.from(summaryMap, ([id, data]) => ({
      id,
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
