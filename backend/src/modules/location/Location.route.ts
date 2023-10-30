import express from 'express';
import { GetFriendLocation } from './presentation/GetFriendLocation.presentation';

const router = express.Router();

router.get('/locations', GetFriendLocation);

export default router;
