import { html } from "../node_modules/lit-html/lit-html.js";

export const messagesViewTemplate = (messages) => html`
<textarea readonly id="chat-box">
 ${messages.slice().reverse().map(x => html`${x.timestamp} ${x.creator.username}: \n${x.content}\n\n`)}
</textarea>
`
