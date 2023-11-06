import { FindManyOptions } from 'typeorm';
import { UserFriend } from '../entity/UserFriend.entity';
import AppDataSource from '../config/orm.config';
import { countTotalData } from '../framework/Utils';

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

export async function getFriendsByUserLocation(
  id: string,
  limit: number,
  offset: number,
) {
  const options: FindManyOptions<UserFriend> = {
    select: {
      id: true,
      user1: {
        id: true,
        username: true,
        photoURL: true,
      },
      user2: {
        id: true,
        username: true,
        photoURL: true,
      },
    },
    where: [
      {
        user1: {
          id: id,
        },
      },
      {
        user2: {
          id: id,
        },
      },
    ],
    relations: ['user1', 'user2', 'user1.location', 'user2.location'],
    take: limit,
    skip: offset,
  };
  const data = await repository.find(options);
  const total_data = await countTotalData(UserFriend, options);
  return { data, total_data };
}

export async function getFriendsByUser(
  id: string,
  limit: number,
  offset: number,
) {
  const options: FindManyOptions<UserFriend> = {
    select: {
      id: true,
      user1: {
        id: true,
        userId: true,
        username: true,
        name: true,
        photoURL: true,
        email: false,
        password: false,
      },
      user2: {
        id: true,
        userId: true,
        username: true,
        name: true,
        photoURL: true,
        email: false,
        password: false,
      },
    },
    where: [
      {
        user1: {
          id: id,
        },
      },
      {
        user2: {
          id: id,
        },
      },
    ],
    relations: ['user1', 'user2'],
    take: limit,
    skip: offset,
  };
  const data = await repository.find(options);
  const total_data = await countTotalData(UserFriend, options);
  return { data, total_data };
}
