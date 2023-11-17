import express from 'express';
import { upload } from '../../framework/MulterToFirebase';
import { getMyProfile } from './presentation/GetMyProfile.presentation';
import { getProfileById } from './presentation/GetProfileById.presentation';
import { removeAccount } from './presentation/RemoveAccount.presentation';
import { editProfile } from './presentation/EditProfile.presentation';

const router = express.Router();

router.get('/profile', getMyProfile);
router.put('/profile', upload.single('file'), editProfile);
router.get('/profile/:id', getProfileById);
router.delete('/profile/remove', removeAccount);

export default router;
