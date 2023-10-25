import { Request, Response } from 'express';
import { ResponseBody } from 'src/framework/ResponseBody';
import { GetProfileById } from '../Profile.interface';
import { authChecker } from '../../../framework/Auth.Checker';
import { getProfileByIdLogic } from '../logic/GetProfileById.logic';

export async function getProfileById(req: Request, res: Response) {
  try {
    const data = new GetProfileById();

    const auth = await authChecker(req.headers['authorization'] as string);

    data.user = auth.user;
    data.id = req.params.id;

    const result = await getProfileByIdLogic(data);

    res.status(200);
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
