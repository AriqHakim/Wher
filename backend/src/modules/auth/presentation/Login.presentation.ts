import { Request, Response } from 'express';
import { LoginInterface } from '../Auth.interface';
import { ResponseBody } from 'src/framework/ResponseBody';
import { loginUser } from '../logic/Login.logic';

export async function login(req: Request, res: Response) {
  try {
    const data: LoginInterface = {
      username: req.body.username,
      password: req.body.password,
    };

    const token = await loginUser(data);

    res.status(201);
    const result = {
      token: token,
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
