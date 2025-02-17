export interface InsertedRecord {
  idd:string,
  id: number;
  prenom: string;
  inTime: string;
  outTime: string;
}

export interface UploadResponse {
  insertedRecords: InsertedRecord[];
  errors: string[];
}




