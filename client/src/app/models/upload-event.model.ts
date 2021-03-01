export interface UploadEvent {
  /** Path to the file from the server including the filename */
  serverPath: string;

  file: File;
}


export interface StartedUploadEvent extends UploadEvent {
}


export interface FinishedUploadEvent extends UploadEvent {
  /** The url of the completely uploaded file */
  url: string;
}
