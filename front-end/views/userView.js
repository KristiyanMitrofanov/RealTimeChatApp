import { render , html } from "../node_modules/lit-html/lit-html.js";
import { getChats, subscribeToChats} from "../services/chatService.js"
import { checkAuthentication, checkUser } from "../utilities/validators.js";
import { navigateToChatHandler } from "../utilities/handlers.js";
import { disconnectFromSocket } from "../services/websocketService.js";
import { navigationViewTemplate } from './navigationView.js';

const withContext = (handler, ctx, chatId) => (e) => handler(e, ctx, chatId);

const chatsUserViewTemplate = (chats, ctx) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
  <h3 class="heading">Please choose a chat room to enter:</h3>
  <div class="chats-container">
    ${chats.content.map(
      (x) =>
        html`
          <div class="card">
            <a href="" @click=${withContext(navigateToChatHandler, ctx, x.id)}>
              <img src="../img/devexpertsChat.png" alt="Chat Image">
              <div class="container">
                <h4><b>${x.name}</b></h4>
              </div>
            </a>
          </div>
          `
    )}
  </div>
`

const noChatsUserViewtemplate = (message) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
<p>${message}</p>
`
const root =  document.getElementById('root');

export const userView = (ctx) => {
    disconnectFromSocket();
    if (!checkAuthentication(ctx)) return;
    if (!checkUser(ctx)) return;
    getChats()
        .then(chats => {
        subscribeToChats(chats.content, ctx);
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