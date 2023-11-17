import { BadRequestError } from '../../../framework/Error.interface';
import {
  deleteFriendRequest,
  getFriendRequestByID,
} from '../../../data-repository/FriendRequest.data';
import { FriendRequestInterface } from '../Friends.interface';

export async function rejectFriendRequestLogic(data: FriendRequestInterface) {
  const request = await getFriendRequestByID(data.id);
  if (!request) {
    throw new BadRequestError('Request Not Found!');
  }

  return await deleteFriendRequest(request.id);
}
