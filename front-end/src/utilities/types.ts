import { Context } from "page";
import { Client } from "@stomp/stompjs";

export type Information = {
    token: string,
    username: string,
    role: string
}

export type Register = {
    username: string,
    email: string,
    password: string,
    confirmPassword: string;
}

export type Login = {
    username: string,
    password: string;
}

export type Handler = (e: Event, ctx?: Context, client?: Client, id?: string) => Promise<void>;

