import express from 'express';
import { SearchUser } from './presentation/SearchUser.presentation';
import { GetFriendRequest } from './presentation/GetFriendRequests.presentation';
import { GetFriendsLists } from './presentation/GetFriendsList.presentation';
import { removeFriend } from './presentation/RemoveFriends.presentation';
import { friendRequest } from './presentation/FriendRequest.presentation';
import { acceptFriendRequest } from './presentation/AcceptFriendRequest.presentation';
import { rejectFriendRequest } from './presentation/RejectFriendRequest,presentation';

const router = express.Router();

router.get('/users', SearchUser);
router.get('/friends', GetFriendsLists);
router.get('/friends/request', GetFriendRequest);
router.post('/friends/request/:id', friendRequest);
router.post('/friends/request/:id/accept', acceptFriendRequest);
router.post('/friends/request/:id/reject', rejectFriendRequest);
router.delete('/friends/:id/remove', removeFriend);

export default router;
