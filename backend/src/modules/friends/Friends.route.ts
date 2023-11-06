import express from 'express';
import { SearchUser } from './presentation/SearchUser.presentation';
import { GetFriendRequest } from './presentation/GetFriendRequests.presentation';
import { GetFriendsLists } from './presentation/GetFriendsList.presentation';
import { removeFriend } from './presentation/RemoveFriends.presentation';
import { friendRequest } from './presentation/FriendRequest.presentation';

const router = express.Router();

router.get('/users', SearchUser);
router.get('/friends', GetFriendsLists);
router.get('/friends/request', GetFriendRequest);
router.post('/friends/request/:id', friendRequest);
router.delete('/friends/:id/remove', removeFriend);

export default router;
