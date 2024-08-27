import { html } from "lit-html";
import { Message } from "../utilities/interfaces";

export const messagesViewTemplate = (messages: Message[]) => html`
<textarea readonly id="chat-box">
 ${messages.slice().reverse().map(x => html`${x.timestamp} ${x.creator.username}: \n${x.content}\n\n`)}
</textarea>
`
