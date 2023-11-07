import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { User } from '../../../entity/User.entity';
import { authChecker } from '../../../framework/Auth.Checker';
import { GetFriendRequestInterface } from '../Friends.interface';
import { parseQueryToInt } from '../../../framework/Utils';
import { PaginationResult } from '../../../framework/pagination.interface';
import { GetFriendsList } from '../logic/GetFriendLists.logic';

export async function GetFriendsLists(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new GetFriendRequestInterface();

    data.user = auth.user;
    data.offset = parseQueryToInt(req.query, 'offset');
    data.limit = parseQueryToInt(req.query, 'limit');

    const users = await GetFriendsList(data);

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
