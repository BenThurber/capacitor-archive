
export class FileReference {

  id: number;
  url: string;

  constructor(fileReference?: FileReference) {
    if (fileReference) {
      this.id = fileReference.id;
      this.url = fileReference.url;
    }
  }


  static fromUrl(url: string): FileReference {
    const fileReference = new FileReference();
    fileReference.url = url;
    return fileReference;
  }
}
