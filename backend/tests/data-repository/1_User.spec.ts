import * as UserData from '../../src/data-repository/User.data';
import { User } from '../../src/entity/User.entity';
import { TestHelper } from '../TestHelper';
import { describe, expect, test, beforeAll, afterAll } from '@jest/globals';

beforeAll(async () => {
  await TestHelper.instance.setupTestDB();
});

afterAll(async () => {
  await TestHelper.instance.teardownTestDB();
});

describe('User Data Repository Tests', () => {
  const user = new User();
  user.email = 'test@gmail.com';
  user.name = 'test';
  user.password = 'pass';
  user.userId = 'userID';
  user.username = 'username';
  user.photoURL = 'example.com';

  const resultTest = (result) => {
    expect(result.email).toBe('test@gmail.com');
    expect(result.name).toBe('test');
    expect(result.userId).toBe('userID');
    expect(result.username).toBe('username');
    expect(result.photoURL).toBe('example.com');
  };

  test('Insert New User', async () => {
    const result = await UserData.upsertUser(user);
    resultTest(result);
  });

  test('Get User by ID', async () => {
    const result = await UserData.getUserByID(user.id);
    expect(result).not.toBeNull();
    resultTest(result);
  });

  test('Get User by Email', async () => {
    const result = await UserData.getUserByEmail('test@gmail.com');
    expect(result).not.toBeNull();
    resultTest(result);
  });

  test('Get User by Username', async () => {
    const result = await UserData.getUserByUsername('username');
    expect(result).not.toBeNull();
    resultTest(result);
  });

  test('Update User', async () => {
    user.name = 'updated';
    const result = await UserData.upsertUser(user);
    expect(result.name).toBe('updated');
  });

  test('Get User by Email or Username', async () => {
    const result1 = await UserData.getUserByEmailOrUsername('username');
    const result2 = await UserData.getUserByEmailOrUsername('test@gmail.com');
    expect(result1).toEqual(result2);
  });

  test('Delete User', async () => {
    const result = await UserData.deleteUser(user.id);
    expect(result.affected).toEqual(1);
  });

  test('Get User by User ID or Username', async () => {
    for (let i = 0; i < 20; i++) {
      const usr = new User();
      usr.email = `test${i}@gmail.com`;
      usr.name = `test${i}`;
      usr.password = 'pass';
      usr.userId = `userID${i}`;
      usr.username = `username${i}`;
      usr.photoURL = `example${i}.com`;
      await UserData.upsertUser(usr);
    }

    // test fetch all data
    const allData = await UserData.getUserByUserIdOrUsername('usern', 20, 0);
    expect(allData.length).toEqual(20);

    // test fetch all data with limit
    const allDataLimted = await UserData.getUserByUserIdOrUsername(
      'usern',
      10,
      0,
    );
    expect(allDataLimted.length).toEqual(10);

    // test fetch all data with limit and offset
    const allDataSkipped = await UserData.getUserByUserIdOrUsername(
      'usern',
      10,
      12,
    );
    expect(allDataSkipped.length).toEqual(8);

    // test fetch one of user based on userId
    const dataUser3 = await UserData.getUserByUserIdOrUsername(
      'userID3',
      20,
      0,
    );
    expect(dataUser3.length).toEqual(1);
    expect(dataUser3[0].userId).toEqual('userID3');

    const dataUser9 = await UserData.getUserByUserIdOrUsername(
      'userID9',
      20,
      0,
    );
    expect(dataUser9.length).toEqual(1);
    expect(dataUser9[0].userId).toEqual('userID9');

    const dataUser13 = await UserData.getUserByUserIdOrUsername(
      'userID13',
      20,
      0,
    );
    expect(dataUser13.length).toEqual(1);
    expect(dataUser13[0].userId).toEqual('userID13');

    // test fetch some of users data based on username
    const usernames = await UserData.getUserByUserIdOrUsername(
      'username1',
      20,
      0,
    );
    expect(usernames.length).toEqual(11);

    // test fetch some of users data based on username
    const usernamesLimited = await UserData.getUserByUserIdOrUsername(
      'username1',
      10,
      0,
    );
    expect(usernamesLimited.length).toEqual(10);
  });
});
