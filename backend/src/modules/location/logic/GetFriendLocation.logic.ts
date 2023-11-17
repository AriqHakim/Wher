import { getFriendsByUserLocation } from '../../../data-repository/UserFriend.data';
import { GetFriendLocationInterface } from '../Location.interface';

export async function GetFriendRequestLogic(data: GetFriendLocationInterface) {
  const userFriend = await getFriendsByUserLocation(
    data.user.id,
    data.limit,
    data.offset,
  );
  const users = {
    data: [],
    total_data: userFriend.total_data,
  };
  userFriend.data.forEach(function (value) {
    if (value.user1.id === data.user.id) {
      users.data.push(value.user2);
    }

    if (value.user2.id === data.user.id) {
      users.data.push(value.user1);
    }
  });
  return users;
}
