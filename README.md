# Zpráva ze semestrálky je na [wiki](https://gitlab.fel.cvut.cz/belkapre/ear-semestralka/-/wikis/home)
### Zadání


#### School Information System

The system is a simple version of KOS, maintaining courses, students, teachers, rooms. Teachers can create a course and schedule them into rooms that restrict the capacity of the course. Students can enroll into the course unless the course is no more capacity left. Students can list courses they are enrolled into, teachers can list courses that they teach.


### Komu bude systém určen

Systém bude určen pro použití na nějaké české vysoké technické škole. Skupiny uživatel, kteří s nim budou pracovat rozdělujeme do následujících rolí.



* studenti
* učitelé
* studijní referentky
* katederní správci
* IT administrátor systému


### Seznam hlavních funkcí

Popis funkcionality aplikace jsme se rozhodli rozdělit podle různých skupin uživatel, kteří se systémem budou pracovat. CRUD v závorce za funkcionalitou znamená, že když se uživatel může např. zapsat na paralelku, tak potom se může i odhlásit, přihlásit na jinou paralelku a zobrazit všechny své zapsané paralelky.



* všichni
    * Přihlašování pomocí SSO
    * Změna hesla, změna osobních údajů a podobné operace… (CRUD)
* studenti
    * Zapsat se na kurz (CRUD)
    * Přihlásit se na učební paralelku kurzu(CRUD)
    * Zobrazit své studijní výsledky
    * Zapsat se na zkoušku (CRUD)
    * Rezervace místností pro samostudium (CRUD)
* učitelé
    * Zadávat studijní výsledky studentů svých kurzů (CRUD)
    * Vypsat termín zkoušky (CRUD)
    * Vytvářet učební paralelky kurzů (CRUD)
* katederní správci
    * Přidávat nové předměty (CRUD)
    * Vytvářet skupiny předmětů (CRUD)
* studijní referentky
    * Zobrazit studijní výsledky všech studentů
    * Úprava zápisů předmětů (CRUD)
* IT administrátor systému
    * Přidávat uživatele (CRUD)
    * Rozdělovat role (CRUD)
    * Spouštět CRON úlohy
    * \+ operace všech ostatních rolí
