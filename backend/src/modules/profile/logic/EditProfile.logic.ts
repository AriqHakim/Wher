import { uploadToFirebase } from '../../../framework/MulterToFirebase';
import { BadRequestError } from '../../../framework/Error.interface';
import { EditProfileInterface } from '../Profile.interface';
import bcrypt from 'bcrypt';
import { upsertUser } from '../../../data-repository/User.data';

export async function editProfileLogic(data: EditProfileInterface) {
  if (data.password !== data.confirmPassword) {
    throw new BadRequestError("Password doesn't match!");
  }
  if (data.password === '') {
    if (data.confirmPassword !== '') {
      throw new BadRequestError("Password doesn't match!");
    }
  } else {
    if (data.password !== data.confirmPassword) {
      throw new BadRequestError("Password doesn't match!");
    } else {
      const saltRound = 10;
      const encryptedPass = await bcrypt.hash(data.password, saltRound);
      data.user.password = encryptedPass;
    }
  }
  const url = await uploadToFirebase(data.file);
  data.user.photoURL = url;
  data.user.name = data.name;
  return await upsertUser(data.user);
}
