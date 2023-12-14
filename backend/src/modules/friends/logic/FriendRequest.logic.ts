import { getUserByID } from '../../../data-repository/User.data';
import {
  BadRequestError,
  NotFoundError,
} from '../../../framework/Error.interface';
import { FriendRequestInterface } from '../Friends.interface';
import {
  FriendRequest,
  REQUEST_STATUS,
} from '../../../entity/FriendRequest.entity';
import {
  checkFriendRequest,
  deleteFriendRequest,
  upsertFriendRequest,
} from '../../../data-repository/FriendRequest.data';
import { getFriendship } from '../../../data-repository/UserFriend.data';

export async function friendRequestLogic(data: FriendRequestInterface) {
  const target = await getUserByID(data.id);
  if (!target) {
    throw new NotFoundError('User not found!');
  }

  const friendRequest = await checkFriendRequest(target.id, data.user.id);

  if (data.cancel) {
    if (friendRequest) {
      return await deleteFriendRequest(friendRequest.id);
    } else {
      throw new BadRequestError('Not Requested Yet!');
    }
  }

  if (!data.cancel && friendRequest) {
    throw new BadRequestError('Already Requested!');
  }

  const friendship = await getFriendship(target.id, data.user.id);
  if (friendship.length !== 0) {
    throw new BadRequestError('Already Friend With This User!');
  }

  const request = new FriendRequest();
  request.requester = data.user;
  request.status = REQUEST_STATUS.PENDING;
  request.target = target;

  return await upsertFriendRequest(request);
}
