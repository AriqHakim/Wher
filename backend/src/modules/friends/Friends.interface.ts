import { User } from 'src/entity/User.entity';

export class SearchUserInterface {
  user: User;
  q: string;
  limit: number;
  offset: number;
}

export class GetFriendRequestInterface {
  user: User;
  limit: number;
  offset: number;
}

export class RemoveFriendInterface {
  user: User;
  id: string;
}
