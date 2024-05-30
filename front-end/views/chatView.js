import { render, html, nothing } from '../node_modules/lit-html/lit-html.js';
import { navigationViewTemplate } from './navigationView.js';
import { messagesViewTemplate } from './messagesView.js';
import { checkAuthentication } from '../utilities/validators.js';
import { closeModal, openModal } from '../utilities/renderers.js';
import { sendMessageHandler, deleteChatHandler, addUserHandler, deleteUserHandler } from "../utilities/handlers.js";
import { connectToWebSocket, stompClient } from '../services/websocketService.js';
const withContext = (handler, ctx, stompClient, chat) => (e) => handler(e, ctx, stompClient, chat);

const sendMessageTemplate = (chat, ctx, stompClient) => {
    return html`
    <div class="message-container">
      <textarea name="message" placeholder="Please type your message here..." id="message-input"></textarea>
      <button id="submit-input" data-id="${chat.id}" @click=${withContext(sendMessageHandler, ctx, stompClient, chat)}>Send</button>
    </div>`
}

const userManagamentTemplate = (chat, ctx) => {
    return html`
<section class="modal-info hidden">
  <div class="flex">
    <button class="btn-close" @click=${closeModal}>⨉</button>
  </div>
  <div class="chat-admin-controls">
      <button data-id="${chat.id}"  @click=${withContext(addUserHandler, ctx)}>Add User</button>
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
<button class="btn btn-open" @click=${openModal}>User Management</button>`
}

export const chatViewTemplate = (chat, ctx, stompClient) =>  {
const userRole = localStorage.getItem('role');
return html`
${navigationViewTemplate(localStorage.getItem('token'))}

<div class="header-container">  
  ${userRole === "ADMIN" 
              ? html `
              ${userManagamentTemplate(chat, ctx)}
              ` : nothing
  }

  <h2 class="nickname-container">${chat.name}</h2>

  ${userRole === "ADMIN"
              ? html `
                <button id="delete-button" data-id="${chat.id}" @click=${deleteChatHandler} class="delete-button">Delete Chat</button>
              ` : nothing
  }

</div>

<div class="error-container">
    <p id="errorField"></p>
</div>

<div id="container">
  ${messagesViewTemplate(chat.history)}
</div>

${userRole === 'USER' ? html`
  ${sendMessageTemplate(chat, ctx, stompClient)}
` : nothing}
`
};

const root =  document.getElementById('root');

export const chatView = async (ctx) => {
  if (!checkAuthentication(ctx)) return;
  try {
      connectToWebSocket(ctx);
  } catch (err) {
      alert(err.message);
      ctx.page.redirect('/user');
  }
};

export function renderChatView(chat, ctx) {
  render(chatViewTemplate(chat, ctx, stompClient), root);
}









