import { render } from "../node_modules/lit-html/lit-html.js";
import { chatViewTemplate, renderChatView } from "../views/chatView.js";
import { getChat } from "./chatService.js";
export let stompClient = null;

  export async function connectToWebSocket(ctx) {
    if (!stompClient) {
        const socket = new SockJS(`http://localhost:8080/ws?token=${localStorage.getItem('token')}`);
        stompClient = Stomp.over(socket);
        
        stompClient.connect({}, async function(frame) {
          try{
            let chat = await getChat(ctx.params.chatId);
            subscribeToChat(ctx, chat);
            render(chatViewTemplate(chat, ctx, stompClient), root);
           } catch (err) {
            alert(err.message);
            ctx.page.redirect('/user');
        }
        });
    } else {
       try{
        let chat = await getChat(ctx.params.chatId);
        subscribeToChat(ctx, chat);
        render(chatViewTemplate(chat, ctx, stompClient), root);
       } catch (err) {
        alert(err.message);
        ctx.page.redirect('/user');
    }
    }
}

export function disconnectFromSocket() {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log('Disconnected');
        })
    }
}

export function subscribeToChat(ctx) {
    stompClient.subscribe(`/topic/chats/${ctx.params.chatId}`, function(message) {
        const chat = JSON.parse(message.body);
        renderChatView(chat, ctx);
      });    
  }