let baseUrl = 'http://localhost:8080/api';

import { Message } from "../utilities/interfaces";
import { fetchWithHandling, isNotNull } from "../utilities/typeGuards";
import { stompClient } from "./websocketService";

export const getMessages = (): Promise<Message[]> => {
    return fetchWithHandling<Message[]>(`${baseUrl}/messages`, {
        method: 'GET',
        headers: {
            'content-type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
}

export const sendMessage = (chatId: string, message: string): void => {
    const body = JSON.stringify({ content: message });
    if (isNotNull(stompClient)) {
        stompClient.publish({ destination: `/app/chats/${chatId}/messages`, body });
    }
}