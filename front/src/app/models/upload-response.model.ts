export interface InsertedRecord {
  id: string;
  prenom: string;
  inTime: string;
  outTime: string;
}

export interface UploadResponse {
  insertedRecords: InsertedRecord[];
  errors: string[];
}




