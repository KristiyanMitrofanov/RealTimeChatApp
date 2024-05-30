let baseUrl = 'http://localhost:8080/api';

import { stompClient } from "../services/websocketService.js";

export const getMessages = () => {
    return fetch(`${baseUrl}/messages`, {
        method: 'GET',
        headers: {
            'content-type' : 'application/json',
            'Authorization' : `Bearer ${localStorage.getItem('token')}`
        }
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(data => {
                    throw new Error(data || 'Fetching failed');
                });
            } else {
                return res.json();
            }
        })
}

export const sendMessage = (chatId, message) => {
    const body = JSON.stringify({content: message});
    stompClient.send(`/app/chats/${chatId}/messages`, {}, body);
}