import { fetchWithHandling } from "../utilities/typeGuards";
import { Information, Login, Register } from "../utilities/types";

let baseUrl = 'http://localhost:8080/api';

function saveInformation(info: Information): void {
    localStorage.setItem("token", info.token);
    localStorage.setItem("username", info.username);
    localStorage.setItem("role", info.role);
}

export const login = async (loginInformation: Login): Promise<void> => {
    const info = await fetchWithHandling<Information>(`${baseUrl}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginInformation)
    });
    saveInformation(info);
}

export const register = async (registerInformation: Register): Promise<void> => {
    await fetchWithHandling<void>(`${baseUrl}/auth/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(registerInformation)
    });
}
