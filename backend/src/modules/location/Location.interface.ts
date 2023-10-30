import { User } from '../../entity/User.entity';

export class GetFriendLocationInterface {
  user: User;
  limit: number;
  offset: number;
}
