# Marketplace App — Ghid pentru începători

Aplicație JavaFX cu bază de date MySQL pentru gestionarea unui marketplace (vânzători, produse, comenzi).

---

## Ce ai nevoie înainte să începi

Instalează **în ordine** programele de mai jos. Dacă le ai deja, sari peste pasul respectiv.

| Program | Versiune | Link descărcare |
|---|---|---|
| **JDK (Java)** | 21 sau mai nou | https://www.oracle.com/java/technologies/downloads/ |
| **IntelliJ IDEA** | Community (gratuit) | https://www.jetbrains.com/idea/download/ |
| **MySQL Community Server** | 8.0 | https://dev.mysql.com/downloads/mysql/ |
| **Git** | orice versiune | https://git-scm.com/downloads |

> **Cum știi dacă Java e instalat?** Deschide CMD și scrie `java -version`. Dacă apare un număr de versiune, e instalat.

---

## Pasul 1 — Clonează proiectul

1. Deschide **Command Prompt** (CMD) sau **PowerShell**
2. Navighează unde vrei să salvezi proiectul, de exemplu:
   ```
   cd C:\Users\NumeleTau\Documents
   ```
3. Clonează repo-ul:
   ```
   git clone https://github.com/Ston1cc/parctica-marketplace.git
   ```
4. Acum ai un folder `parctica-marketplace` cu tot codul.

---

## Pasul 2 — Deschide proiectul în IntelliJ

1. Pornește **IntelliJ IDEA**
2. Pe ecranul de start, apasă **Open**
3. Navighează la folderul `parctica-marketplace` și apasă **OK**
4. IntelliJ va detecta fișierul `pom.xml` și va întreba:
   > *"Maven build scripts found. Would you like to load it?"*

   Apasă **Load** (sau **Trust Project** dacă apare mai întâi)

5. Așteaptă ~1-2 minute — IntelliJ descarcă automat JavaFX și MySQL Connector din internet (bara de progres jos-dreapta)

> ✅ Când bara de jos dispare, toate librăriile sunt descărcate.

---

## Pasul 3 — Configurează versiunea de Java

1. Din meniu: **File → Project Structure** (sau `Ctrl + Alt + Shift + S`)
2. La **Project SDK**: selectează **JDK 21** (sau 25 dacă ai instalat aia)
   - Dacă nu apare în listă: apasă **Add SDK → JDK** și navighează la folderul unde e instalat Java
     - Windows: de obicei `C:\Program Files\Java\jdk-21` sau `C:\Program Files\Java\jdk-25`
3. La **Language Level**: alege **21**
4. Apasă **Apply** → **OK**

---

## Pasul 4 — Pornește MySQL

### 4a. Pornește serviciul MySQL

**Metoda 1 — prin Services (recomandat):**
1. Apasă `Win + R`, scrie `services.msc`, apasă Enter
2. Caută **MySQL80** în listă
3. Click dreapta → **Start**

**Metoda 2 — CMD ca Administrator:**
```
net start MySQL80
```

### 4b. Creează baza de date și tabelele

1. Deschide **CMD** în folderul proiectului:
   ```
   cd C:\Users\NumeleTau\Documents\parctica-marketplace
   ```

2. Rulează schema (înlocuiește `PAROLA_TA` cu parola ta de root MySQL):
   ```
   "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < schema.sql
   ```
   Când cere parola, introdu parola ta MySQL și apasă Enter.

3. Rulează datele de test (50 înregistrări per tabel):
   ```
   "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < seed.sql
   ```

> **Nu știi parola de root?** A fost setată când ai instalat MySQL. Dacă ai uitat-o, vezi secțiunea [Probleme frecvente](#probleme-frecvente) de jos.

> **Ești pe PowerShell?** Comanda cu `<` nu funcționează în PowerShell. Folosește în schimb:
> ```powershell
> Get-Content schema.sql | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p
> Get-Content seed.sql   | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p
> ```

### 4c. Verifică parola din cod

Deschide fișierul `dao/DatabaseConnection.java` la linia 15:

```java
private static final String PASSWORD = "dani";
```

Dacă parola ta de MySQL **nu este** `dani`, schimb-o cu parola ta reală:

```java
private static final String PASSWORD = "parola_ta_aici";
```

---

## Pasul 5 — Rulează aplicația

1. În IntelliJ, în panoul din stânga (**Project**), deschide:
   ```
   ui → MainApp.java
   ```
2. Click dreapta pe fișier → **Run 'MainApp.main()'**

   SAU apasă butonul verde ▶ din toolbar (sus-dreapta).

3. Aplicația se va deschide într-o fereastră separată.

> ✅ Dacă vezi fereastra cu sidebar-ul negru și Dashboard-ul — totul funcționează!

---

## Structura proiectului

```
parctica-marketplace/
│
├── dao/                  ← Comunicarea cu baza de date (CRUD)
│   ├── DatabaseConnection.java
│   ├── VanzatorDAO.java
│   ├── ProdusDAO.java
│   └── ComandaDAO.java
│
├── model/                ← Clasele de date (Vanzator, Produs, Comanda)
│   ├── Vanzator.java
│   ├── Produs.java
│   ├── ProdusFizic.java
│   ├── ProduDigital.java
│   ├── Comanda.java
│   └── enums/StatusComanda.java
│
├── ui/                   ← Interfața grafică (JavaFX)
│   ├── MainApp.java      ← Punctul de intrare în aplicație
│   ├── VanzatoriController.java
│   ├── ProduseController.java
│   ├── ComenziController.java
│   └── RapoarteController.java
│
├── util/                 ← Servicii (logică business + export)
│   ├── VanzatorService.java
│   ├── ProdusService.java
│   ├── RaportService.java
│   └── ExportService.java
│
├── pom.xml               ← Configurare Maven (descarcă librăriile automat)
├── schema.sql            ← Creează baza de date și tabelele
└── seed.sql              ← Adaugă 50 înregistrări de test per tabel
```

---

## Probleme frecvente

### ❌ "Cannot connect to MySQL" la pornirea aplicației
- MySQL nu e pornit → urmează **Pasul 4a**
- Parola greșită → verifică **Pasul 4c**
- Baza de date nu există → rulează din nou `schema.sql` (**Pasul 4b**)

### ❌ "JavaFX runtime components are missing"
- Proiectul nu s-a deschis ca Maven → închide IntelliJ, șterge folderul `.idea/` din proiect și redeschide
- Sau: **View → Tool Windows → Maven** → apasă butonul 🔄 (Reload All Maven Projects)

### ❌ IntelliJ nu găsește `pom.xml`
- Asigură-te că ai deschis **folderul** `parctica-marketplace`, nu un fișier din el

### ❌ Am uitat parola de root MySQL
Deschide CMD ca Administrator și rulează:
```
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqladmin.exe" -u root password "dani"
```
Apoi setează `PASSWORD = "dani"` în `DatabaseConnection.java`.

### ❌ Eroarea "Port 3306 already in use"
Un alt proces folosește portul MySQL. Repornește serviciul:
```
net stop MySQL80
net start MySQL80
```

---

## Tehnologii folosite

- **Java 21** — limbajul de programare
- **JavaFX 21** — interfața grafică
- **MySQL 8.0** — baza de date
- **MySQL Connector/J 8.3** — driver JDBC pentru conectarea Java ↔ MySQL
- **Maven** — gestionarea automată a dependențelor
