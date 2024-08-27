import { Context } from "page";
import { render } from "lit-html";
import { chatViewTemplate, renderChatView } from "../views/chatView";
import { getChat } from "./chatService";
import { Client, Message } from '@stomp/stompjs';
import SockJS from "sockjs-client";
import { isNotNull, isNotUndefined } from "../utilities/typeGuards";

export let stompClient: Client;

const root = document.getElementById('root') as HTMLElement;

export async function connectToWebSocket(ctx: Context): Promise<void> {
    if (!root) {
        console.error('Root element not found');
        return;
    }

    if (!stompClient) {
        const socket = new SockJS(`http://localhost:8080/ws?token=${localStorage.getItem('token')}`);

        stompClient = new Client({
            webSocketFactory: () => socket,
            connectHeaders: {},
            debug: (str) => console.log(str),
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: async (frame) => {
                try {
                    const chat = await getChat(ctx.params.chatId);
                    subscribeToChat(ctx);
                    render(chatViewTemplate(chat, ctx, stompClient), root);
                } catch (err: any) {
                    alert(err.message);
                    ctx.page.redirect('/user');
                }
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            }
        });
    }

    if (stompClient) {
        stompClient.activate();
    }
}

export function disconnectFromSocket(): void {
    if (isNotUndefined(stompClient)) {
        stompClient.deactivate();
    }
}

export function subscribeToChat(ctx: Context): void {
    if (isNotNull(stompClient)) {
        stompClient.subscribe(`/topic/chats/${ctx.params.chatId}`, (message: Message) => {
            const chat = JSON.parse(message.body);
            renderChatView(chat, ctx);
        });
    }
}
