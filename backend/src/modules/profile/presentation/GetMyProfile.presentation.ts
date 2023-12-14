import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { GetMyProfileInterface } from '../Profile.interface';
import { authChecker } from '../../../framework/Auth.Checker';

export async function getMyProfile(req: Request, res: Response) {
  try {
    const data = new GetMyProfileInterface();

    const auth = await authChecker(req.headers['authorization'] as string);

    data.user = auth.user;
    delete data.user.id;
    delete data.user.password;

    res.status(200);
    res.send(data.user);
    return data.user;
  } catch (error) {
    const result: ResponseBody<null> = {
      message: error.message,
    };

    res.status(error.code);
    res.send(result);
    return result;
  }
}
