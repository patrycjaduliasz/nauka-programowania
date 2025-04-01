/* procedura dodajaca pytanie */

create
    definer = root@localhost procedure dodaj_pytanie(IN p_id_kategorii int, IN p_tresc_pytania text,
                                                     IN p_odpowiedz1 varchar(255), IN p_odpowiedz2 varchar(255),
                                                     IN p_odpowiedz3 varchar(255), IN p_odpowiedz4 varchar(255),
                                                     IN p_poprawna_odpowiedz int)
BEGIN
    INSERT INTO pytania
    (id_kategorii, tresc_pytania, odpowiedz1, odpowiedz2, odpowiedz3, odpowiedz4, poprawna_odpowiedz)
    VALUES
        (p_id_kategorii, p_tresc_pytania, p_odpowiedz1, p_odpowiedz2, p_odpowiedz3, p_odpowiedz4, p_poprawna_odpowiedz);
END;

/*pobieranie historii quizu uzytkownika (quiz zamkniety) */
create
    definer = root@localhost procedure pobierz_historie_quizu_uzytkownika(IN p_id_uzytkownika int, IN p_limit int)
BEGIN
    SELECT q.*, k.nazwa AS nazwa_kategorii
    FROM quizy q
             JOIN kategorie k ON q.id_kategorii = k.id
    WHERE q.id_uzytkownika = p_id_uzytkownika
    ORDER BY q.data_proby DESC
    LIMIT p_limit;
END;

/*pobieranie kategorii, zwraca liste kategorii posortowane po ID */
create
    definer = root@localhost procedure pobierz_kategorie()
BEGIN
    SELECT id, nazwa
    FROM kategorie
    ORDER BY id;
END;

/* pobieranie losowo pytan do quizu zamknietego */
create
    definer = root@localhost procedure pobierz_pytania_z_kategorii(IN p_id_kategorii int, IN p_limit int)
BEGIN
    SELECT id, id_kategorii, tresc_pytania, odpowiedz1, odpowiedz2, odpowiedz3, odpowiedz4, poprawna_odpowiedz
    FROM pytania
    WHERE id_kategorii = p_id_kategorii
    ORDER BY RAND() /* sortowaniewynikow w losowej kolejnosci */
    LIMIT p_limit;
END;

/* pobieranie pytan do wpisywania (Quiz otwarty) */
create
    definer = root@localhost procedure PobierzPytaniaDoWpisywaniaSpecjalne(IN p_id_kategorii int, IN p_liczba_pytan int)
BEGIN
    SELECT
        id,
        id_kategorii,
        tresc_pytania,
        poprawna_odpowiedz,
        sciezka_zdjecia
    FROM
        pytania_do_wpisywania
    WHERE
        (p_id_kategorii = 0 OR id_kategorii = p_id_kategorii)
    ORDER BY RAND()
    LIMIT p_liczba_pytan;
END;

/* rejestracja uzytkownika */
create
    definer = root@localhost procedure rejestracja_uzytkownika(IN p_nazwa_uzytkownika varchar(50),
                                                               IN p_haslo varchar(255), IN p_email varchar(100))
BEGIN
    INSERT INTO uzytkownicy (nazwa_uzytkownika, haslo, email)
    VALUES (p_nazwa_uzytkownika, p_haslo, p_email);
END;

/* weryfikacja uzytkownika */
create
    definer = root@localhost procedure weryfikacjaUzytkownika(IN p_nazwa_uzytkownika varchar(50), IN p_haslo varchar(255))
BEGIN
    SELECT * FROM uzytkownicy
    WHERE nazwa_uzytkownika = p_nazwa_uzytkownika AND haslo = p_haslo;
END;

/* zapisz probe quizu zamknietego */
create
    definer = root@localhost procedure zapisz_probe_quizu(IN p_id_uzytkownika int, IN p_id_kategorii int,
                                                          IN p_wynik int, IN p_liczba_pytan int,
                                                          IN p_data_proby timestamp)
BEGIN
    INSERT INTO quizy (id_uzytkownika, id_kategorii, wynik, liczba_pytan, data_proby)
    VALUES (p_id_uzytkownika, p_id_kategorii, p_wynik, p_liczba_pytan, p_data_proby);
END;

/* zapisz sesje wpisywania (Quiz otwarty) */
create
    definer = root@localhost procedure zapisz_sesje_wpisywania(IN p_id_uzytkownika int, IN p_id_kategorii int,
                                                               IN p_wynik int, IN p_liczba_pytan int,
                                                               IN p_data_sesji datetime)
BEGIN
    INSERT INTO sesje_wpisywania (id_uzytkownika, id_kategorii, wynik, liczba_pytan, data_sesji)
    VALUES (p_id_uzytkownika, p_id_kategorii, p_wynik, p_liczba_pytan, p_data_sesji);
END;


