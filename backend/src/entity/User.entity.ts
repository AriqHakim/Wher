import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  OneToMany,
  OneToOne,
} from 'typeorm';
import { UserFriend } from './UserFriend.entity';
import { FriendRequest } from './FriendRequest.entity';
import { Location } from './Location.entity';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({
    type: 'varchar',
    length: 50,
  })
  name: string;

  @Column({
    type: 'varchar',
    length: 50,
    unique: true,
  })
  username: string;

  @Column({
    name: 'user_id',
    type: 'varchar',
    length: 8,
    unique: true,
  })
  userId: string;

  @Column({
    type: 'varchar',
    length: 50,
    unique: true,
  })
  email: string;

  @Column({
    type: 'varchar',
    length: 255,
  })
  password: string;

  @Column({
    name: 'photo_profile_url',
    type: 'varchar',
    length: 255,
  })
  photoURL: string;

  @OneToMany(() => UserFriend, (u) => u.user1)
  user1?: UserFriend[];

  @OneToMany(() => UserFriend, (u) => u.user2)
  user2?: UserFriend[];

  @OneToMany(() => FriendRequest, (u) => u.requester)
  requester?: FriendRequest[];

  @OneToMany(() => FriendRequest, (u) => u.target)
  target?: FriendRequest[];

  @OneToOne(() => Location, (l) => l.user)
  location?: Location;
}
