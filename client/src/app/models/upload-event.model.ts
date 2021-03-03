export interface UploadEvent {
  /** Path on the server.  i.e. bucketName/folder1/folder2 */
  awsS3BucketDir: string;

  /** Name of the file on the server */
  filename: string;
}


export interface StartedUploadEvent extends UploadEvent {
  /** The file on the user's file system */
  file: File;
}


export interface FinishedUploadEvent extends UploadEvent {
  /** The url of the completely uploaded file */
  url: string;
}
