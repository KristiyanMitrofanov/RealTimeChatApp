export function checkAuthentication(ctx) {
        if (!localStorage.getItem('token')) {
            ctx.page.redirect('/login');
            return false; 
        }
        return true;
    }

export function checkAdmin(ctx) {
        if (localStorage.getItem('role') !== 'ADMIN') {
            ctx.page.redirect('/login');
            return false; 
        }
        return true; 
    }

    export function checkUser(ctx) {
        if (localStorage.getItem('role') !== 'USER') {
            ctx.page.redirect('/login');
            return false; 
        }
        return true; 
    }
    

export function validateMessage(message) {
    if (message.trim() === '') {
        throw new Error("The message cannot be empty!")
    } else if (message.length > 250) {
        throw new Error("You have exceeded the maximum limit of 250 characters!");
    }
}







