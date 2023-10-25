import express from 'express';
import { getMyProfile } from './presentation/GetMyProfile.presentation';

const router = express.Router();

router.get('/profile', getMyProfile);

export default router;
