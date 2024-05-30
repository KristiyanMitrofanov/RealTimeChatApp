let baseUrl = 'http://localhost:8080/api';

function saveInformation(info) {
    localStorage.setItem("token", info.token);
    localStorage.setItem("username", info.username)
    localStorage.setItem("role", info.role);
}

export const login = (username, password) => {
    return fetch(`${baseUrl}/auth/login`, {
        method: 'POST',
        headers: {
            'content-type' : 'application/json'
        },
        body: JSON.stringify({username, password})
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(data => {
                throw new Error(data || 'Login failed');
            });
        } else {
            return res.json().then(info => {
                saveInformation(info);
            })
        }
    })
}

export const register = (username, email, password, confirmPassword) => {
    return fetch(`${baseUrl}/auth/register`, {
        method: 'POST',
        headers: {
            'content-type' : 'application/json'
        },
        body: JSON.stringify({username, email, password, confirmPassword})
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(data => {
                    throw new Error(data || 'Registration failed');
                });
            }
        }) 
        
}
