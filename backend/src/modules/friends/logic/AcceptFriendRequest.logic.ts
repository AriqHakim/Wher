import { BadRequestError } from '../../../framework/Error.interface';
import {
  deleteFriendRequest,
  getFriendRequestByID,
} from '../../../data-repository/FriendRequest.data';
import { FriendRequestInterface } from '../Friends.interface';
import { UserFriend } from '../../../entity/UserFriend.entity';
import { upsertUserFriend } from '../../../data-repository/UserFriend.data';

export async function acceptFriendRequestLogic(data: FriendRequestInterface) {
  const request = await getFriendRequestByID(data.id);
  if (!request) {
    throw new BadRequestError('Request Not Found!');
  }

  const userFriend = new UserFriend();
  userFriend.user1 = data.user;
  userFriend.user2 = request.requester;

  await deleteFriendRequest(request.id);

  return await upsertUserFriend(userFriend);
}
