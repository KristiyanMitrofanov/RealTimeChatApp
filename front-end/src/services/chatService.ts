
let baseUrl = 'http://localhost:8080/api';
import { Observable, Subscription } from 'rxjs';
import { renderNotification } from "../utilities/renderers";
import { Chat, Notification } from "../utilities/interfaces";
import { Context } from "page";
import { fetchWithHandling } from "../utilities/typeGuards";

export const getChats = (): Promise<Chat[]> => {
    let fetchUrl;
    if (localStorage.getItem("role") == "USER") {
        fetchUrl = `${baseUrl}/chats?username=${localStorage.getItem("username")}`;
    } else {
        fetchUrl = `${baseUrl}/chats`;
    }
    return fetchWithHandling<Chat[]>(`${fetchUrl}`, {
        method: 'GET',
        headers: {
            'content-type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
}

export const getChat = (chatId: string): Promise<Chat> => {
    return fetchWithHandling<Chat>(`${baseUrl}/chats/${chatId}`, {
        method: 'GET',
        headers: {
            'content-type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
}

export const deleteChat = (chatId: string): Promise<void> => {
    return fetchWithHandling<void>(`${baseUrl}/chats/${chatId}`, {
        method: 'DELETE',
        headers: {
            'content-type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
}

export const createChat = (name: string): Promise<void> => {
    return fetchWithHandling<void>(`${baseUrl}/chats`, {
        method: 'POST',
        headers: {
            'content-type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(name)
    })
}

export const addUserToChat = (chatId: string, username: string): Promise<void> => {
    return fetchWithHandling<void>(`${baseUrl}/chats/${chatId}/users?username=${username}`, {
        method: 'POST',
        headers: {
            'content-type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
}

export const deleteUserFromChat = (chatId: string, username: string): Promise<void> => {
    return fetchWithHandling<void>(`${baseUrl}/chats/${chatId}/users?username=${username}`, {
        method: 'DELETE',
        headers: {
            'content-type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
}

const chatSubscriptions = new Map<string, Subscription>();

function subscribeToChatNotifications(chatId: string): Observable<Notification> {
    return new Observable<Notification>(observer => {
        const eventSource = new EventSource(`http://localhost:8080/notifications/${chatId}`);

        eventSource.onmessage = event => {
            try {
                const data = JSON.parse(event.data);

                // Transform data to match your Notification interface
                const notification: Notification = {
                    sender: data.sender,
                    message: data.message,
                    chatId: data.chatId
                };

                observer.next(notification);
            } catch (e) {
                observer.error(new Error('Failed to parse notification data'));
            }
        };

        eventSource.onerror = err => {
            observer.error(err);
            eventSource.close();
        };

        return () => {
            eventSource.close();
        };
    });
}


export function unsubscribeFromChats(): void {
    for (const [chatId, subscription] of chatSubscriptions) {
        subscription.unsubscribe();
        chatSubscriptions.delete(chatId);
    }
}


export function subscribeToChats(chats: Chat[], ctx: Context): void {
    for (const chat of chats) {
        if (!chatSubscriptions.has(chat.id)) {
            const subscription = subscribeToChatNotifications(chat.id).subscribe(
                notification => {
                    console.log('Received notification:', notification);
                    renderNotification(notification, ctx);
                },
                error => {
                    console.error('Error in chat notifications:', error);
                },
                () => {
                    console.log('Chat notifications stream completed');
                }
            );

            chatSubscriptions.set(chat.id, subscription);
        }
    }
}