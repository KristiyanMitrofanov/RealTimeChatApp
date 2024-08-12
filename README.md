# RealTimeChatWebApp

This project is a real-time chat application that supports multiple chat participation.

<br>

 <h2>Table of Contents</h2>
    <ul>
        <li><a href="knowledge">Knowledge Goals</a></li>
        <li><a href="#technologies">Technologies and Principles</a></li>
        <li><a href="#main-requirements">Main Functionalities</a></li>
        <li><a href="#public-part">Public Part</a></li>
        <li><a href="#private-part">Private Part</a></li>
        <li><a href="#administration-part">Administration Part</a></li>
        <li><a href="#rest-api">REST API</a></li>
        <li><a href="#database">Database</a></li>
    </ul>
<br>

<h2 id="#knowledge">Knowledge Goals</h2>

The purpose of the project was to help me gain knowledge about:
<ul>
  <li>implementing the <strong>WebSockets</strong> protocol for real-time communication</li>
  <li>implementing <strong>Spring Web-flux</strong> and <strong>RXJs</strong> for optimizing the resource usage by leveraging non-blocking, reactive programming model</li>
  <li>implementing notification mechanism by utilizing <strong>Server-Sent Events</strong></li>
  <li>implementing <strong>Docker</strong> for containerization of the backend and frontend components for improved portability, scalability, and deployment consistency</li>
  <li>implementing <strong>Docker-compose</strong> in order to ease the management and orchestration of the multi-container Docker app</li>
  <li>implementing <strong>JWT Authentication</strong> with <strong>Spring Security</strong> to provide more secure authentication and authorization mechanisms</li>
  <li>creating a <strong>CI/CD</strong> pipeline using <strong>TeamCity</strong></li>
  <li>creating a Single-Page Application that enhances the UI/UX using <strong>lit-html</strong> and <strong>page.js</strong></li>
</ul>

<br>

<h2 id="#technologies">Technologies and Principles</h2>
  <ul>
    <li>Java, Spring Boot, Spring MVC, Spring Security, Spring WebFlux, Spring Data JPA, Lombok, JUnit, Mockito, HTML, CSS, JavaScript, Hibernate, PostgreSQL, WebSockets, SSE, Swagger, JWT Authentication, CI/CD with TeamCity</li>
    <li>DRY, KISS, YAGNI, SOLID, OOP, REST API design</li>
    <li>Service layer tests have over 80% code coverage</li>
    <li>Multilayered architecture</li>
  </ul>
  <br>

  <h2 id="#main-requirements">Main Functionalities</h2>
  <h3>Real-time Chat Communication</h3>
   <ul>
     <li>Users can enter a chat and start sending messages. Everything is received and sent real-time through the usage of the WebSockets protocol. It was considered a best option since it opens a full duplex communcation between the client and the server with minimal overhead.</li>
   </ul>
<br>

  <h3>Different chat rooms</h3>
   <ul>
     <li>Users may enter different chat rooms. This is decided by the admins, who are responsible for adding them to the corresponding chat room.</li>
   </ul>
<br>

<h3>Notification mechanism</h3>
   <ul>
     <li>Users receive notifications from specific chats when they are on the User Panel or inside another chat room. By clicking them they can navigate to the concrete room. This functionality was implemented by using Server-Sent Events and Spring Web-Flux + RXJs</li>
   </ul>
<br>
   <br>
   <br>

<h2 id="#public-part">Public Part</h2>
 <ul>
      <li>Visible without authentication</li>
      <li>Users have access to the registration page, where they can create a new account</li>
      <li>Users have access to the login page</li>
    </ul>
    <br>
    
<h2 id="#private-part">Private Part</h2>
<ul>
  <li>Regular users are able to see the user panel with all chats, in which they are added</li>
  <li>Each member is able to open a specific chat if it is visible to him</li>
</ul>
<br>

<h2 id="#administration-part">Administration Part</h2>
<ul>
  <li>Admins are able to see the admin panel with all the existing chats in the system</li>
  <li>They have the option to add/remove chats from the panel and access a specific chat room</li>
  <li>Upon entering a chat room, the admin can manage user access by adding/removing users from them</li>
  <li>Admins cannot send messages in the chat</li>
</ul>

<br>

<h2 id="#rest-api">REST API</h2>
  <ul>
    <li>Link to the documentation in Swagger -> http://localhost:8080/swagger-ui.html (access after you have successfully configured the project)</li>
  </ul>

<br>

  <h2 id="#database">Database</h2>
  <br>

![image](https://github.com/KristiyanMitrofanov/RealTimeChatApp-BE/blob/main/database-diagram.png)
