import { render, html } from 'lit-html';
import { navigationViewTemplate } from './navigationView';
import { disconnectFromSocket } from "../services/websocketService";
import { loginHandler } from '../utilities/handlers';
import { Context } from 'page';
import { Handler } from '../utilities/types';

const withContext = (handler: Handler, ctx: Context) => (e: Event) => handler(e, ctx);

const loginViewTemplate = (loginHandler: Handler, ctx: Context) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
  <h1 class="heading">Welcome To dxChat</h1>

<div class="main-container">
<h3 class="heading">Please provide your login credentials:</h3>

<form @submit=${withContext(loginHandler, ctx)} id="login-data">

<p id="errorField"></p>
<div class="input-container">
<label for="username">Username:</label>
<input type="text" name="username">

<label for="password">Password:</label>
<input type="password" name="password">

<button class="input-button">Login</button>


</div>
</form>
</div>
`
const root = document.getElementById('root') as HTMLElement;

export const loginView = (ctx?: Context) => {
  disconnectFromSocket();
  render(loginViewTemplate(loginHandler, ctx!), root);
}

