import { getFriendsByUser } from '../../../data-repository/UserFriend.data';
import { GetFriendRequestInterface } from '../Friends.interface';

export async function GetFriendsList(data: GetFriendRequestInterface) {
  const userFriend = await getFriendsByUser(
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
