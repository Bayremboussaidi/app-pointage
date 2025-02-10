import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Record } from '../models/record.model';

@Injectable({
  providedIn: 'root'
})
export class FilterService {
  private apiUrl = 'http://localhost:8080/api/point/fetch';
  private records: Record[] = [];

  constructor(private http: HttpClient) {}

  fetchRecords(requestPayload: any): Observable<Record[]> { //  Expecting an array
    console.log(" Sending request to:", this.apiUrl, "with payload:", requestPayload);
    return this.http.post<Record[]>(this.apiUrl, requestPayload); //  Expect response as an array
  }

  setRecords(records: Record[]) {
    this.records = records;
  }

  fetchAllRecords(requestPayload: { date1: string; date2: string }): Observable<{ records: Record[] }> {
    return this.http.post<{ records: Record[] }>(`${this.apiUrl}/all`, requestPayload);
  }

  getRecords(): Record[] {
    return this.records;
  }
}
