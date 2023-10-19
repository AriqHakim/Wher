import express from 'express';
import { register } from './presentation/Register.presentation';
import { login } from './presentation/Login.presentation';

const router = express.Router();

router.post('/register', register);
router.post('/login', login);

export default router;
