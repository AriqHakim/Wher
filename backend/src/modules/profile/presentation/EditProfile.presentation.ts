import { Request, Response } from 'express';
import { ResponseBody } from '../../../framework/ResponseBody';
import { EditProfileInterface } from '../Profile.interface';
import { authChecker } from '../../../framework/Auth.Checker';
import { editProfileLogic } from '../logic/EditProfile.logic';

export async function editProfile(req: Request, res: Response) {
  try {
    const data = new EditProfileInterface();

    const auth = await authChecker(req.headers['authorization'] as string);

    data.user = auth.user;
    data.name = req.body.name;
    data.password = req.body.password;
    data.confirmPassword = req.body.confirmPassword;
    data.file = req.file;

    const updatedUser = await editProfileLogic(data);

    res.status(201);
    const result: ResponseBody<null> = {
      message: 'Edit Profile Successful!',
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
