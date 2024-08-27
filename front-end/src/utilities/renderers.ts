import { Context } from "page";
import { Notification } from "./interfaces";

export function renderNotification(notification: Notification, ctx: Context) {
    if (notification.sender !== localStorage.getItem('username')) {
        const notificationInstance = new Notification(notification.sender, {
            body: notification.message
        });

        notificationInstance.onclick = function () {
            ctx.page.redirect(`/chats/${notification.chatId}`);
            notificationInstance.close();
        };
    }
}

export const openModal = function (): void {
    const modal = document.querySelector('.modal-info') as HTMLElement | null;
    const overlay = document.querySelector('.overlay') as HTMLElement | null;

    if (modal) {
        modal.classList.remove("hidden");
    }

    if (overlay) {
        overlay.classList.remove("hidden");
    }
};

export const closeModal = function (): void {
    const modal = document.querySelector('.modal-info') as HTMLElement | null;
    const overlay = document.querySelector('.overlay') as HTMLElement | null;

    if (modal) {
        modal.classList.add("hidden");
    }

    if (overlay) {
        overlay.classList.add("hidden");
    }
};

export function displayError(err: Error): void {
    const errorField = document.getElementById('errorField') as HTMLElement | null;
    if (errorField) {
        errorField.textContent = err.message;
        errorField.style.display = "inline";
    }
}

export function displayModalError(err: Error): void {
    const errorField = document.getElementById('errorField-modal') as HTMLElement | null;
    if (errorField) {
        errorField.textContent = err.message;
        errorField.style.display = "inline";
    }
}