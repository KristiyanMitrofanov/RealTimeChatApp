import { render, html } from "lit-html";
import { getChats, subscribeToChats } from "../services/chatService"
import { checkAuthentication, checkUser } from "../utilities/validators";
import { navigateToChatHandler } from "../utilities/handlers";
import { disconnectFromSocket } from "../services/websocketService";
import { navigationViewTemplate } from './navigationView';
import { Context } from 'page';
import { Handler } from "../utilities/types";
import { Client } from "@stomp/stompjs";
import { Chat } from "../utilities/interfaces";
import devexpertsChat from '../static/img/devexpertsChat.png';

const withContext = (handler: Handler, ctx: Context, id?: string, client?: Client) => (e: Event) => {
  handler(e, ctx, client, id);
};

const chatsUserViewTemplate = (chats: Chat[], ctx: Context) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
  <h3 class="heading">Please choose a chat room to enter:</h3>
  <div class="chats-container">
    ${chats.map(
  (chat) =>
    html`
          <div class="card">
            <a href="" @click=${withContext(navigateToChatHandler, ctx, chat.id)}>
              <img src="${devexpertsChat}" alt="Chat Image">
              <div class="container">
                <h4><b>${chat.name}</b></h4>
              </div>
            </a>
          </div>
          `
)}
  </div>
`

const noChatsUserViewtemplate = (message: string) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
<p>${message}</p>
`
const root = document.getElementById('root') as HTMLElement;

export const userView = (ctx: Context) => {
  disconnectFromSocket();
  if (!checkAuthentication(ctx)) return;
  if (!checkUser(ctx)) return;
  getChats()
    .then(chats => {
      subscribeToChats(chats, ctx);
      render(chatsUserViewTemplate(chats, ctx), root);
    })
    .catch(err => {
      if (err.message.includes("Token")) {
        alert(err.message);
        ctx.page.redirect('/login');
        localStorage.clear();
      } else {
        render(noChatsUserViewtemplate(err.message), root);
      }
    });
}