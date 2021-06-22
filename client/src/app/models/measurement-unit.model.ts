
export enum Unit {MM, CM, IN}

export const ALL_UNITS = [...Array(Object.keys(Unit).length / 2).keys()].map(i => Unit[i].toLowerCase());
