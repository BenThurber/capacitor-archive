/**
 * Scrolls to an html element
 * @param $element an Element type
 * @param behavior either 'auto' or 'smooth', defaults to 'smooth'
 */
export function scrollToElement($element, behavior: 'auto' | 'smooth' = 'auto'): void {
  $element.scrollIntoView({behavior, block: 'nearest', inline: 'nearest'});
}
