import { Component } from '@angular/core';
import { FilterService } from '../services/filter.service';
import { Record } from '../models/record.model';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent {
  dateStart: string = '';
  dateEnd: string = '';
  personnelNumber: number | null = null;
  filteredRecords: Record[] = [];
  errorMessage: string = ''; // ✅ Added error message handling

  constructor(private filterService: FilterService) {}

  filterRecords() {
    if (!this.dateStart || !this.dateEnd || this.personnelNumber === null) {
      this.errorMessage = 'Please fill all fields before filtering.';
      return;
    }

    const requestPayload = {
      personnel: this.personnelNumber,
      date1: new Date(this.dateStart).toISOString(), // ✅ Convert to ISO format
      date2: new Date(this.dateEnd).toISOString()
    };

    console.log("📤 Sending Request:", requestPayload);

    this.filterService.fetchRecords(requestPayload).subscribe(
      (response: Record[]) => { // ✅ Expecting response as an array
        console.log("✅ Response Received:", response);

        if (response.length > 0) {
          this.filteredRecords = response; // ✅ Directly store response (no `.records`)
          this.errorMessage = ''; // ✅ Clear error if successful
        } else {
          this.filteredRecords = [];
          this.errorMessage = 'No records found.';
        }
      },
      (error) => {
        console.error('❌ Error fetching records:', error);
        this.errorMessage = 'An error occurred while fetching records.';
      }
    );
  }
}
