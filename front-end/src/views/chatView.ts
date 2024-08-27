import { render, html, nothing, TemplateResult } from 'lit-html';
import { navigationViewTemplate } from './navigationView';
import { messagesViewTemplate } from './messagesView';
import { checkAuthentication } from '../utilities/validators';
import { closeModal, openModal } from '../utilities/renderers';
import { sendMessageHandler, deleteChatHandler, addUserHandler, deleteUserHandler } from "../utilities/handlers";
import { connectToWebSocket, stompClient } from '../services/websocketService';
import { Chat } from '../utilities/interfaces';
import { Context } from 'page';
import { Client } from '@stomp/stompjs';
import { Handler } from '../utilities/types';


const withContext = (handler: Handler, ctx: Context, client?: Client, id?: string) => (e: Event) => {
  handler(e, ctx, client, id);
};

const sendMessageTemplate = (chat: Chat, ctx: Context, stompClient: Client): TemplateResult => html`
  <div class="message-container">
    <textarea name="message" placeholder="Please type your message here..." id="message-input"></textarea>
    <button id="submit-input" data-id="${chat.id}" @click=${withContext(sendMessageHandler, ctx, stompClient)}>Send</button>
  </div>
`;

const userManagementTemplate = (chat: Chat, ctx: Context): TemplateResult => html`
  <section class="modal-info hidden">
    <div class="flex">
      <button class="btn-close" @click=${closeModal}>⨉</button>
    </div>
    <div class="chat-admin-controls">
      <button data-id="${chat.id}" @click=${withContext(addUserHandler, ctx)}>Add User</button>
      <input id="add-name" type="text" placeholder="Enter a username..." />
    </div>

    <div class="error-container">
      <p id="errorField-modal"></p>
    </div>

    <table class="users-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        ${chat.users.map(user => html`
          <tr key=${user.id}>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td><button data-id="${chat.id}" data-name="${user.username}" @click=${withContext(deleteUserHandler, ctx)}>Delete</button></td>
          </tr>
        `)}
      </tbody>
    </table>
  </section>
  <div class="overlay hidden" @click=${closeModal}></div>
  <button class="btn btn-open" @click=${openModal}>User Management</button>
`;

export const chatViewTemplate = (chat: Chat, ctx: Context, stompClient: Client): TemplateResult => {
  const userRole = localStorage.getItem('role');
  return html`
    ${navigationViewTemplate(localStorage.getItem('token'))}
    <div class="header-container">
      ${userRole === "ADMIN" ? userManagementTemplate(chat, ctx) : nothing}
      <h2 class="nickname-container">${chat.name}</h2>
      ${userRole === "ADMIN" ? html`<button id="delete-button" data-id="${chat.id}" @click=${deleteChatHandler} class="delete-button">Delete Chat</button>` : nothing}
    </div>
    <div class="error-container">
      <p id="errorField"></p>
    </div>
    <div id="container">
      ${messagesViewTemplate(chat.history)}
    </div>
    ${userRole === 'USER' ? sendMessageTemplate(chat, ctx, stompClient) : nothing}
  `;
};

const root = document.getElementById('root') as HTMLElement | null;

export const chatView = async (ctx: Context): Promise<void> => {
  if (!checkAuthentication(ctx)) return;
  try {
    connectToWebSocket(ctx);
  } catch (err) {
    alert((err as Error).message);
    ctx.page.redirect('/user');
  }
};

export function renderChatView(chat: Chat, ctx: Context): void {
  if (root) {
    render(chatViewTemplate(chat, ctx, stompClient!), root);
  } else {
    console.error('Root element not found');
  }
}








