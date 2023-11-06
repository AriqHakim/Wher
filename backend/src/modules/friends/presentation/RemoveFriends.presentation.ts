import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { authChecker } from '../../../framework/Auth.Checker';
import { RemoveFriendInterface } from '../Friends.interface';
import { removeFriendLogic } from '../logic/RemoveFriends.logic';

export async function removeFriend(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new RemoveFriendInterface();

    data.user = auth.user;
    data.id = req.params.id;

    await removeFriendLogic(data);

    res.status(200);
    const result: ResponseBody<null> = {
      message: 'Remove Friend Successful!',
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
