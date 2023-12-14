import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { authChecker } from '../../../framework/Auth.Checker';
import { FriendRequestInterface } from '../Friends.interface';
import { friendRequestLogic } from '../logic/FriendRequest.logic';

export async function friendRequest(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new FriendRequestInterface();

    data.user = auth.user;
    data.id = req.params.id;
    data.cancel = req.body.cancel;

    await friendRequestLogic(data);

    res.status(201);
    const result: ResponseBody<null> = {
      message: 'Operation Successful!',
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
