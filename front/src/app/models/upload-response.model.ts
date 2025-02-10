export interface InsertedRecord {
  nombreDuPersonnel: number;
  prenom: string;
  inTime: string;
  outTime: string;
}

export interface UploadResponse {
  insertedRecords: InsertedRecord[];
  errors: string[];
}




