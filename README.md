<h1 align="center">✨ Lumio</h1>
<p align="center"><i>Learn Brighter. Go Further</i></p>

## 🚀 About Lumio

**Lumio** — *Learn Brighter. Go Further.*

Lumio is an ambitious commercial platform designed to redefine the way people learn and share knowledge online. The goal is to build a comprehensive digital marketplace where educators and creators can publish, manage, and monetize high-quality educational content across a wide range of subjects.

Inspired by leading platforms like Udemy and Alura, Lumio aims to go beyond the standard learning experience by integrating modern, interactive features such as real-time chat and live streaming for immersive, instructor-led sessions.

### 💡 Vision

To create a powerful, scalable, and engaging learning ecosystem that empowers both students and educators — making knowledge more accessible, interactive, and valuable.

### ⚙️ Tech Stack

The backend is being built with a robust and modern architecture, including:

- **Java 25**
- **Quarkus** for high-performance
- **Keycloak** for authentication and identity management
- **AWS services** for scalability and cloud infrastructure
- **Messaging systems** for asynchronous communication and event-driven design

### 🔮 Planned Features

- Course marketplace (buy/sell educational content)
- Video-based lessons and structured learning paths
- Real-time chat between students and instructors
- Live streaming for interactive classes
- User authentication and role-based access control

---

Lumio is more than just a platform — it's a step toward a smarter, more connected future of learning.

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## Setup

1. Clone the repository
2. Build the project with Maven
3. Start images of container: `docker compose up`
4. Run Docker container: `mvn quarkus:dev`

--- 

## Generate JaCoCo Report

3. Run tests and generate coverage report: `mvn clean test`
4. Open the report in:: `target/site/jacoco/index.html`

--- 