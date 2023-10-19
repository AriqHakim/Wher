import { User } from '../../../entity/User.entity';
import {
  getUserByEmail,
  getUserByUsername,
  upsertUser,
} from '../../../data-repository/User.data';
import { BadRequestError } from '../../../framework/Error.interface';
import { RegisterInterface } from '../Auth.interface';
import bcrypt from 'bcrypt';
import { randomString } from '../../../framework/Utils';

export async function registerUser(data: RegisterInterface) {
  if (data.password === '') {
    throw new BadRequestError("Password can't be empty!");
  }

  if (data.password != data.confirmPassword) {
    throw new BadRequestError("Password doesn't match!");
  }

  const userEmail = await getUserByEmail(data.email);
  if (userEmail) {
    throw new BadRequestError('Email already used!');
  }

  const userUsername = await getUserByUsername(data.username);
  if (userUsername) {
    throw new BadRequestError('Username already used!');
  }

  const saltRound = 10;
  const encryptedPass = await bcrypt.hash(data.password, saltRound);

  const user: User = new User();

  user.email = data.email;
  user.password = encryptedPass;
  user.name = data.name;
  user.username = data.username;
  user.userId = randomString(8);
  user.photoURL = '';

  return (await upsertUser(user)) instanceof User;
}
