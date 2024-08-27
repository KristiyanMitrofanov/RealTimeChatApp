import { addUserToChat, createChat, deleteChat, deleteUserFromChat } from "../services/chatService";
import { checkAuthentication, validateMessage } from "./validators";
import { sendMessage } from "../services/messageService";
import { adminView } from "../views/adminView";
import { login } from '../services/authService';
import { register } from '../services/authService';
import { displayError, displayModalError } from "./renderers";
import { isNotNull } from "./typeGuards";
import { Handler } from "./types";

const errorField = document.getElementById('errorField-modal') as HTMLElement | null;

export const navigateToChatHandler: Handler = async (e, ctx, _, chatId) => {
    e.preventDefault();
    if (ctx && chatId) {
        ctx.page.redirect(`/chats/${chatId}`);
    }
}

export const deleteChatHandler: Handler = async (e, ctx) => {
    checkAuthentication();
    e.preventDefault();
    const button = e.currentTarget as HTMLElement | null;
    const chatId = button?.dataset.id;
    if (!chatId) return;

    try {
        await deleteChat(chatId);
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        adminView();
    } catch (err) {
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        displayError(err as Error);
    }
}

export const createChatHandler: Handler = async (e, ctx) => {
    checkAuthentication();
    e.preventDefault();
    const nameInput = document.getElementById('name') as HTMLInputElement | null;
    const name = nameInput?.value;
    if (!name) return;

    try {
        await createChat(name);
        if (nameInput) nameInput.value = '';
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        adminView();
    } catch (err) {
        if (nameInput) nameInput.value = '';
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        displayError(err as Error);
    }
}

export const addUserHandler: Handler = async (e, ctx) => {
    checkAuthentication();
    e.preventDefault();
    const button = e.currentTarget as HTMLElement | null;
    const chatId = button?.dataset.id;
    const usernameInput = document.getElementById('add-name') as HTMLInputElement | null;
    const username = usernameInput?.value;
    if (!chatId || !username) return;

    try {
        await addUserToChat(chatId, username);
        if (usernameInput) usernameInput.value = '';
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        if (ctx) {
            ctx.page.redirect(`/chats/${chatId}`);
        }
    } catch (err) {
        if (usernameInput) usernameInput.value = '';
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        displayModalError(err as Error);
    }
}

export const deleteUserHandler: Handler = async (e, ctx) => {
    checkAuthentication();
    e.preventDefault();
    const button = e.currentTarget as HTMLElement | null;
    const chatId = button?.dataset.id;
    const username = button?.dataset.name;
    if (!chatId || !username) return;

    try {
        await deleteUserFromChat(chatId, username);
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        if (ctx) {
            ctx.page.redirect(`/chats/${chatId}`);
        }
    } catch (err) {
        if (isNotNull(errorField)) {
            errorField.style.display = 'none';
        }
        displayModalError(err as Error);
    }
}

export const sendMessageHandler: Handler = async (e, _, client, chatId) => {
    checkAuthentication();
    e.preventDefault();
    const button = e.currentTarget as HTMLElement | null;
    const messageField = document.getElementById('message-input') as HTMLInputElement | null;
    const message = messageField?.value;
    if (!chatId || !message || !client) return;

    try {
        validateMessage(message);
        await sendMessage(chatId, message);

        if (isNotNull(errorField)) {
            errorField.textContent = '';
        }
        if (messageField) {
            messageField.value = '';
        }
    } catch (err) {
        displayError(err as Error);
    }
}

export const loginHandler: Handler = async (e, ctx) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget as HTMLFormElement);
    const username = formData.get('username') as string;
    const password = formData.get('password') as string;
    if (!username || !password) return;

    try {
        await login({ username, password });
        if (localStorage.getItem("role") === "USER") {
            ctx!.page.redirect('/user');
        } else {
            ctx!.page.redirect('/admin');
        }
    } catch (err) {
        displayError(err as Error);
    }
};

export const registerHandler: Handler = async (e, ctx) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget as HTMLFormElement);
    const username = formData.get('username') as string;
    const email = formData.get('email') as string;
    const password = formData.get('password') as string;
    const confirmPassword = formData.get('confirmPassword') as string;
    if (!username || !email || !password || !confirmPassword) return;

    const registerInformation = {
        username,
        email,
        password,
        confirmPassword
    };

    try {
        await register(registerInformation);
        ctx!.page.redirect('/login');
        alert("User successfully registered");
    } catch (err) {
        displayError(err as Error);
    }
};