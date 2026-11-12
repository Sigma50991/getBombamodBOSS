# GET - mod do Minecrafta (Fabric, 1.21.11)

Oryginalny, wizualny klient-mod. Napisany od zera w oparciu wylacznie o
Fabric API (bez mixinow tam gdzie dalo sie tego uniknac), zeby bylo jak
najmniej ryzyka niezgodnosci z mapowaniami przy kompilacji.

## Funkcje
- **ESP** - obrysy (boxy) wokol graczy/mobow, widoczne przez sciany
- **Tracery** - linie od kamery do graczy
- **Target HUD** - panel z nickiem/HP/dystansem najblizszego gracza
- **Watermark** - napis "GET" w rogu ekranu
- **Fullbright** (uproszczony, przez podbicie opcji gamma)
- **Zoom** - trzymaj `C`, zmniejsza FOV (mixin do `GameRenderer#getFov`)
- **Autosprint**
- **Menu** - klawisz `J`, wlacza/wylacza kazda funkcje osobno

## Czego NIE ma (celowo)
W stosunku do oryginalnego jara, z ktorego to bylo inspirowane, **usunieto
calkowicie**:
- wyszukiwanie/sprawdzanie IP graczy (nie ma zadnych zapytan do zewnetrznych
  API typu "ip finder")
- funkcje "atakowania" / pingowania celu i logowania tego na webhooki
- system licencji wysylajacy HWID do zewnetrznego serwera
- wszelka komunikacje siecowa poza samym Minecraftem - ten mod nic nigdzie
  nie wysyla

Jesli kiedys zechcesz dodac wlasny, lokalny system licencji (np. prosty
klucz sprawdzany o Twoj wlasny serwer), trzeba to dopisac swiadomie i
oddzielnie - tu go nie ma.

## Bindy
- `J` - otwiera menu GET (wlacz/wylacz kazda funkcje osobno)
- `C` (trzymaj) - zoom

## Budowanie przez GitHub Actions (polecane, bez lokalnego setupu)
W repo jest juz `.github/workflows/build.yml`:

1. Zaloz nowe repo na GitHub i wrzuc do niego cala zawartosc tego folderu
   (np. `git init`, `git add .`, `git commit -m "init"`, dodaj remote,
   `git push`).
2. Wejdz w zakladke **Actions** w repo - workflow "Build GET mod" odpali
   sie automatycznie po pushu na branch `main` (mozesz tez go odpalic
   recznie przyciskiem "Run workflow").
3. Po zakonczeniu (zielony ptaszek) wejdz w ten konkretny run -> na dole
   strony sekcja **Artifacts** -> pobierz `get-mod-jar` (to zip z gotowym
   `.jar` w srodku).
4. Wrzuc `.jar` do folderu `mods` w instalacji z Fabric Loaderem (plus
   wymagany mod `Fabric API` w tej samej wersji co w `gradle.properties`).

Ten workflow sam instaluje Gradle (`gradle/actions/setup-gradle`) i JDK 21,
wiec nie musisz miec nic zainstalowanego lokalnie - caly build dzieje sie
na serwerach GitHuba.

## Budowanie lokalnie (alternatywa)
Jesli wolisz budowac u siebie, potrzebujesz zainstalowanego Gradle (albo
wygenerowanego wrappera przez `gradle wrapper` - w tym archiwum go nie ma,
bo wymaga pobrania binarki `gradle-wrapper.jar`, a tutaj nie mam dostepu
do sieci):

1. Zainstaluj Gradle i JDK 21.
2. Sprawdz aktualne numery wersji dla 1.21.11 na https://fabricmc.net/develop/
   i podmien je w `gradle.properties` (`yarn_mappings`, `loader_version`,
   `fabric_version`) jesli sie zmienily.
3. `gradle build` w folderze projektu.
4. Gotowy `.jar` znajdziesz w `build/libs/`.

## Mapping-sensitive miejsce
`GameRendererMixin` celuje w metode liczaca FOV (`getFov`). Jesli build
zglosi blad przy tym mixinie, otworz zdekompilowany `GameRenderer` w swoim
IDE (po imporcie projektu Gradle) i popraw nazwe/sygnature metody na te,
ktora faktycznie wystepuje w Twoim buildzie mapowan - to jedyne miejsce w
projekcie, ktore moze wymagac recznej poprawki, bo nie mialem mozliwosci
skompilowania tego kodu w tym srodowisku (brak dostepu do sieci).
