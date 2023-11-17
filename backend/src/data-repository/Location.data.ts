import { FindOneOptions } from 'typeorm';
import { Location } from '../entity/Location.entity';
import AppDataSource from '../config/orm.config';

const repository = AppDataSource.getRepository(Location);

export async function searchLocationByUser(id: string) {
  const options: FindOneOptions<Location> = {
    where: {
      user: {
        id: id,
      },
    },
    relations: ['user'],
  };
  return await repository.findOne(options);
}

export async function upsertLocation(data: Location) {
  return await repository.save(data);
}
