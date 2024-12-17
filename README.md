# Server GUI

----

Compilar ejemplo en C:
```
J:\ServerGUI>mingw32-make c
gcc -O3 -Isrc/CSGUI/include -c src/CSGUI/src/csgui.c -o out/csgui.o
gcc -O3 -Isrc/CSGUI/include src/CSGUI/code.c out/csgui.o -o code.exe
mingw32-make code.exe
mingw32-make[1]: Entering directory 'J:/ServerGUI'
mingw32-make[1]: 'code.exe' is up to date.
mingw32-make[1]: Leaving directory 'J:/ServerGUI'

```


----