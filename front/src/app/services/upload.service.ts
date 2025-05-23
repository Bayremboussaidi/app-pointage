import { Record } from './../models/record.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { InsertedRecord, UploadResponse } from '../models/upload-response.model';
import { HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private apiUrl = 'http://localhost:8080/api/excel';

  // Store the response to make it available to other components
  private responseSubject = new BehaviorSubject<UploadResponse>({ insertedRecords: [], errors: [] });

  constructor(private http: HttpClient) {}

  uploadFile(file: File): Observable<UploadResponse> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<UploadResponse>(this.apiUrl, formData).pipe(
      map(response => {
        this.responseSubject.next(response); // Store response for other components
        return response;
      }),
      catchError(this.handleError)
    );
  }

  getStoredResponse(): Observable<UploadResponse> {
    return this.responseSubject.asObservable();
  }

  storeParsedData(data: InsertedRecord[]): void {
    const updatedResponse: UploadResponse = { insertedRecords: data, errors: [] };
    this.responseSubject.next(updatedResponse); // ✅ Update response
  }

  addRecord(record: Record): Observable<Record> {
    return this.http.post<Record>(`http://localhost:8080/api/records/add`, record);
  }


  deletePersonnelRecord(idd: number): Observable<string> {
    const url = `http://localhost:8080/api/records/delete/${idd}`;
    return this.http.delete<string>(url, { responseType: 'text' as 'json' });
  }


  //update
  updateRecord(id: number, updatedRecord: Record): Observable<Record> {
    const url = `http://localhost:8080/api/records/update/${id}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.put<Record>(url, updatedRecord, { headers });
  }



  private handleError(error: HttpErrorResponse): Observable<UploadResponse> {
    console.error("Erreur d'upload:", error);
    return throwError(() => ({
      insertedRecords: [],
      errors: ['Une erreur est survenue lors du téléchargement du fichier.']
    }));
  }
}
