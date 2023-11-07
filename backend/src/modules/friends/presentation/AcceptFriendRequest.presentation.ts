import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { authChecker } from '../../../framework/Auth.Checker';
import { FriendRequestInterface } from '../Friends.interface';
import { acceptFriendRequestLogic } from '../logic/AcceptFriendRequest.logic';

export async function acceptFriendRequest(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new FriendRequestInterface();

    data.user = auth.user;
    data.id = req.params.id;

    await acceptFriendRequestLogic(data);

    res.status(201);
    const result: ResponseBody<null> = {
      message: 'Accept Friend Request Successful!',
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
