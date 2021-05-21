
export function caseInsensitiveCompare(a: string, b: string): number {
  const nameA = a.toUpperCase(); // ignore upper and lowercase
  const nameB = b.toUpperCase(); // ignore upper and lowercase
  if (nameA < nameB) {
    return -1;
  }
  if (nameA > nameB) {
    return 1;
  }
  // names must be equal
  return 0;
}

/**
 * Like String.prototype.padEnd but adds ' \u200B' to strings instead of spaces.  U+200B is a blank unicode character
 * which tricks html to add multiple consecutive spaces.
 * @param str string to pad
 * @param targetLength The length of the resulting string once the current str has been padded.
 * If the value is less than str.length, then str is returned as-is.
 */
export function padEndHtml(str: string, targetLength: number): string {
  return str + ' \u200B'.repeat(targetLength - str.length);
}

/**
 * Like String.prototype.padEnd but adds ' \u200B' to strings instead of spaces.  U+200B is a blank unicode character
 * which tricks html to add multiple consecutive spaces.
 * @param str string to pad
 * @param targetLength The length of the resulting string once the current str has been padded.
 * If the value is less than str.length, then str is returned as-is.
 */
export function padStartHtml(str: string, targetLength: number): string {
  return ' \u200B'.repeat(targetLength - str.length) + str;
}

/**
 * Generate a string of random characters in range [a-z][A-Z][0-9]
 * @param length the length of the random string
 */
export function randomString(length: number): string {
  let result = '';
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  for ( let i = 0; i < length; i++ ) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
}

/**
 * Takes a string and capitalizes the first letter between each deliminator
 * @param str the string to make a title
 * @param deliminators An array of characters.  This could be [' ', '-'] that would capitalize words separated by
 *                     spaces and dashes.
 * @return capitalized string
 */
export function  title(str: string, deliminators?: Array<string>): string {
  if (!deliminators) {deliminators = [' ']; }

  const deliminatorSet = new Set<string>(deliminators);
  const strArr = str.toLowerCase().split('');
  let previousCharIsLetter = false;
  for (let i = 0; i < str.length; i++) {
    if (!previousCharIsLetter && isLetter(strArr[i])) {
      strArr[i] = strArr[i].toUpperCase();
      previousCharIsLetter = true;
    } else if (deliminatorSet.has(strArr[i])) {
      previousCharIsLetter = false;
    }
  }
  return strArr.join('');

}

function isLetter(c): boolean {
  return c.toLowerCase() !== c.toUpperCase();
}


