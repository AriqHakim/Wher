import { User } from "src/entity/User.entity";

export class SearchUserInterface {
    user: User
    q: string
    limit: number
    offset: number
}