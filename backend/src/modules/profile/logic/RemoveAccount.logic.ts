import { deleteUser } from '../../../data-repository/User.data';
import { GetProfileById } from '../Profile.interface';

export async function removeAccountLogic(data: GetProfileById) {
  await deleteUser(data.user.id);
}
