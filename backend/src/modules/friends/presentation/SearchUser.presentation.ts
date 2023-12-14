import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { User } from '../../../entity/User.entity';
import { authChecker } from '../../../framework/Auth.Checker';
import { SearchUserInterface } from '../Friends.interface';
import { parseQueryToInt } from '../../../framework/Utils';
import { SearchUserLogic } from '../logic/SearchUser.logic';

export async function SearchUser(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new SearchUserInterface();

    data.user = auth.user;
    data.offset = parseQueryToInt(req.query, 'offset');
    data.limit = parseQueryToInt(req.query, 'limit');
    data.q = req.query['q'] ? (req.query['q'] as string) : undefined;

    const users = await SearchUserLogic(data);

    res.status(200);
    const result: ResponseBody<User[]> = {
      message: null,
      data: users,
    };
    res.send(result);
    return result;
  } catch (error) {
    const result: ResponseBody<null> = {
      message: error.message,
    };

    res.status(error.code);
    res.send(result);
    return result;
  }
}
