
export class FileReference {

  constructor(fileReference?: FileReference) {
    if (fileReference) {
      this.id = fileReference.id;
      this.url = fileReference.url;
    }
  }

  id: number;
  url: string;


  static fromUrl(url: string): FileReference {
    const fileReference = new FileReference();
    fileReference.url = url;
    return fileReference;
  }
}
