import { Request, Response } from 'express';
import { authChecker } from '../../../framework/Auth.Checker';
import { GetProfileById } from '../Profile.interface';
import { ResponseBody } from '../../../framework/ResponseBody';
import { removeAccountLogic } from '../logic/RemoveAccount.logic';

export async function removeAccount(req: Request, res: Response) {
  try {
    const data = new GetProfileById();

    const auth = await authChecker(req.headers['authorization'] as string);

    data.user = auth.user;
    data.id = req.params.id;

    await removeAccountLogic(data);

    const result: ResponseBody<null> = {
      message: 'Account Removal Success!',
    };

    res.status(201);
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
