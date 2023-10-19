import { User } from '../../../entity/User.entity';
import { LoginInterface } from '../Auth.interface';
import { getUserByEmailOrUsername } from '../../../data-repository/User.data';
import {
  BadRequestError,
  NotFoundError,
} from '../../../framework/Error.interface';
import bcrypt from 'bcrypt';
import { signJWT } from '../../../config/jwt.config';

export async function loginUser(data: LoginInterface) {
  const user: User = await getUserByEmailOrUsername(data.username);

  if (!user) {
    throw new NotFoundError(`Email or username ${data.username} is not found!`);
  }

  if (!bcrypt.compareSync(data.password, user.password)) {
    throw new BadRequestError("Password doesn't match!");
  }

  return signJWT({
    id: user.id,
  });
}
