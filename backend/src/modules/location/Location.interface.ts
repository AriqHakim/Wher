import { Double } from 'typeorm';
import { User } from '../../entity/User.entity';

export class GetFriendLocationInterface {
  user: User;
  limit: number;
  offset: number;
}

export class ShareLocationInterface {
  user: User;
  lat: Double;
  lon: Double;
}
