import dotenv from 'dotenv';
dotenv.config();

import bodyParser from 'body-parser';
import AppDataSource from './config/orm.config';
import ExpressServer, { Express, Request, Response } from 'express';
import cors from 'cors';

import authRouter from './modules/auth/Auth.route';
import profileRouter from './modules/profile/Profile.route';

const PORT: number = parseInt(process.env.PORT || '3000');

async function main() {
  const server: Express = ExpressServer();
  await AppDataSource.initialize()
    .then(() => {
      console.log('Data Source has been initialized!');
    })
    .catch((err) => {
      console.error('Error during Data Source initialization', err);
    });

  server.use(cors());
  server.use(bodyParser.json());

  server.listen(PORT, () => console.log(`Server running on port ${PORT}`));

  server.get('/', (req: Request, res: Response) => {
    res.send('Rest API wher-backend model created!');
  });

  server.use('/', authRouter);
  server.use('/', profileRouter);
}

main();
