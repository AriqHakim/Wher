import { Double } from 'typeorm';
import * as LocationData from '../../src/data-repository/Location.data';
import { Location } from '../../src/entity/Location.entity';
import { User } from '../../src/entity/User.entity';
import { TestHelper } from '../TestHelper';
import { describe, expect, test, beforeAll, afterAll } from '@jest/globals';

beforeAll(async () => {
  await TestHelper.instance.setupTestDB();
});

afterAll(async () => {
  await TestHelper.instance.teardownTestDB();
});

describe('Location Data Repository Tests', () => {
  const user = new User();
  user.email = 'testLoc@gmail.com';
  user.name = 'testLoc';
  user.password = 'pass';
  user.userId = 'userLOC';
  user.username = 'usernameLoc';
  user.photoURL = 'example.com';

  const today = Date.now();
  const currDay = new Date(today);

  const location = new Location();
  location.lat = 0.0 as unknown as Double;
  location.lon = 0.0 as unknown as Double;
  location.user = user;
  location.date = currDay;

  test('Insert New Location', async () => {
    const result = await LocationData.upsertLocation(location);
    expect(result.id).toEqual(location.id);
  });

  test('Search Locatoin By User', async () => {
    const result = await LocationData.searchLocationByUser(user.id);
    expect(result?.lon).toEqual(location.lon);
    expect(result?.lat).toEqual(location.lat);
  });
});
