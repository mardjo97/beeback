import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 6728,
  login: 'z`my@e\\WVA6I98\\-dR',
};

export const sampleWithPartialData: IUser = {
  id: 8696,
  login: 'o',
};

export const sampleWithFullData: IUser = {
  id: 8381,
  login: 'JuT',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
