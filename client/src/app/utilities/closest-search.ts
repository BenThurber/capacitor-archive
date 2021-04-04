
/**
 * Given an array of items and a target, finds the item is that is closest to the target.  Performs a modified linear
 * search on an unsorted array.
 * @param target property searching for
 * @param items an array of items
 * @param accessor a function to manipulate an item to then compare to the target
 */
export function closestSearch<T>(target: any, items: Array<T>, accessor?: (item: T) => any): T {

  if (items.length <= 0) {return null; }
  if (!accessor) {accessor = (item: any) => item; }

  let bestIndex;
  let bestDelta = Infinity;
  let delta;
  for (const [i, item] of items.entries()) {
    delta = Math.abs(accessor(item) - target);
    if (delta < bestDelta) {
      bestDelta = delta;
      bestIndex = i;
    }
  }

  return items[bestIndex];
}
