import { Request, Response } from 'express';
import { RegisterInterface } from '../Auth.interface';
import { ResponseBody } from 'src/framework/ResponseBody';
import { registerUser } from '../logic/Register.logic';

export async function register(req: Request, res: Response) {
  try {
    const data: RegisterInterface = {
      email: req.body.email,
      username: req.body.username,
      name: req.body.name,
      password: req.body.password,
      confirmPassword: req.body.confirmPassword,
    };

    await registerUser(data);

    res.status(201);
    const result: ResponseBody<null> = {
      message: 'Registration Success!',
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
