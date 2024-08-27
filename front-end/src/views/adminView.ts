import { getChats } from "../services/chatService";
import { render, html, TemplateResult } from "lit-html";
import { navigationViewTemplate } from "./navigationView";
import { deleteChatHandler, createChatHandler } from "../utilities/handlers";
import { checkAuthentication, checkAdmin } from "../utilities/validators";
import { Chat } from "../utilities/interfaces";
import devexpertsChat from '../static/img/devexpertsChat.png';
const root = document.getElementById('root') as HTMLElement | null;

const chatsAdminViewTemplate = (chats: Chat[]): TemplateResult => html`
  ${navigationViewTemplate(localStorage.getItem('token'))}
  <h3 class="heading">Admin Panel</h3>

  <div class="error-container">
    <p id="errorField"></p>
  </div>
  <form class="nickname-container">
    <input id="name" type="text">
    <button type="button" class="input-button" @click=${createChatHandler}>Create</button>
  </form>
  <div class="chats-container">
    ${chats.length > 0
    ? chats.map(
      (chat: Chat) => html`
          <div class="card">
            <a href="/chats/${chat.id}">
              <img src="${devexpertsChat}" alt="Chat Image">
              <div class="container">
                <h4><b>${chat.name}</b></h4>
              </div>
            </a>
            <button type="button" id="delete-button" data-id="${chat.id}" @click=${deleteChatHandler}>Delete</button>
          </div>
        `
    )
    : html`<p>No chats available.</p>`
  }
  </div>
`;

const noChatsAdminViewTemplate = (message: string): TemplateResult => html`
  ${navigationViewTemplate(localStorage.getItem('token'))}
  <p>${message}</p>
`;

export const adminView = (ctx?: PageJS.Context): void => {
  if (!checkAuthentication(ctx)) return;
  if (!checkAdmin(ctx!)) return;

  if (root === null) {
    console.error('Root element not found');
    return;
  }

  getChats()
    .then((chats: Chat[]) => {
      render(chatsAdminViewTemplate(chats), root);
    })
    .catch((err: Error) => {
      if (err.message.includes("Token")) {
        alert(err.message);
        ctx!.page.redirect('/login');
        localStorage.clear();
      } else {
        render(noChatsAdminViewTemplate(err.message), root);
      }
    });
};