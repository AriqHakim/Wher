import { FindOneOptions } from 'typeorm';
import { User } from '../entity/User.entity';
import AppDataSource from '../config/orm.config';

const repository = AppDataSource.getRepository(User);
export async function upsertUser(user: User): Promise<User> {
  return await repository.save(user);
}

export async function getUserByID(id: string) {
  const options: FindOneOptions<User> = {
    select: {
      id: true,
      email: true,
      name: true,
      password: false,
      photoURL: false,
    },
    where: {
      id: id,
    },
  };
  return await repository.findOne(options);
}

export async function getUserByEmail(email: string) {
  const options: FindOneOptions<User> = {
    where: {
      email: email,
    },
  };
  return await repository.findOne(options);
}

export async function getUserByUsername(username: string) {
  const options: FindOneOptions<User> = {
    where: {
      username: username,
    },
  };
  return await repository.findOne(options);
}

export async function getUserByEmailOrUsername(data: string) {
  const options: FindOneOptions<User> = {
    where: [
      {
        username: data,
      },
      {
        email: data,
      },
    ],
  };
  return await repository.findOne(options);
}
