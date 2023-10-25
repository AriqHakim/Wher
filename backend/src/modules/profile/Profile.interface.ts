import { User } from '../../entity/User.entity';

export class GetMyProfileInterface {
  user: User;
}

export class GetProfileById {
  user: User;
  id: string;
}
