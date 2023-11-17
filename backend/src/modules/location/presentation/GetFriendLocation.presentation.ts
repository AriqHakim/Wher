import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { User } from '../../../entity/User.entity';
import { authChecker } from '../../../framework/Auth.Checker';
import { GetFriendLocationInterface } from '../Location.interface';
import { parseQueryToInt } from '../../../framework/Utils';
import { GetFriendRequestLogic } from '../logic/GetFriendLocation.logic';
import { PaginationResult } from '../../../framework/pagination.interface';

export async function GetFriendLocation(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new GetFriendLocationInterface();

    data.user = auth.user;
    data.offset = parseQueryToInt(req.query, 'offset');
    data.limit = parseQueryToInt(req.query, 'limit');

    const users = await GetFriendRequestLogic(data);

    res.status(200);
    const result: PaginationResult<User> = {
      data: users.data,
      total_data: users.total_data,
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
