import { getChats } from "../services/chatService.js";
import { render , html } from "../node_modules/lit-html/lit-html.js";
import { navigationViewTemplate } from "./navigationView.js";
import { deleteChatHandler, createChatHandler } from "../utilities/handlers.js";
import { checkAuthentication, checkAdmin } from "../utilities/validators.js";

const chatsAdminViewTemplate = (chats, ctx) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
  <h3 class="heading">Admin Panel</h3>

  <div class="error-container">
  <p id="errorField"></p>
</div>
<form class="nickname-container">
<input id="name" type="text">
<button class="input-button" @click=${createChatHandler}>Create</button> 
</form>
<div class="chats-container">
    ${chats.content.map(
      (x) =>
        html`
          <div class="card">
            <a href="/chats/${x.id}">
              <img src="../img/devexpertsChat.png" alt="Chat Image">
              <div class="container">
                <h4><b>${x.name}</b></h4>
              </div>
            </a>
            <button id="delete-button" data-id="${x.id}" @click=${deleteChatHandler}>Delete</button>
          </div>
        `
    )}
  </div>
`

const noChatsAdminViewtemplate = (message) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
<p>${message}</p>
`
const root =  document.getElementById('root');


export const adminView = (ctx) => {
  if (!checkAuthentication(ctx)) return;
  if (!checkAdmin(ctx)) return;
    getChats()
        .then(chats => {
        render(chatsAdminViewTemplate(chats), root);
    })
    .catch(err => {
        if (err.message.includes("Token")) {
            alert(err.message);
            ctx.page.redirect('/login');
            localStorage.clear();
        } else {
            render(noChatsAdminViewtemplate(err.message), root);
        }
    });
}


