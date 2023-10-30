import { FindManyOptions, FindOneOptions } from 'typeorm';
import { FriendRequest } from '../entity/FriendRequest.entity';
import AppDataSource from '../config/orm.config';

const repository = AppDataSource.getRepository(FriendRequest);
export async function checkFriendRequest(
  targetId: string,
  requesterId: string,
) {
  const options: FindOneOptions<FriendRequest> = {
    where: {
      target: {
        id: targetId,
      },
      requester: {
        id: requesterId,
      },
    },
    relations: ['target', 'requester'],
  };
  return await repository.findOne(options);
}

export async function searchFriendRequester(targetId: string) {
  const options: FindManyOptions<FriendRequest> = {
    select: {
      id: true,
      status: true,
      requester: {
        id: true,
        email: false,
        userId: true,
        username: true,
        name: true,
        photoURL: true,
        password: false,
      },
      target: {
        id: true,
      },
    },
    where: {
      target: {
        id: targetId,
      },
    },
    relations: ['target', 'requester'],
  };
  return await repository.find(options);
}
