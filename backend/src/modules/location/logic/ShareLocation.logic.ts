import {
  searchLocationByUser,
  upsertLocation,
} from '../../../data-repository/Location.data';
import { ShareLocationInterface } from '../Location.interface';
import { Location } from '../../../entity/Location.entity';

export async function GetFriendRequestLogic(data: ShareLocationInterface) {
  let location = await searchLocationByUser(data.user.id);
  if (location) {
    location.lat = data.lat;
    location.lon = data.lon;
  } else {
    location = new Location();
    location.user = data.user;
    location.lat = data.lat;
    location.lon = data.lon;
  }
  const today = Date.now();
  const currDay = new Date(today);
  location.date = currDay;

  return await upsertLocation(location);
}
