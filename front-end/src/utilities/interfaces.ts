export interface Chat {
    id: string;
    name: string;
    history: Message[];
    users: User[];
}

export interface Message {
    timestamp: string,
    creator: User,
    content: string;
}

export interface User {
    id: number;
    username: string;
    email: string;
}

export interface Notification {
    sender: string,
    message: string,
    chatId: number;
}