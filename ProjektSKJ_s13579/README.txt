You need to add a graphviz-2.38 (http://www.graphviz.org/Download_windows.php) to root of project.

Co zostało zrealizowane:
	1) parsing pliku konfiguracyjnego (podany jak w zadaniu) i tworzenie Klienta oraz lokalnego drzewa do .png
	2) komunikacja (TCP) z Klientem przez Konsole.
	3) zrealizowane są komendy ADD | REMOVE | MODIFY (wpisywanie komend zrobione bez " " , => ADD agent 1 text agent 2 text )
	4) wizualizacja wiedzy klienta w przeglądarce | komenda - "openGraf" (ignore case) (po zmianie wiedzy, uruchomienie tej komendy ponownie generuje drzewo Klienta z aktualnymi danymi)
	5) komenda "closeClien"(ic) dla zamykania socketu.
Czego się nie udało:
	1) komunikacja między Klientami.
