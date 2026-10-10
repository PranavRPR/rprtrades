<!-- ============================================= -->
<!--        🚀 RPR TRADES - PREMIUM README         -->
<!-- ============================================= -->

<h1 align="center">🌍 RPR TRADES</h1>

<h3 align="center">Import & Export Marketplace • Trade Management Platform</h3>

<p align="center">
  <em>Connecting importers and exporters through a streamlined, secure, and role-based business workflow.</em>
</p>

<br>

<p align="center">
  <a href="#-features"><img src="https://img.shields.io/badge/✨_Features-0d6efd?style=for-the-badge" /></a>
  <a href="#-screenshots"><img src="https://img.shields.io/badge/📸_Screenshots-6610f2?style=for-the-badge" /></a>
  <a href="#-getting-started"><img src="https://img.shields.io/badge/🚀_Get_Started-198754?style=for-the-badge" /></a>
  <a href="#-api-reference"><img src="https://img.shields.io/badge/📡_API_Docs-dc3545?style=for-the-badge" /></a>
</p>

<br>

<!-- Tech Stack Badges -->
<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-18-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" />
  <img src="https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Secure-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Status-Active-success?style=flat-square" />
  <img src="https://img.shields.io/badge/Version-1.0.0-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" />
  <img src="https://img.shields.io/badge/PRs-Welcome-brightgreen?style=flat-square" />
  <img src="https://img.shields.io/badge/Made_with-❤️_&_☕-red?style=flat-square" />
</p>

---

## 📌 About The Project

**RPR Trades** is a full-stack web application designed to digitize and streamline the import/export business workflow. It connects **importers** and **exporters** on a single platform, enabling seamless product discovery, order placement, payments, invoicing, and shipment tracking.

Built with **Spring Boot**, **PostgreSQL**, and **Thymeleaf**, the platform uses **JWT-based authentication** and **role-based authorization** to ensure that each user — Importer, Exporter, or Admin — only accesses the operations permitted for their role.

<br>

<table>
<tr>
<td>

### 💡 Why RPR Trades?

Traditional import/export workflows are fragmented across emails, spreadsheets, and paper documents. 

**RPR Trades brings everything together** into one elegant, secure, and auditable digital platform.

</td>
<td>

### 🎯 Core Principles

- 🔐 **Security First** — JWT + role-based access
- 🎨 **Clean UX** — Bootstrap 5 responsive design
- ⚡ **Performance** — Spring Boot 3.5 optimized
- 📊 **Audit-Ready** — Complete activity tracking

</td>
</tr>
</table>

---

## 📖 Table of Contents

<div align="center">

| 🚀 Project | ✨ Features | 📡 Reference |
|:----------:|:----------:|:------------:|
| [About](#-about-the-project) | [Importer](#-importer) | [API Reference](#-api-reference) |
| [Screenshots](#️-screenshots) | [Exporter](#-exporter) | [Role Matrix](#️-role-based-authorization) |
| [Getting Started](#-getting-started) | [Admin](#️-administrator) | [Security](#-security-notes) |
| [Tech Stack](#️-tech-stack) | [Workflow](#-business-workflow) | [Contributing](#-contributing) |
| [Structure](#-project-structure) | [Roadmap](#️-roadmap) | [License](#-license) |

</div>

---

## ✨ Features

<table>
<tr>
<th width="33%">👤 Importer</th>
<th width="33%">🏭 Exporter</th>
<th width="34%">🛡️ Administrator</th>
</tr>
<tr>
<td valign="top">

- ✅ Secure registration & login
- 🔍 Browse & search products
- 🛒 Place orders
- 📋 Order history
- 💳 Make payments
- 🧾 View invoices
- 🚚 Track shipments

</td>
<td valign="top">

- ✅ Secure registration & login
- 📦 Add & manage products
- 📤 Submit for approval
- 📥 View incoming orders
- ✔️ Approve / reject orders
- 🧾 Generate invoices
- 🚚 Manage shipments

</td>
<td valign="top">

- 🔐 Predefined admin account
- 👥 Manage all users
- 📦 Approve products
- 🛒 Monitor orders
- 💳 Oversee payments
- 🚚 Track shipments
- 📊 Platform analytics

</td>
</tr>
</table>

### 🎨 UI & Security Highlights

<table>
<tr>
<td>

| 🎭 Feature | 📝 Description |
|:----------:|:---------------|
| Role-Based Dashboards | Separate UI per user role |
| JWT Authentication | Stateless secure tokens |
| Per-Tab Sessions | Independent tab logins |
| Toast Notifications | Smooth in-app feedback |

</td>
<td>

| 🎨 Feature | 📝 Description |
|:----------:|:---------------|
| Responsive Design | Desktop, tablet, mobile |
| Backend Authorization | Every endpoint verified |
| Password Encoding | BCrypt hashing |
| Exception Handling | Global error management |

</td>
</tr>
</table>

---

## 🔄 Business Workflow

<div align="center">

```text
╔══════════════════════════════════════════════════════════╗
║                   🏭 EXPORTER WORKFLOW                   ║
╚══════════════════════════════════════════════════════════╝

    ┌─────────────────────┐
    │  1. Register/Login  │
    └──────────┬──────────┘
               ▼
    ┌─────────────────────┐
    │  2. Add Product     │
    └──────────┬──────────┘
               ▼
    ┌─────────────────────┐
    │  3. Await Approval  │
    └──────────┬──────────┘
               ▼
╔══════════════════════════════════════════════════════════╗
║                    🛡️ ADMIN WORKFLOW                     ║
╚══════════════════════════════════════════════════════════╝
               ▼
    ┌─────────────────────┐
    │  4. Approve Product │
    └──────────┬──────────┘
               ▼
╔══════════════════════════════════════════════════════════╗
║                  👤 IMPORTER WORKFLOW                    ║
╚══════════════════════════════════════════════════════════╝
               ▼
    ┌─────────────────────┐
    │  5. Place Order     │
    └──────────┬──────────┘
               ▼
    ┌─────────────────────┐
    │  6. Make Payment    │
    └──────────┬──────────┘
               ▼
    ┌─────────────────────┐
    │  7. Track Shipment  │
    └──────────┬──────────┘
               ▼
    ┌─────────────────────┐
    │  8. 🎉 Delivered    │
    └─────────────────────┘
