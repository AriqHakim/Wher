import { NotFoundError } from '../../../framework/Error.interface';
import { checkFriendRequest } from '../../../data-repository/FriendRequest.data';
import { getUserByID } from '../../../data-repository/User.data';
import { getFriendship } from '../../../data-repository/UserFriend.data';
import { GetProfileById } from '../Profile.interface';

export async function getProfileByIdLogic(data: GetProfileById) {
  const targetUser = await getUserByID(data.id);
  if (!targetUser) {
    throw new NotFoundError('User not found!');
  }
  const result = {
    photoURL: targetUser.photoURL,
    userId: targetUser.userId,
    username: targetUser.username,
    name: targetUser.name,
    email: targetUser.email,
    isFriend: null,
    requester: null,
    requestStatus: null,
  };

  result.isFriend =
    (await getFriendship(targetUser.id, data.user.id)).length !== 0
      ? true
      : false;

  const FriendRequest = await checkFriendRequest(targetUser.id, data.user.id);
  if (FriendRequest) {
    result.requester = true;
    result.requestStatus = FriendRequest.status;
  } else {
    result.requester = false;
    result.requestStatus = 'none';
  }

  return result;
}
