import {User} from "./User.ts";

export interface Income {
    id: number;
    amount: number,
    payment_date: Date
    user: User
}
