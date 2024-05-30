import { render, html } from '../node_modules/lit-html/lit-html.js';
import { navigationViewTemplate } from './navigationView.js';
import { disconnectFromSocket } from "../services/websocketService.js";
import { loginHandler } from '../utilities/handlers.js';

const withContext = (handler, ctx) => (e) => handler(e, ctx);

const loginViewTemplate = (loginHandler, ctx) => html`
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
const root = document.getElementById('root');

export const loginView = (ctx) => {
    disconnectFromSocket();
    render(loginViewTemplate(loginHandler, ctx), root);
}

