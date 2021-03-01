
export interface StartedUploadEvent {
  /** Path to the file from the server including the filename */
  serverPath: string;
  file: File;
}
