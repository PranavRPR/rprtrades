<!-- ============================================= -->
<!--           RPR TRADES - PREMIUM README          -->
<!-- ============================================= -->

<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:0d6efd,100:6610f2&height=200&section=header&text=RPR%20TRADES&fontSize=70&fontColor=ffffff&animation=fadeIn&fontAlignY=38&desc=Import%20%26%20Export%20Marketplace%20%26%20Trade%20Management%20Platform&descAlignY=58&descSize=18" width="100%" />

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
</p>

<h3>🌍 A Full-Stack Import/Export Marketplace for Modern Global Trade</h3>

<p><i>Connecting importers and exporters through a streamlined, secure, and role-based business workflow.</i></p>

</div>

---

## 📖 Table of Contents

- [About the Project](#-about-the-project)
- [Features](#-features)
- [Business Workflow](#-business-workflow)
- [Tech Stack](#️-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Administrator Account](#-administrator-account)
- [Role-Based Authorization](#️-role-based-authorization)
- [API Reference](#-api-reference)
- [Screenshots](#️-screenshots)
- [Testing Checklist](#-testing-checklist)
- [Roadmap](#️-roadmap)
- [Contributing](#-contributing)
- [Security Notes](#-security-notes)
- [License](#-license)
- [Author](#-author)

---

## 📌 About the Project

**RPR Trades** is a full-stack web application designed to digitize and streamline the import/export business workflow. It connects **importers** and **exporters** on a single platform, enabling seamless product discovery, order placement, payments, invoicing, and shipment tracking.

Built with **Spring Boot**, **PostgreSQL**, and **Thymeleaf**, the platform uses **JWT-based authentication** and **role-based authorization** to ensure that each user — Importer, Exporter, or Admin — only accesses the operations permitted for their role.

> 💡 **Why RPR Trades?**
> Traditional import/export workflows are fragmented across emails, spreadsheets, and paper documents. RPR Trades brings everything together into one elegant, secure, and auditable digital platform.

---

## ✨ Features

### 👤 Importer

- ✅ Secure registration and login
- 🔍 Browse and search approved products
- 🛒 Place orders with exporters
- 📋 View personal order history
- 💳 Make payments for approved orders
- 🧾 View invoices and payment status
- 🚚 Track shipments in real time

### 🏭 Exporter

- ✅ Secure registration and login
- 📦 Add, update, and manage products
- 📤 Submit products for admin approval
- 📥 View and manage incoming orders
- ✔️ Approve or reject orders
- 🧾 Generate invoices
- 🚚 Create and update shipments
- 📊 Monitor products and business activity

### 🛡️ Administrator

- 🔐 Predefined admin account (no public registration)
- 👥 Manage users and access control
- 📦 Review and approve products
- 🛒 Monitor all orders and payments
- 🚚 Oversee shipments
- 📊 Platform-wide reports and analytics
- 📁 Document management

### 🎨 UI and Security

| Feature | Description |
|:-------:|:------------|
| 🎭 **Role-Based Dashboards** | Separate UI for Importer, Exporter, and Admin |
| 🔐 **JWT Authentication** | Stateless, secure token-based auth |
| 🗂️ **Per-Tab Sessions** | Independent sessions using `sessionStorage` |
| 🔔 **Toast Notifications** | Smooth in-app feedback (no annoying alerts) |
| 📱 **Responsive Design** | Works beautifully on desktop, tablet, and mobile |
| 🛡️ **Backend Authorization** | Every protected endpoint independently verified |

---

## 🔄 Business Workflow

```text
    🏭 Exporter Registers / Logs In
                  ↓
            📦 Add Product
                  ↓
         🛡️ Admin Approval
                  ↓
          ✅ Product Available
                  ↓
       👤 Importer Places Order
                  ↓
      ✔️ Exporter Approves Order
                  ↓
         🧾 Invoice Generated
                  ↓
       💳 Importer Makes Payment
                  ↓
      🚚 Exporter Creates Shipment
                  ↓
      📍 Shipment Status Updates
                  ↓
         🎉 Delivery Completed
