import express from 'express';
import { SearchUser } from './presentation/SearchUser.presentation';

const router = express.Router();

router.get('/users', SearchUser)

export default router;
