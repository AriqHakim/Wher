import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { FriendRequest } from '../../../entity/FriendRequest.entity';
import { authChecker } from '../../../framework/Auth.Checker';
import { GetFriendRequestInterface } from '../Friends.interface';
import { parseQueryToInt } from '../../../framework/Utils';
import { GetFriendRequestLogic } from '../logic/GetFriendRequests.logic';

export async function GetFriendRequest(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new GetFriendRequestInterface();

    data.user = auth.user;
    data.offset = parseQueryToInt(req.query, 'offset');
    data.limit = parseQueryToInt(req.query, 'limit');

    const requests = await GetFriendRequestLogic(data);

    res.status(200);
    const result: ResponseBody<FriendRequest[]> = {
      data: requests,
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
