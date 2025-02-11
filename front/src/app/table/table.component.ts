import { Component, OnInit } from '@angular/core';
import { UploadService } from '../services/upload.service';
import { InsertedRecord } from '../models/upload-response.model';

import { HttpClient } from '@angular/common/http';
import * as Papa from 'papaparse';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css'],
  standalone: false
})
export class TableComponent implements OnInit {
  insertedRecords: InsertedRecord[] = [];
  errors: string[] = [];
  isAddModalOpen = false;

  newRecord: InsertedRecord = {
    id: '',
    prenom: '',
    inTime: '',
    outTime: '',
  };

  constructor(private uploadService: UploadService, private http: HttpClient) {}

  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.uploadService.getStoredResponse().subscribe({
      next: (response) => {
        if (response?.insertedRecords) {
          this.insertedRecords = response.insertedRecords;
        } else {
          this.insertedRecords = [];
        }
        this.errors = response?.errors || [];
      },
      error: (err) => {
        console.error("❌ Error fetching data:", err);
        this.errors.push("Failed to fetch data.");
      }
    });
  }


  deleteRecord(record: InsertedRecord, index: number): void {
    if (!confirm('Are you sure you want to delete this record?')) return;

    this.uploadService.deletePersonnelRecord(record.id).subscribe({
      next: () => {
        this.insertedRecords.splice(index, 1);
      },
      error: (error) => {
        console.error('❌ Error deleting record:', error);
        this.errors.push(`Failed to delete record: ${error.message}`);
      }
    });
  }

  exportCSV() {
    if (this.insertedRecords.length === 0) {
      console.warn("⚠ No data to export.");
      return;
    }

    const csvData = Papa.unparse(this.insertedRecords);
    const blob = new Blob([csvData], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = 'exported_records.csv';
    link.click();
    URL.revokeObjectURL(url);
  }

  onUpdate(record: InsertedRecord) {
    console.log('🔄 Updating record:', record);
  }

  onDelete(record: InsertedRecord) {
    console.log('🗑 Deleting record:', record);
  }

  //  Open the Add Record modal
  openAddModal() {
    this.isAddModalOpen = true;
  }

  //  Close the Add Record modal
  closeAddModal() {
    this.isAddModalOpen = false;
    this.newRecord = {
      id: '',
      prenom: '',
      inTime: '',
      outTime: '',
    };
  }

  //  Handle form submission (Add new record)
  onAdd() {
    if (!this.newRecord.id || !this.newRecord.prenom || !this.newRecord.inTime || !this.newRecord.outTime) {
      this.errors.push('⚠ Please fill all fields.');
      return;
    }

    //  Convert date fields before sending
    const formattedRecord = {
      ...this.newRecord,
      inTime: this.formatDate(this.newRecord.inTime),
      outTime: this.formatDate(this.newRecord.outTime),
    };

    this.uploadService.addRecord(formattedRecord).subscribe({
      next: (response) => {
        console.log('Record added successfully:', response);
        this.closeAddModal();
        this.fetchData(); //  Refresh data after adding
      },
      error: (error) => {
        console.error('❌ Error adding record:', error);
        this.errors.push('Failed to add record.');
      }
    });
  }

  //  Helper function to format date as 'YYYY-MM-DDTHH:mm:ss'
  formatDate(date: string): string {
    if (!date) return '';

    const parsedDate = new Date(date);
    if (isNaN(parsedDate.getTime())) {
      console.error('⚠ Invalid date:', date);
      return '';
    }

    return parsedDate.toISOString().slice(0, 19); //  Ensures correct format
  }

  // Handle CSV file input
  handleFileInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files ? input.files[0] : null;

    if (file) {
      Papa.parse(file, {
        header: true,
        skipEmptyLines: true,
        complete: (result: { data: any[] }) => {
          console.log('📥 Parsed CSV Data:', result.data);
          this.insertedRecords = result.data;
          this.uploadService.storeParsedData(result.data);
        },
        error: (err) => {
          console.error('❌ Error parsing CSV:', err);
        }
      });
    }
  }
}
