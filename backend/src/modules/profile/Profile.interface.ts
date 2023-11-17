import { User } from '../../entity/User.entity';

export class GetMyProfileInterface {
  user: User;
}

export class GetProfileById {
  user: User;
  id: string;
}

export class EditProfileInterface {
  user: User;
  name: string;
  password: string;
  confirmPassword: string;
  file: Express.Multer.File;
}
