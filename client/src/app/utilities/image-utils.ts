
require('node_modules/pixl-canvas-plus/canvas-plus.js');  // Accessing node_modules directly may be dubious...



/**
 * Convert a Blob image file to JPG.  Supports JPEG, PNG, GIF, BMP, WebP.
 * @param file in one of the formats JPEG, PNG, GIF, BMP, WebP
 * @param jpegQuality a number from 0 to 100
 * @param width the size of the final image
 */
export async function scaleImageToSize(file: Blob, jpegQuality: number, width = 256): Promise<Blob> {
  const canvasPlus = new (window as any).CanvasPlus();

  await canvasLoadFile(canvasPlus, file);

  // Test if new css property image-orientation is supported in this browser.
  // If it is, don't rotate based on EXIF values, because the browser will do this automatically, and it will be rotated twice.
  if (document.createElement('img').style.imageOrientation !== undefined) {
    canvasPlus.set( 'autoOrient', false );
  }

  canvasPlus.resize({
    width,
    mode: 'fit',
    background: '#ffffff',
  });

  const buffer = await canvasWriteFile(canvasPlus, {format: 'jpeg', quality: jpegQuality});

  return new Blob( [ buffer ], { type: 'image/jpeg' } );
}


/**
 * Wraps CanvasPlus load function to use async/await
 * @param canvasPlus instance of CanvasPlus library
 * @param file the File or Blob to load into the canvas
 */
function canvasLoadFile(canvasPlus, file: Blob): Promise<void> {
  return new Promise((resolve, reject) => {
    canvasPlus.load( file, (err) => {
      if (err) {reject(err); }
      resolve();
    });
  });
}

/**
 * Wraps CanvasPlus write function to use async/await
 * @param canvasPlus instance of CanvasPlus library
 * @param params an object containing the configuration of the file to be created.  i.e. {format: 'jpeg', quality: 50}
 */
function canvasWriteFile(canvasPlus, params: object): Promise<Buffer> {
  return new Promise((resolve, reject) => {
    canvasPlus.write(params, (err, buf) => {
      if (err) {reject(err); }
      resolve(buf);
    });
  });
}
