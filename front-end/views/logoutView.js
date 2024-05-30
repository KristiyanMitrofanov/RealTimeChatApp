import { unsubscribeFromChats } from "../services/chatService.js";

export const logoutView = (ctx) => {
        unsubscribeFromChats();
        localStorage.clear();
        ctx.page.redirect('/login');
}
