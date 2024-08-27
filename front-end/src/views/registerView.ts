import { render, html, TemplateResult } from 'lit-html';
import { navigationViewTemplate } from './navigationView';
import { disconnectFromSocket } from "../services/websocketService";
import { registerHandler } from '../utilities/handlers';
import { Context } from 'page';
import { Handler } from '../utilities/types';

const withContext = (handler: Handler, ctx: Context) => (e: Event) => handler(e, ctx);

const registerViewTemplate = (registerHandler: Handler, ctx: Context): TemplateResult => html`
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
const root = document.getElementById('root') as HTMLElement;

export const registerView = (ctx: Context) => {
    disconnectFromSocket();
    render(registerViewTemplate(registerHandler, ctx), root);
};