import { FindOneOptions } from 'typeorm';
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
