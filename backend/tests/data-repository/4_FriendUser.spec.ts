import * as UserData from '../../src/data-repository/User.data';
import * as UserFriendData from '../../src/data-repository/UserFriend.data';
import { User } from '../../src/entity/User.entity';
import { UserFriend } from '../../src/entity/UserFriend.entity';
import { TestHelper } from '../TestHelper';
import { describe, expect, test, beforeAll, afterAll } from '@jest/globals';

beforeAll(async () => {
  await TestHelper.instance.setupTestDB();
});

afterAll(async () => {
  await TestHelper.instance.teardownTestDB();
});

describe('User Friend Data Repository Tests', () => {
  const user1 = new User();
  user1.email = 'test1@gmail.com';
  user1.name = 'test1';
  user1.password = 'pass';
  user1.userId = 'userID1';
  user1.username = 'usrname1';
  user1.photoURL = 'example1.com';

  const user2 = new User();
  user2.email = 'test2@gmail.com';
  user2.name = 'test2';
  user2.password = 'pass';
  user2.userId = 'userID2';
  user2.username = 'usrname2';
  user2.photoURL = 'example2.com';

  const user3 = new User();
  user3.email = 'test3@gmail.com';
  user3.name = 'test3';
  user3.password = 'pass';
  user3.userId = 'userID3';
  user3.username = 'usrname3';
  user3.photoURL = 'example3.com';

  const userFriend = new UserFriend();
  userFriend.user1 = user1;
  userFriend.user2 = user2;

  const userFriend3 = new UserFriend();
  userFriend3.user1 = user1;
  userFriend3.user2 = user3;

  test('Insert New Friendship', async () => {
    await UserData.upsertUser(user1);
    await UserData.upsertUser(user2);
    const result = await UserFriendData.upsertUserFriend(userFriend);
    expect(result.user1.id).toEqual(user1.id);
    expect(result.user2.id).toEqual(user2.id);
  });

  test('Get Friendship by user1 and user2', async () => {
    await UserData.upsertUser(user3);
    await UserFriendData.upsertUserFriend(userFriend3);
    const result1 = await UserFriendData.getFriendship(user1.id, user3.id);
    const result2 = await UserFriendData.getFriendship(user2.id, user3.id);
    expect(result1.length).toEqual(1);
    expect(result2.length).toEqual(0);
  });

  test('Get Friendship by user', async () => {
    const result1 = await UserFriendData.getFriendsByUser(user1.id, 10, 0);
    const result2 = await UserFriendData.getFriendsByUser(user2.id, 10, 0);
    expect(result1.total_data).toEqual(2);
    expect(result1.data.length).toEqual(2);
    expect(result2.total_data).toEqual(1);
    expect(result2.data.length).toEqual(1);
  });

  test('Get Friendship with location by user', async () => {
    const result1 = await UserFriendData.getFriendsByUserLocation(
      user1.id,
      10,
      0,
    );
    const result2 = await UserFriendData.getFriendsByUserLocation(
      user2.id,
      10,
      0,
    );
    expect(result1.total_data).toEqual(2);
    expect(result1.data.length).toEqual(2);
    expect(result2.total_data).toEqual(1);
    expect(result2.data.length).toEqual(1);
  });

  test('Delete Friendship', async () => {
    const result = await UserFriendData.deleteFriendship(userFriend.id);
    expect(result.affected).toEqual(1);
  });
});
