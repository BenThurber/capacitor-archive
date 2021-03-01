
export interface FinishedUploadEvent {
  /** Path to the file from the server including the filename */
  serverPath: string;
  url: string;
  file: File;
}
