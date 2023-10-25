import express from 'express';
import { getMyProfile } from './presentation/GetMyProfile.presentation';
import { getProfileById } from './presentation/GetProfileById.presentation';
import { removeAccount } from './presentation/RemoveAccount.presentation';

const router = express.Router();

router.get('/profile', getMyProfile);
router.get('/profile/:id', getProfileById);
router.delete('/profile/remove', removeAccount);

export default router;
