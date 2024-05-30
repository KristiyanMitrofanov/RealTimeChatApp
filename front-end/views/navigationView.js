import { html, nothing } from "../node_modules/lit-html/lit-html.js"

export const navigationViewTemplate = (isAuthenticated) => {
const role = localStorage.getItem('role');
const username = localStorage.getItem('username');

return html`
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a id="home-button" class="navbar-brand">dxChat</a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        ${isAuthenticated
          ? html`
            <li class="nav-item">
              <a id="logout-button" class="nav-link" href="/logout">Logout</a>
            </li>
            ${role === 'ADMIN'
              ? html`
                  <li class="nav-item">
                    <a id="admin-button" class="nav-link" href="/admin">Admin Panel</a>
                  </li>
                `
              : html`
                  <li class="nav-item">
                    <a id="admin-button" class="nav-link" href="/user">User Panel</a>
                  </li>
                `}
          `
          : html`
            <li class="nav-item">
              <a class="nav-link" href="/login">Login</a>
            </li>
            <li class="nav-item">
              <a id="register-button" class="nav-link" href="/register">Register</a>
            </li>
          `}
      </ul>

      ${isAuthenticated
        ? html`
            <form class="form-inline my-2 my-lg-0">
              <span id="username-field" class="form-control mr-sm-2">${username}</span>
            </form>
          `
        : nothing}
    </div>
  </nav>

  <div class="image-container">
    <img src="/img/devexperts.png" alt="">
  </div>
`;};