import express from 'express';
import { SearchUser } from './presentation/SearchUser.presentation';
import { GetFriendRequest } from './presentation/GetFriendRequests.presentation';

const router = express.Router();

router.get('/users', SearchUser);
router.get('/friends/request', GetFriendRequest);

export default router;
