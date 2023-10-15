import {
  Entity,
  PrimaryGeneratedColumn,
  ManyToOne,
  JoinColumn,
  Column,
} from 'typeorm';
import { User } from './User.entity';

export enum REQUEST_STATUS {
  PENDING = 'Pending',
  ACCEPTED = 'Accepted',
}

@Entity('friend_requests')
export class FriendRequest {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @ManyToOne(() => User, {
    cascade: true,
    onUpdate: 'CASCADE',
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'requester' })
  requester: User;

  @ManyToOne(() => User, {
    cascade: true,
    onUpdate: 'CASCADE',
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'target' })
  target: User;

  @Column({
    type: 'enum',
    enum: REQUEST_STATUS,
  })
  status: REQUEST_STATUS;
}
