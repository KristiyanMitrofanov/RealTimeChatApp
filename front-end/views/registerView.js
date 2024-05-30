import { render, html } from '../node_modules/lit-html/lit-html.js';
import { navigationViewTemplate } from './navigationView.js';
import { disconnectFromSocket} from "../services/websocketService.js";
import { registerHandler } from '../utilities/handlers.js';

const withContext = (handler, ctx) => (e) => handler(e, ctx);

const registerViewTemplate = (registerHandler, ctx) => html`
${navigationViewTemplate(localStorage.getItem('token'))}
<div class="main-container">
<h3 class="heading">Please fill in the register form:</h3>

<p id="errorField" display="none"></p>


<form @submit=${withContext(registerHandler, ctx)} id="register-data">
<div class="input-container">
<label for="username">Username:</label>
<input type="text" name="username">

<label for="email">Email:</label>
<input type="text" name="email">

<label for="password">Password:</label>
<input type="password" name="password">

<label for="confirmPassword">Confirm Password:</label>
<input type="password" name="confirmPassword">

<button class="input-button">Register</button>
</div>
</form>

</div>
`
const root = document.getElementById('root');

export const registerView = (ctx) => {
    disconnectFromSocket();
    render(registerViewTemplate(registerHandler, ctx), root);
};