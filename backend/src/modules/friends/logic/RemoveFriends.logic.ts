import {
  BadRequestError,
  NotFoundError,
} from '../../../framework/Error.interface';
import { getUserByID } from '../../../data-repository/User.data';
import { FriendRequestInterface } from '../Friends.interface';
import {
  deleteFriendship,
  getFriendship,
} from '../../../data-repository/UserFriend.data';

export async function removeFriendLogic(data: FriendRequestInterface) {
  const target = await getUserByID(data.id);
  if (!target) {
    throw new NotFoundError('User is not found!');
  }

  const friendship = await getFriendship(data.user.id, data.id);
  if (friendship.length === 0) {
    throw new BadRequestError('User not friend!');
  }

  return await deleteFriendship(friendship[0].id);
}
