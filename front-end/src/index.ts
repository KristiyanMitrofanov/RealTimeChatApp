import page from 'page';
import { chatView } from './views/chatView';
import { userView } from './views/userView';
import { loginView } from './views/loginView';
import { logoutView } from './views/logoutView';
import { registerView } from './views/registerView';
import { adminView } from './views/adminView';
import { Context } from 'page';
import './static/styles.css';

page('/login', loginView);
page('/chats/:chatId', chatView);
page('/register', registerView);
page('/logout', logoutView);
page('/admin', adminView);
page('/user', userView);
page('/', loginView);
page('*', redirectToLogin);
page.start();

function redirectToLogin(ctx: Context) {
    ctx.page.redirect('/login');
}






