import {
  Entity,
  PrimaryGeneratedColumn,
  JoinColumn,
  Column,
  OneToOne,
  Double,
} from 'typeorm';
import { User } from './User.entity';

@Entity('locations')
export class Location {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @OneToOne(() => User, {
    cascade: true,
    onUpdate: 'CASCADE',
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'user' })
  user: User;

  @Column({
    type: 'datetime',
  })
  date: Date;

  @Column({
    type: 'double',
  })
  lat: Double;

  @Column({
    type: 'double',
  })
  lon: Double;
}
