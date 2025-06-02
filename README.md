# 🛒 Inventory Manager

Aplicație completă pentru gestionarea stocurilor, angajaților și programului de lucru într-un magazin sau rețea de magazine.

## 🔧 Funcționalități principale

- 📦 **Gestiune produse**: adăugare, ștergere, căutare după cod de bare
- 🏪 **Gestiune magazine**: fiecare companie poate avea mai multe locații
- 👥 **Gestiune angajați**: alocare angajați pe magazine, înregistrare prin Keycloak
- 🕒 **Pontaj și ture**: pontaj zilnic, planificare tura, schimb de ture
- 🧾 **Jurnal stocuri**: istoric complet pentru intrări/ieșiri de produse
- ♻️ **Suport SGR**: identificare produse returnabile cu valoare de garanție
- 🔒 **Autentificare securizată**: cu suport pentru login Google (OAuth2 via Keycloak)

## 🧱 Arhitectură

- **Frontend**: React + Vite (UI custom)
- **Backend**: Spring Boot + PostgreSQL
- **SSO/Auth**: Keycloak (cu suport Social Login)
- **Database migration**: Liquibase (SQL format)
- **Containerizare**: Docker

## 🗄️ Structura bazei de date

Tabelele principale:
- `users`, `company`, `store`, `user_store`
- `product`, `product_stock`, `stock_entry_log`
- `shift_schedule`, `shift_swap_request`
- `time_off_request`, `attendance_log`
- `notification`

## ⚙️ Configurare locală

1. Clonează proiectul:
   ```bash
   git clone https://github.com/username/inventory-manager.git
   cd inventory-manager
    ```