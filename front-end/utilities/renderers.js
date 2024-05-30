export function renderNotification(notification, ctx) {
    if (notification.sender !== localStorage.getItem('username')) {
        let notificationInstance = new Notification(notification.sender, {
            body: notification.message
        });

        notificationInstance.onclick = function() {
            ctx.page.redirect(`/chats/${notification.chatId}`);
            notificationInstance.close();
        };
    }
}

export const openModal = function () {
    let modal = document.querySelector('.modal-info');
    let overlay = document.querySelector('.overlay');
    modal.classList.remove("hidden");
    overlay.classList.remove("hidden");
  };

export const closeModal = function () {
    let modal = document.querySelector('.modal-info');
    let overlay = document.querySelector('.overlay');
    modal.classList.add("hidden");
    overlay.classList.add("hidden");
  };

export function displayError(err) {
    let errorField = document.getElementById('errorField');
    errorField.textContent = err.message;
    errorField.style.display = ("inline");
}

export function displayModalError(err) {
    let errorField = document.getElementById('errorField-modal');
    errorField.textContent = err.message;
    errorField.style.display = ("inline");
}