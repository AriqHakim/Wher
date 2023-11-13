import * as UserData from '../../src/data-repository/User.data';
import * as FriendRequestData from '../../src/data-repository/FriendRequest.data';
import {
  FriendRequest,
  REQUEST_STATUS,
} from '../../src/entity/FriendRequest.entity';
import { User } from '../../src/entity/User.entity';
import { TestHelper } from '../TestHelper';
import { describe, expect, test, beforeAll, afterAll } from '@jest/globals';

beforeAll(async () => {
  await TestHelper.instance.setupTestDB();
});

afterAll(async () => {
  await TestHelper.instance.teardownTestDB();
});

describe('Friend Request Data Repository Test', () => {
  const targetUser = new User();
  targetUser.email = 'target@gmail.com';
  targetUser.name = 'target';
  targetUser.password = 'pass';
  targetUser.userId = 'IDtarget';
  targetUser.username = 'usrTarget';
  targetUser.photoURL = 'example1.com';

  const requesterUser = new User();
  requesterUser.email = 'requester@gmail.com';
  requesterUser.name = 'requester';
  requesterUser.password = 'pass';
  requesterUser.userId = 'IDreq';
  requesterUser.username = 'usrRequester';
  requesterUser.photoURL = 'example2.com';

  const request = new FriendRequest();
  request.requester = requesterUser;
  request.target = targetUser;
  request.status = REQUEST_STATUS.PENDING;

  const resultTest = (result) => {
    expect(result.target.id).toEqual(targetUser.id);
    expect(result.requester.id).toEqual(requesterUser.id);
    expect(result.status).toEqual(REQUEST_STATUS.PENDING);
  };

  test('Insert New Friend Request', async () => {
    await UserData.upsertUser(targetUser);
    await UserData.upsertUser(requesterUser);
    const result = await FriendRequestData.upsertFriendRequest(request);
    resultTest(result);
  });

  test('Get Friend Request by ID', async () => {
    const result = await FriendRequestData.getFriendRequestByID(request.id);
    expect(result?.id).toEqual(request.id);
    resultTest(result);
  });

  test('Get Friend Request by Target and Requester', async () => {
    const result = await FriendRequestData.checkFriendRequest(
      targetUser.id,
      requesterUser.id,
    );
    resultTest(result);
  });

  test('Get Friend Request by Target', async () => {
    for (let i = 0; i < 20; i++) {
      const usr = new User();
      usr.email = `req${i}@gmail.com`;
      usr.name = `req${i}`;
      usr.password = 'pass';
      usr.userId = `req${i}`;
      usr.username = `req${i}`;
      usr.photoURL = `example${i}.com`;
      await UserData.upsertUser(usr);

      const req = new FriendRequest();
      req.requester = usr;
      req.target = targetUser;
      req.status = REQUEST_STATUS.PENDING;
      await FriendRequestData.upsertFriendRequest(req);
    }

    // test fetch all datas
    const allData = await FriendRequestData.searchFriendRequester(
      targetUser.id,
      20,
      0,
    );
    expect(allData.total_data).toEqual(21);
    expect(allData.data.length).toEqual(20);

    // test fetch some datas
    const someData = await FriendRequestData.searchFriendRequester(
      targetUser.id,
      10,
      0,
    );
    expect(someData.total_data).toEqual(21);
    expect(someData.data.length).toEqual(10);

    // test fetch with offset
    const offsetedData = await FriendRequestData.searchFriendRequester(
      targetUser.id,
      20,
      13,
    );
    expect(offsetedData.total_data).toEqual(21);
    expect(offsetedData.data.length).toEqual(8);
  });

  test('Update Friend Request', async () => {
    request.status = REQUEST_STATUS.ACCEPTED;
    const result = await FriendRequestData.upsertFriendRequest(request);
    expect(result.target.id).toEqual(targetUser.id);
    expect(result.requester.id).toEqual(requesterUser.id);
    expect(result.status).toEqual(REQUEST_STATUS.ACCEPTED);
  });

  test('Delete Friend Request', async () => {
    await FriendRequestData.deleteFriendRequest(request.id);
    const result = await FriendRequestData.getFriendRequestByID(request.id);
    expect(result).toBeNull();
  });
});
