import { FindManyOptions, FindOneOptions } from 'typeorm';
import { FriendRequest } from '../entity/FriendRequest.entity';
import AppDataSource from '../config/orm.config';
import { countTotalData } from '../framework/Utils';

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

export async function searchFriendRequester(
  targetId: string,
  limit: number,
  offset: number,
) {
  const options: FindManyOptions<FriendRequest> = {
    select: {
      id: true,
      status: true,
      requester: {
        id: true,
        email: true,
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
    take: limit,
    skip: offset,
  };
  const data = await repository.find(options);
  const total_data = await countTotalData(FriendRequest, options);

  return {
    data,
    total_data,
  };
}

export async function upsertFriendRequest(data: FriendRequest) {
  return await repository.save(data);
}

export async function getFriendRequestByID(id: string) {
  return await repository.findOne({
    where: {
      id: id,
    },
    relations: ['requester', 'target'],
  });
}

export async function deleteFriendRequest(id: string) {
  await repository.delete(id);
}
