import * as dotenv from 'dotenv';
dotenv.config();

import { DataSource } from 'typeorm';
import { User } from '../entity/User.entity';
import { Users1695714925182 } from '../migration/1695714925182-Users';
import { UserFriend } from '../entity/UserFriend.entity';
import { UserFriends1695715664194 } from '../migration/1695715664194-UserFriends';
import { FriendRequest } from '../entity/FriendRequest.entity';
import { FriendRequests1695715957297 } from '../migration/1695715957297-FriendRequests';
import { Location } from '../entity/Location.entity';
import { Location1695716362561 } from '../migration/1695716362561-Location';

const AppDataSource: DataSource = new DataSource({
  type: 'mysql',
  host: process.env.DB_HOST,
  port: parseInt(process.env.DB_PORT),
  username: process.env.DB_USERNAME,
  password: process.env.DB_PASSWORD,
  database:
    process.env.ENVIRONTMENT === 'production'
      ? process.env.DB_NAME
      : process.env.DB_TEST,
  entities: [User, UserFriend, FriendRequest, Location],
  migrations: [
    Users1695714925182,
    UserFriends1695715664194,
    FriendRequests1695715957297,
    Location1695716362561,
  ],
  migrationsTableName: 'migrations',
  synchronize: false,
  logging: false,
  migrationsRun: false,
});

export default AppDataSource;
