
let baseUrl = 'http://localhost:8080/api';
import { renderNotification } from "../utilities/renderers.js";

export const getChats = () => {
    let fetchUrl;
    if (localStorage.getItem("role") == "USER") {
        fetchUrl = `${baseUrl}/chats?username=${localStorage.getItem("username")}`;
    } else {
        fetchUrl = `${baseUrl}/chats`;
    }
    return fetch(`${fetchUrl}`, {
        method : 'GET',
        headers : {
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

export const getChat = (chatId) => {
    return fetch(`${baseUrl}/chats/${chatId}`, {
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

export const deleteChat = (chatId) => {
    return fetch(`${baseUrl}/chats/${chatId}`, {
        method: 'DELETE',
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
            alert("Chat deleted successfully!");
        }
    })
}

export const createChat = (name, ctx) => {
    return fetch(`${baseUrl}/chats`, {
        method: 'POST',
        headers: {
            'content-type' : 'application/json',
            'Authorization' : `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(name)
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(data => {
                throw new Error(data || 'Fetching failed');
            });
        } else {
            alert("Chat created successfully!");
        }
    })
}

export const addUserToChat = (chatId, username) => {
    return fetch(`${baseUrl}/chats/${chatId}/users?username=${username}`, {
        method: 'POST',
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
            alert("User added successfully!");
        }
    })
}

export const deleteUserFromChat = (chatId, username) => {
    return fetch(`${baseUrl}/chats/${chatId}/users?username=${username}`, {
        method: 'DELETE',
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
            alert("User deleted successfully!");
        }
    })
}

const { Observable } = rxjs;
let chatSubscriptions = new Map();

function subscribeToChatNotifications(chatId) {
    return new Observable(observer => {
        const eventSource = new EventSource(`http://localhost:8080/notifications/${chatId}`);

        eventSource.onmessage = event => {
            observer.next(JSON.parse(event.data));
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

export function unsubscribeFromChats() {
    for (const [chatId, subscription] of chatSubscriptions) {
        subscription.unsubscribe();
        chatSubscriptions.delete(chatId);
    }
}


export function subscribeToChats(chats, ctx) {
    for (const chat of chats) {
        if (!chatSubscriptions.has(chat.id)) {
            const subscription = subscribeToChatNotifications(chat.id).subscribe(
                notification => {
                    console.log(notification)
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