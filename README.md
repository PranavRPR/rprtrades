<!-- ============================================= -->
<!--           RPR TRADES - PREMIUM README          -->
<!-- ============================================= -->

<div align="center">

<!-- Logo / Banner -->
<img src="https://capsule-render.vercel.app/api?type=waving&color=0:0d6efd,100:6610f2&height=200&section=header&text=RPR%20TRADES&fontSize=70&fontColor=ffffff&animation=fadeIn&fontAlignY=38&desc=Import%20%26%20Export%20Marketplace%20%26%20Trade%20Management%20Platform&descAlignY=58&descSize=18" width="100%" />

<!-- Badges -->
<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-18-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white" alt="Bootstrap" />
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Status-Active_Development-success?style=flat-square" />
  <img src="https://img.shields.io/badge/Version-1.0.0-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" />
  <img src="https://img.shields.io/badge/PRs-Welcome-brightgreen?style=flat-square" />
  <img src="https://img.shields.io/badge/Made_with-❤️_%26_☕-red?style=flat-square" />
</p>

<!-- Short Description -->
<h3>🌍 A Full-Stack Import/Export Marketplace for Modern Global Trade</h3>

<p align="center">
  <i>Connecting importers and exporters through a streamlined, secure, and role-based business workflow.</i>
</p>

<!-- Quick Links -->
<p align="center">
  <a href="#-features"><b>Features</b></a> •
  <a href="#-tech-stack"><b>Tech Stack</b></a> •
  <a href="#-architecture"><b>Architecture</b></a> •
  <a href="#-getting-started"><b>Getting Started</b></a> •
  <a href="#-api-reference"><b>API</b></a> •
  <a href="#️-screenshots"><b>Screenshots</b></a> •
  <a href="#️-roadmap"><b>Roadmap</b></a>
</p>

</div>

---

## 📖 Table of Contents

<details open>
<summary><b>Click to expand/collapse</b></summary>

- [📌 About the Project](#-about-the-project)
- [✨ Features](#-features)
  - [👤 Importer](#-importer)
  - [🏭 Exporter](#-exporter)
  - [🛡️ Administrator](#️-administrator)
  - [🎨 UI & Security](#-ui--security)
- [🔄 Business Workflow](#-business-workflow)
- [🛠️ Tech Stack](#️-tech-stack)
- [🏗️ Architecture](#️-architecture)
- [📂 Project Structure](#-project-structure)
- [🚀 Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Running the App](#running-the-app)
- [🔐 Administrator Account](#-administrator-account)
- [🛡️ Role-Based Authorization](#️-role-based-authorization)
- [🔗 API Reference](#-api-reference)
- [🖼️ Screenshots](#️-screenshots)
- [🧪 Testing Checklist](#-testing-checklist)
- [🗺️ Roadmap](#️-roadmap)
- [🤝 Contributing](#-contributing)
- [🔒 Security Notes](#-security-notes)
- [📜 License](#-license)
- [👨‍💻 Author](#-author)
- [🙏 Acknowledgements](#-acknowledgements)

</details>

---

## 📌 About the Project

<div align="center">
  <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&size=22&duration=3000&pause=1000&color=0D6EFD&center=true&vCenter=true&width=800&lines=Welcome+to+RPR+Trades!;A+Modern+Import%2FExport+Marketplace;Built+with+Spring+Boot+%2B+PostgreSQL;Secure+%7C+Scalable+%7C+Role-Based" />
</div>

**RPR Trades** is a **full-stack web application** designed to digitize and streamline the import/export business workflow. It connects **importers** and **exporters** on a single platform, enabling seamless product discovery, order placement, payments, invoicing, and shipment tracking.

Built with **Spring Boot**, **PostgreSQL**, and **Thymeleaf**, the platform uses **JWT-based authentication** and **role-based authorization** to ensure that each user — Importer, Exporter, or Admin — only accesses the operations permitted for their role.

> 💡 **Why RPR Trades?**
> Traditional import/export workflows are fragmented across emails, spreadsheets, and paper documents. RPR Trades brings everything together into one elegant, secure, and auditable digital platform.

---

## ✨ Features

### 👤 Importer

<table>
<tr>
<td width="50%">

- ✅ Secure registration & login
- 🔍 Browse & search approved products
- 🛒 Place orders with exporters
- 📋 View personal order history
- 💳 Make payments for approved orders
- 🧾 View invoices & payment status
- 🚚 Track shipments in real time

</td>
<td width="50%">

<img src="https://user-images.githubusercontent.com/74038190/212257468-1e9a91f1-b626-4baa-b15d-5c385dfa7ed2.gif" width="100%" />

</td>
</tr>
</table>

---

### 🏭 Exporter

<table>
<tr>
<td width="50%">

- ✅ Secure registration & login
- 📦 Add, update & manage products
- 📤 Submit products for admin approval
- 📥 View & manage incoming orders
- ✔️ Approve or reject orders
- 🧾 Generate invoices
- 🚚 Create & update shipments
- 📊 Monitor products & business activity

</td>
<td width="50%">

<img src="https://user-images.githubusercontent.com/74038190/212257454-16e3712e-945a-4ca2-b238-408ad0bf87e6.gif" width="100%" />

</td>
</tr>
</table>

---

### 🛡️ Administrator

<table>
<tr>
<td width="50%">

- 🔐 Predefined admin account (no public registration)
- 👥 Manage users & access control
- 📦 Review & approve products
- 🛒 Monitor all orders & payments
- 🚚 Oversee shipments
- 📊 Platform-wide reports & analytics
- 📁 Document management

</td>
<td width="50%">

<img src="https://user-images.githubusercontent.com/74038190/212257472-08e52665-c503-4bd9-aa20-f5a4c92d1e9b.gif" width="100%" />

</td>
</tr>
</table>

---

### 🎨 UI & Security

<div align="center">

| Feature | Description |
|:-------:|:------------|
| 🎭 **Role-Based Dashboards** | Separate UI for Importer, Exporter, and Admin |
| 🔐 **JWT Authentication** | Stateless, secure token-based auth |
| 🗂️ **Per-Tab Sessions** | Independent sessions using `sessionStorage` |
| 🔔 **Toast Notifications** | Smooth in-app feedback (no annoying alerts) |
| 📱 **Responsive Design** | Works beautifully on desktop, tablet, and mobile |
| 🛡️ **Backend Authorization** | Every protected endpoint independently verified |

</div>

---

## 🔄 Business Workflow

```mermaid
flowchart TD
    A[🏭 Exporter Registers / Logs In] --> B[📦 Add Product]
    B --> C{🛡️ Admin Approval}
    C -- Approved --> D[✅ Product Available]
    C -- Rejected --> B
    D --> E[👤 Importer Places Order]
    E --> F[✔️ Exporter Approves Order]
    F --> G[🧾 Invoice Generated]
    G --> H[💳 Importer Makes Payment]
    H --> I[🚚 Exporter Creates Shipment]
    I --> J[📍 Shipment Status Updates]
    J --> K[🎉 Delivery Completed]

    style A fill:#0d6efd,color:#fff
    style K fill:#198754,color:#fff
    style C fill:#ffc107,color:#000
    style G fill:#6610f2,color:#fff
