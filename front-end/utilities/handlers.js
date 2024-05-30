import { addUserToChat, createChat, deleteChat, deleteUserFromChat } from "../services/chatService.js";
import { checkAuthentication, validateMessage } from "./validators.js";
import { sendMessage } from "../services/messageService.js";
import { adminView } from "../views/adminView.js";
import { login } from '../services/authService.js';
import { register } from '../services/authService.js';
import { displayError, displayModalError } from "./renderers.js";

export function navigateToChatHandler(e, ctx, chatId) {
    e.preventDefault();
    ctx.page.redirect(`/chats/${chatId}`);
}

export async function deleteChatHandler(e) {
    checkAuthentication();
    e.preventDefault();
    let button = e.currentTarget;
    let chatId = button.dataset.id;
    deleteChat(chatId)
    .then(res => {
        errorField.style.display = 'none';
            adminView();
    })
    .catch(err => { 
        errorField.style.display = 'none';
            displayError(err);
    })
}

export  async function createChatHandler(e) {
    checkAuthentication();
    e.preventDefault();
    let name = document.getElementById('name').value;
    createChat(name)
    .then(res => {
            name = '';
        errorField.style.display = 'none';
            adminView();
    })
    .catch(err => {
            name = '';
        errorField.style.display = 'none';
            displayError(err);
    })
}


export async function addUserHandler(e, ctx) {
    checkAuthentication();
    e.preventDefault();
    let button = e.currentTarget;
    let chatId = button.dataset.id;
    let username = document.getElementById('add-name').value;
    let errorField = document.getElementById('errorField-modal');
    addUserToChat(chatId, username)
    .then(res => {
            username = '';
        errorField.style.display = 'none';
            ctx.page.redirect(`/chats/${chatId}`);
    })
    .catch(err => {
            username = '';
        errorField.style.display = 'none';
            displayModalError(err);
    })
}

export async function deleteUserHandler(e, ctx) {
    checkAuthentication();
    e.preventDefault();
    let button = e.currentTarget;
    let chatId = button.dataset.id;
    let username = button.dataset.name;
    deleteUserFromChat(chatId, username)
    .then(res => {
        errorField.style.display = 'none';
        ctx.page.redirect(`/chats/${chatId}`);
    })
    .catch(err => {
        errorField.style.display = 'none';
            displayModalError(err);
    })
}

export async function sendMessageHandler(e, stompClient) {
try {
checkAuthentication();
e.preventDefault();
let button = e.currentTarget;
let chatId = button.dataset.id;
let messageField = document.getElementById('message-input');
let message = messageField.value;
let errorField = document.getElementById('errorField');
validateMessage(message);
sendMessage(chatId, message, stompClient);
errorField.textContent = '';
messageField.value = '';
} catch (err) {
    displayError(err);
}
}

export const loginHandler = (e, ctx) => {
    e.preventDefault();
    let {username, password} = Object.fromEntries(new FormData(e.currentTarget));
    login(username, password)
        .then(user => {
            if (localStorage.getItem("role") == "USER") {
                ctx.page.redirect('/user');
            } else {
                ctx.page.redirect('/admin');
            }
        })
        .catch(err => {
            displayError(err);
        });

};

export const registerHandler = (e, ctx) => {
    e.preventDefault();

    let {username, email,  password, confirmPassword} = Object.fromEntries(new FormData(e.currentTarget));
    register(username, email, password, confirmPassword)
        .then(user => {
            ctx.page.redirect('/login');
            alert("User successfully registered");
        })
        .catch(err => {
            displayError(err);
        });
};