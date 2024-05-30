import page from './node_modules/page/page.mjs'
import { chatView } from './views/chatView.js';
import { userView } from './views/userView.js';
import { loginView } from './views/loginView.js';
import { logoutView } from './views/logoutView.js';
import { registerView } from './views/registerView.js';
import { adminView } from './views/adminView.js';

page('/login', loginView);
page('/chats/:chatId', chatView);
page('/register', registerView);
page('/logout', logoutView);
page('/admin', adminView);
page('/user', userView);
page('/', loginView);
page('*', redirectToLogin);
page.start();

function redirectToLogin(ctx) {
    ctx.page.redirect('/login');
}






