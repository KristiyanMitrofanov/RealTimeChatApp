import { Context } from "page";

export function checkAuthentication(ctx?: Context): boolean | void {
    const token = localStorage.getItem('token');
    if (token === null || token === '') {
        ctx!.page.redirect('/login');
        return false;
    }
    return true;
}

function checkRole(ctx: Context, requiredRole: string): boolean {
    const role = localStorage.getItem('role');
    if (role !== requiredRole) {
        ctx.page.redirect('/login');
        return false;
    }
    return true;
}

export function checkAdmin(ctx: Context): boolean {
    return checkRole(ctx, 'ADMIN');
}

export function checkUser(ctx: Context): boolean {
    return checkRole(ctx, 'USER');
}

export function validateMessage(message: string): void {
    if (message.trim() === '') {
        throw new ValidationError("The message cannot be empty!");
    } else if (message.length > 250) {
        throw new ValidationError("You have exceeded the maximum limit of 250 characters!");
    }
}







