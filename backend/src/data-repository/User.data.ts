import { FindManyOptions } from 'typeorm';
import { User } from '../entity/User.entity';
import AppDataSource from '../orm.config';

const repository = AppDataSource.getRepository(User);
export async function upsertUser(user: User): Promise<User> {
  return await repository.save(user);
}

export async function getUserByEmail(email: string) {
  const options: FindManyOptions<User> = {
    where: {
      email: email,
    },
  };
  return await repository.findOne(options);
}

export async function getUserByUsername(username: string) {
  const options: FindManyOptions<User> = {
    where: {
      username: username,
    },
  };
  return await repository.findOne(options);
}
