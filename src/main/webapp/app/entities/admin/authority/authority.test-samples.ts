import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'cd4de3b5-9121-4fc8-a133-bf3c0c9420c1',
};

export const sampleWithPartialData: IAuthority = {
  name: '5fced5e3-ec18-40fd-9bf1-59ee33c9db8f',
};

export const sampleWithFullData: IAuthority = {
  name: '868fd4d5-f4f3-44b4-a2e4-3340fb14af57',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
