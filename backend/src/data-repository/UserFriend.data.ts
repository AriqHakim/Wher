import { FindManyOptions } from 'typeorm';
import { UserFriend } from '../entity/UserFriend.entity';
import AppDataSource from '../config/orm.config';

const repository = AppDataSource.getRepository(UserFriend);
export async function getFriendship(id1: string, id2: string) {
  const options: FindManyOptions<UserFriend> = {
    where: [
      {
        user1: {
          id: id1,
        },
        user2: {
          id: id2,
        },
      },
      {
        user1: {
          id: id2,
        },
        user2: {
          id: id1,
        },
      },
    ],
    relations: ['user1', 'user2'],
  };
  return await repository.find(options);
}
