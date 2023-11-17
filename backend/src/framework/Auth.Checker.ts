import { User } from '../entity/User.entity';
import { jwt_config } from '../config/jwt.config';
import { UnauthorizedError } from './Error.interface';
import { extractDataFromJWT } from './JWTExtactor';

export async function authChecker(header: string) {
  if (!header) {
    throw new UnauthorizedError(`Your request is unauthorized`);
  }

  const auth = (await extractDataFromJWT(jwt_config, header)) as {
    user: User;
  };

  if (!auth.user) {
    throw new UnauthorizedError(`Your request is unauthorized`);
  }

  return auth;
}
