
require('./canvas-plus.js');



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
  // If it is, don't rotate based on EXIF Orientation values, because the browser will do this automatically,
  // and it will be rotated twice.
  if (browserSupportsAutomaticImageOrientation()) {
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
 * Returns true if the browser supports the CSS3 property image-orientation.
 * If this property is supported, it causes canvas images with an EXIF Orientation property > 1 to
 * be rotated automatically (which needs to be compensated for by canvas-plus).
 */
export function browserSupportsAutomaticImageOrientation(): boolean {
  return BROWSER_SUPPORTS_AUTOMATIC_IMAGE_ORIENTATION;
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


// Code to test if the browser supports the CSS3 property image-orientation which would cause images with
// EXIF Orientation properties to be automatically rotated.
//
// Code is written in the global scope so it is only executed once.
//
// Credit to the JavaScript-Load-Image library for this workaround:
// https://github.com/blueimp/JavaScript-Load-Image/compare/v2.28.0...v2.29.0

let BROWSER_SUPPORTS_AUTOMATIC_IMAGE_ORIENTATION;

const TEST_IMAGE_BASE64_URL =
  'data:image/jpeg;base64,/9j/4QAiRXhpZgAATU0AKgAAAAgAAQESAAMAAAABAAYAAAA' +
  'AAAD/2wCEAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBA' +
  'QEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQE' +
  'BAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAf/AABEIAAEAAgMBEQACEQEDEQH/x' +
  'ABKAAEAAAAAAAAAAAAAAAAAAAALEAEAAAAAAAAAAAAAAAAAAAAAAQEAAAAAAAAAAAAAAAA' +
  'AAAAAEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwA/8H//2Q==';

const TEST_IMG = document.createElement('img');
TEST_IMG.onload = () => {
  // Check if browser supports automatic image orientation:
  BROWSER_SUPPORTS_AUTOMATIC_IMAGE_ORIENTATION = TEST_IMG.width === 1 && TEST_IMG.height === 2;
};

TEST_IMG.src = TEST_IMAGE_BASE64_URL;
