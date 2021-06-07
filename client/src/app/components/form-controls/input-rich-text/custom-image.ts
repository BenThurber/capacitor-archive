/**
 * Credit to https://github.com/Fandom-OSS/quill-blot-formatter/issues/5#issuecomment-670696134
 * Creates a custom Image for Quill that accepts CSS classes and styles.
 * This fixes Issue #18 where images that were saved with an alignment (left, right, centre) lost their alignment
 * when edited again.
 */

import Quill from 'quill';

// Had to get the class this way, instead of ES6 imports, so that quill could register it without errors
const Image = Quill.import('formats/image');

const ATTRIBUTES = [
  'alt',
  'height',
  'width',
  'class',
  'style', // Had to add this line because the style was inlined
];

class CustomImage extends Image {
  static formats(domNode): any {
    return ATTRIBUTES.reduce((formats, attribute) => {
      const copy = { ...formats };

      if (domNode.hasAttribute(attribute)) {
        copy[attribute] = domNode.getAttribute(attribute);
      }

      return copy;
    }, {});
  }

  format(name, value): any {
    if (ATTRIBUTES.indexOf(name) > -1) {
      if (value) {
        (this as any).domNode.setAttribute(name, value);
      } else {
        (this as any).domNode.removeAttribute(name);
      }
    } else {
      super.format(name, value);
    }
  }
}

export default CustomImage;
