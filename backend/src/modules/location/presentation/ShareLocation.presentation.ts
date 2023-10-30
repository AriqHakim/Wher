import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { authChecker } from '../../../framework/Auth.Checker';
import { ShareLocationInterface } from '../Location.interface';
import { GetFriendRequestLogic } from '../logic/ShareLocation.logic';

export async function shareLocation(req: Request, res: Response) {
  try {
    const auth = await authChecker(req.headers['authorization'] as string);

    const data = new ShareLocationInterface();

    data.user = auth.user;
    data.lat = req.body.lat;
    data.lon = req.body.lon;

    await GetFriendRequestLogic(data);

    res.status(200);
    const result: ResponseBody<null> = {
      message: 'Update Location Successful!',
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
