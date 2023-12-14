// @Entity
import { User } from '../entity/User.entity';

// @Interface
import { BadRequestError } from '../framework/Error.interface';

// @Data
import { getUserByID } from '../data-repository/User.data';

// @Util
import { JWTConfig } from '../framework/JWTConfig.interface';
import jwt from 'jsonwebtoken';

const JWT_SECRET_KEY: string = process.env.JWT_SECRET_KEY ?? 'sample-key';

export interface JWTPayload {
  id: string;
}

export function signJWT(payload: JWTPayload): string {
  return jwt.sign(payload, JWT_SECRET_KEY);
}

export const jwt_config: JWTConfig = {
  secret_key: JWT_SECRET_KEY,
  async jwtDataAction(payload: JWTPayload) {
    try {
      const user: User = await getUserByID(payload.id);
      return {
        user,
      };
    } catch (err) {
      throw new BadRequestError(`Your user seems to be invalid or not found`);
    }
  },
};
