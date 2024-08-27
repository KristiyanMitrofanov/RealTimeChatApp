import { Context } from "page";
import { unsubscribeFromChats } from "../services/chatService";

export const logoutView = (ctx: Context) => {
        unsubscribeFromChats();
        localStorage.clear();
        ctx.page.redirect('/login');
}
