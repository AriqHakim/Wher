import express from 'express';
import { GetFriendLocation } from './presentation/GetFriendLocation.presentation';
import { shareLocation } from './presentation/ShareLocation.presentation';

const router = express.Router();

router.get('/locations', GetFriendLocation);
router.put('/locations', shareLocation);

export default router;
