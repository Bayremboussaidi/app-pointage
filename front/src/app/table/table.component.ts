import { Component, OnInit } from '@angular/core';
import { UploadService } from '../services/upload.service';
import { InsertedRecord } from '../models/upload-response.model';

import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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
    nombreDuPersonnel: 1,
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
        this.insertedRecords = response.insertedRecords;
        this.errors = response.errors;
      },
      error: (err) => {
        console.error("Erreur de récupération des données:", err);
      }
    });
  }

  // Delete function
  deleteRecord(record: any, index: number): void {
    if (!confirm('Are you sure you want to delete this record?')) return;

    this.uploadService.deletePersonnelRecord(record.nombreDuPersonnel).subscribe({
      next: () => {
        this.insertedRecords.splice(index, 1); // Remove from UI
      },
      error: (error) => {
        this.errors.push(`Failed to delete record: ${error.message}`);
      }
    });
  }

  exportCSV() {
    if (this.insertedRecords.length === 0) {
      console.warn("No data to export.");
      return;
    }

    const csvData = Papa.unparse(this.insertedRecords);
    const blob = new Blob([csvData], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');

    const url = URL.createObjectURL(blob);
    link.setAttribute('href', url);
    link.setAttribute('download', 'exported_records.csv');
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  onUpdate(record: any) {
    console.log('Update record:', record);
  }

  onDelete(record: any) {
    console.log('Delete record:', record);
  }

  // Open the Add Record modal
  openAddModal() {
    this.isAddModalOpen = true;
  }

  // Close the Add Record modal
  closeAddModal() {
    this.isAddModalOpen = false;
    this.newRecord = {
      nombreDuPersonnel: 1,
      prenom: '',
      inTime: '',
      outTime: '',
    };
  }

  // Handle form submission (Add new record)
  onAdd() {
    if (!this.newRecord.nombreDuPersonnel || !this.newRecord.prenom || !this.newRecord.inTime || !this.newRecord.outTime) {
      this.errors.push('Please fill all fields.');
      return;
    }

    // Convert date fields before sending
    const formattedRecord = {
      ...this.newRecord,
      inTime: this.formatDate(this.newRecord.inTime),
      outTime: this.formatDate(this.newRecord.outTime),
    };

    this.uploadService.addRecord(formattedRecord).subscribe(
      (response: any) => {
        console.log('Record added successfully:', response);
        this.closeAddModal();
        this.fetchData(); // Refresh data after adding
      },
      (error: any) => {
        console.error('Error adding record:', error);
        this.errors.push('Failed to add record.');
      }
    );
  }

  // Helper function to format date as 'YYYY-MM-DDTHH:mm:ss'
  formatDate(date: string): string {
    if (!date) return '';

    const parsedDate = new Date(date);
    if (isNaN(parsedDate.getTime())) {
      console.error('Invalid date:', date);
      return '';
    }

    const year = parsedDate.getFullYear();
    const month = String(parsedDate.getMonth() + 1).padStart(2, '0');
    const day = String(parsedDate.getDate()).padStart(2, '0');
    const hours = String(parsedDate.getHours()).padStart(2, '0');
    const minutes = String(parsedDate.getMinutes()).padStart(2, '0');
    const seconds = String(parsedDate.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }

  handleFileInput(event: any) {
    const file = event.target.files[0];

    if (file) {
      Papa.parse(file, {
        header: true,
        skipEmptyLines: true,
        complete: (result: { data: any }) => {
          console.log('Parsed CSV Data:', result.data);
          this.insertedRecords = result.data;
          this.uploadService.storeParsedData(result.data);
        },
        error: (err) => {
          console.error('Error parsing CSV:', err);
        }
      });
    }
  }
}
