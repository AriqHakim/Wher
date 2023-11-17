import { getUserByUserIdOrUsername } from '../../../data-repository/User.data';
import { SearchUserInterface } from '../Friends.interface';

export async function SearchUserLogic(data: SearchUserInterface) {
  return await getUserByUserIdOrUsername(data.q, data.limit, data.offset);
}
