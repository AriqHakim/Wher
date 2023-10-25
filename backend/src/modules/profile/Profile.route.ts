import express from 'express';
import { getMyProfile } from './presentation/GetMyProfile.presentation';
import { getProfileById } from './presentation/GetProfileById.presentation';

const router = express.Router();

router.get('/profile', getMyProfile);
router.get('/profile/:id', getProfileById);

export default router;
