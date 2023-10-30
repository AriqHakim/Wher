import { searchFriendRequester } from '../../../data-repository/FriendRequest.data';
import { GetFriendRequestInterface } from '../Friends.interface';

export async function GetFriendRequestLogic(data: GetFriendRequestInterface) {
  return await searchFriendRequester(data.user.id, data.limit, data.offset);
}
