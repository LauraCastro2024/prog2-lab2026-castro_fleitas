# Materiales de una biblioteca.

Trabajo de Programación 2 del CeRP del Suroeste, modalidad semipresencial.
Docente: Domingo Pérez. Año: 2026.

## Integrantes

- María Laura Castro Bazán.
- Camila Daniela Fleitas Pérez.

## Estado del proyecto

Partimos del prototipo docente de un gestor de registros en Java y Swing, organizado con MVC, fachada y persistencia en JSON.
Adaptamos el prototipo para gestionar el catálogo de materiales de la biblioteca de un liceo. Implementamos el registro, la selección y modificación, la eliminación con confirmación, la búsqueda con filtro por tipo, la limpieza del formulario y el guardado en JSON.
La [consigna del laboratorio](docs/letra.md)
detalla los requisitos.

## Problema y destinatarios

Algunas bibliotecas de liceo llevan el inventario en un cuaderno o en una
planilla electrónica. Cuando un estudiante pregunta por un libro, el funcionario que lo atiende tiene que revisar esos registros para saber cuántos
ejemplares hay y cuántos están prestados. Tampoco le resulta fácil saber si un material está deteriorado o si solo se puede consultar en sala.
Esto ocasiona errores porque se presta un diccionario que no debería salir de la
biblioteca, o se da de baja un libro que todavía no devolvieron.
La aplicación está pensada para que la persona encargada de la biblioteca lleve
el registro de los materiales y su estado, y pueda responder en el momento qué
está realmente disponible.

## Utilidad de la solución

El catálogo queda en un solo lugar y se conserva cuando se cierra la aplicación. Para cada material se ve cuántos ejemplares hay, cuántos están
prestados y cuántos quedan disponibles; este último número lo calcula el programa. De esta forma, cuando un estudiante pregunta por un libro, se lo busca por título o autor, o se filtra por tipo, y la respuesta aparece en la tabla.
La aplicación también evita los errores que describimos antes. No deja anotar más prestados que ejemplares, ni prestar un material deteriorado o de consulta en sala, ni eliminar uno que tiene ejemplares sin devolver. En las
observaciones se puede dejar anotado el estado de cada material, por ejemplo
"rotura en las esquinas".

## Adaptación del modelo y reglas

El modelo inicial contiene registros con identificador, nombre, categoría y estado activo.
En nuestro proyecto, cada registro es un [Material](src/main/java/modelo/Material.java)
del catálogo: un título con uno o más ejemplares físicos.
| Atributo de la base | Atributo en nuestro proyecto |
| --- | --- |
| identificador | `identificador`: lo asigna el sistema y no se repite. |
| nombre | `titulo`: título del material. |
| categoria | `tipo`: Libro, Revista, Diccionario, Enciclopedia, Mapa o Material audiovisual. |
| activo | `soloEnSala`: si el material es solo para consulta en sala o se puede llevar a domicilio. |
| (nuevo) | `autor`: autor o editorial. |
| (nuevo) | `ejemplaresTotales` y `ejemplaresPrestados`: cantidades de ejemplares. |
| (nuevo) | `estado`: estado de conservación, con el enum [EstadoConservacion](src/main/java/modelo/EstadoConservacion.java) (Bueno, Regular, Deteriorado). |
| (nuevo) | `observaciones`: texto libre. |
 
Las validaciones están en el método `validar()` de `Material`. Si una regla no
se cumple, se lanza una [ValidacionException](src/main/java/modelo/ValidacionException.java)
con el motivo; la vista muestra el mensaje y el formulario conserva los datos
para corregirlos.

| Validación requerida | Regla de nuestro proyecto | Estado |
| --- | --- | --- |
| Primera validación | **Campo obligatorio:** El título no puede ser nulo ni estar vacío (`titulo.trim().isEmpty()`). | Implementado. |
| Segunda validación | **Coherencia de ejemplares:** Debe haber al menos 1 ejemplar total en biblioteca, y los ejemplares prestados no pueden ser negativos ni superar la cantidad total de ejemplares. | Implementado. |
| Tercera validación, propia del problema | **Restricción de préstamos y bajas:** Un material `DETERIORADO` no se puede prestar a domicilio (debe ser "Solo en sala"). Si está marcado como "Solo en sala", no puede tener ejemplares prestados. Además, no se permite eliminar un material si tiene ejemplares pendientes de devolución. | Implementado. |

##También falta implementar el identificador único, la selección y modificación,
la eliminación con confirmación, la búsqueda, la limpieza del formulario y la
actualización del listado. La lectura y escritura JSON están disponibles como
infraestructura; falta integrar el guardado con las operaciones.

## Componentes gráficos

La base incluye JTextField, JComboBox, JCheckBox, JButton y JTable.
JLabel y los contenedores de apoyo no cuentan para alcanzar el mínimo solicitado.

Los tres tipos nuevos todavía no fueron elegidos ni implementados:

| Componente nuevo | Dato o acción real que permitirá gestionar | Estado |
| --- | --- | --- |
| Pendiente de elegir. | Pendiente de definir. | Sin implementar. |
| Pendiente de elegir. | Pendiente de definir. | Sin implementar. |
| Pendiente de elegir. | Pendiente de definir. | Sin implementar. |

## Organización de paquetes y recorrido de una operación

| Paquete o carpeta | Responsabilidad |
| --- | --- |
| [app](src/main/java/app) | Iniciar la aplicación y conectar los objetos. |
| [modelo](src/main/java/modelo) | Representar los registros y, al completar el proyecto, sus reglas. |
| [vista](src/main/java/vista) | Mostrar la interfaz Swing y los mensajes. |
| [controlador](src/main/java/controlador) | Recibir eventos y coordinar las acciones mediante la fachada. |
| [fachada](src/main/java/fachada) | Ofrecer operaciones y coordinar el modelo con la persistencia. |
| [persistencia](src/main/java/persistencia) | Leer y escribir JSON, sin depender de Swing. |
| [datos](datos) | Contener el archivo JSON; no es un paquete Java. |

El recorrido que ya funciona es la carga inicial:
[Aplicacion](src/main/java/app/Aplicacion.java) conecta los objetos;
el controlador solicita la carga a la fachada; la fachada utiliza la
persistencia para leer el archivo; y el controlador entrega los registros a la
vista para mostrarlos en la tabla. La vista no lee ni escribe archivos.

**Pendiente de documentar:** el recorrido completo de una operación de gestión
implementada por nuestro equipo.

## Instrucciones de ejecución

### Requisitos

- JDK 25 LTS o superior, con `JAVA_HOME` apuntando al JDK y su carpeta `bin`
  en el `PATH`, para disponer de los comandos `java` y `javac`.
- Windows, macOS o Linux con un entorno gráfico para mostrar Swing.
- Git, si se descarga el proyecto mediante clonación.
- Conexión a Internet en la primera ejecución para descargar Maven y las
  dependencias. La aplicación utiliza un archivo local, sin base de datos ni
  conexión a Internet durante su uso.

No es necesario instalar Maven ni agregarlo al `PATH`: el repositorio incluye
Maven Wrapper, que descarga y utiliza Maven 3.9.11 automáticamente.
La compilación genera clases para Java 25; no se pueden ejecutar con Java 17
ni Java 21. Maven Wrapper utiliza el JDK indicado por `JAVA_HOME`.

Después de instalar o cambiar el JDK, reiniciar la terminal y VS Code.
Comprobar que `java -version`, `javac -version` y `.\mvnw.cmd -version`
(Windows) o `sh mvnw -version` (macOS y Linux) indiquen Java 25 o superior.

### Descargar el proyecto

**Pendiente:** reemplazar `USUARIO` y `REPOSITORIO` por los datos del repositorio
del equipo antes de entregar.

```text
git clone https://github.com/USUARIO/REPOSITORIO.git
cd REPOSITORIO
```

El nombre del repositorio debe seguir el formato indicado en la consigna:
`prog2-lab2026-apellido1_apellido2`.

Ejecutar los siguientes comandos desde la raíz del proyecto, donde se
encuentra [pom.xml](pom.xml).

### Windows: PowerShell

```powershell
.\mvnw.cmd compile exec:java
```

Para compilar sin abrir la ventana:

```powershell
.\mvnw.cmd compile
```

Si aparece «mvn no se reconoce», utilizar `.\mvnw.cmd` como en los comandos
anteriores. El prefijo `.\` indica a PowerShell que ejecute el lanzador de la
carpeta actual.

### macOS y Linux: terminal

```sh
sh mvnw compile exec:java
```

Para compilar sin abrir la ventana:

```sh
sh mvnw compile
```

Al utilizar `sh mvnw` no es necesario cambiar los permisos del archivo.
En Linux se requiere una sesión de escritorio; una terminal de servidor sin
entorno gráfico no puede mostrar la ventana.

### VS Code y dependencias

Abrir la carpeta raíz con Extension Pack for Java instalado e importar el
proyecto Maven. También se puede ejecutar el método `main` de
[Aplicacion](src/main/java/app/Aplicacion.java), con la raíz del proyecto como
directorio de trabajo.
En `Java: Configure Java Runtime`, seleccionar un JDK 25 para este proyecto.
La configuración local de VS Code selecciona el JDK 25 instalado en
`%USERPROFILE%\.jdks\jdk-25.0.4.1+1` para las terminales de Windows y el
proyecto Java, sin modificar el `PATH` del sistema. Si se utiliza otra
instalación, ajustar las rutas en `.vscode/settings.json`.

La biblioteca de persistencia es Gson 2.13.2. Está declarada, junto con los
complementos de compilación y ejecución, en [pom.xml](pom.xml).

### Archivo de datos

El archivo de ejemplo es [datos/registros.json](datos/registros.json).
La ruta es relativa al directorio desde el cual se inicia la aplicación.

Si el archivo no existe, la aplicación inicia con una lista vacía. Si es
inválido o ilegible, muestra un error y mantiene la ventana abierta sin
reemplazarlo. La fachada bloquea el guardado cuando la carga no fue correcta.
Los errores de escritura se propagan al controlador; falta integrar el guardado
y sus mensajes con las operaciones del equipo.

**En el estado actual, ningún botón guarda datos y cerrar la ventana tampoco
guarda.**

## Pruebas realizadas y limitaciones conocidas

### Comprobaciones heredadas del prototipo

La base fue verificada en Windows con OpenJDK 17.0.17 y Maven 3.9.11.
Es compatible por diseño con macOS y Linux, pero su ejecución en esos sistemas
todavía no fue comprobada.

Tras actualizar a Java 25 LTS, se verificó en Windows con Eclipse Temurin
25.0.4.1 y Maven 3.9.11 la compilación limpia mediante `.\mvnw.cmd clean verify`.
También se comprobaron la carga de los tres registros de ejemplo, la lectura
sin archivo, la escritura y lectura JSON con tildes, la copia del listado,
el rechazo de JSON inválido y el bloqueo del guardado tras una carga fallida.
No hay pruebas automatizadas en el repositorio; estas comprobaciones se
realizaron con un programa temporal. La interfaz gráfica no se volvió a
verificar como parte de esta actualización.

| Acción | Resultado esperado | Resultado obtenido en la base |
|---|---|---|
| Registrar "La Tregua" con 5 ejemplares y 1 prestado. | Aparece en el listado con identificador 4 y 4 disponibles. | "Material registrado con el identificador 4". |
|Cambiar título de "La Tregua" “La Tregua (edición escolar)” | Conserva el identificador 4 y no aparece duplicada. | "Material 4 modificado" |
| Eliminar el Diccionario y responder No. | Se cancela y el material se conserva.| "Eliminación cancelada. El material se conserva".|
| Eliminar un material de prueba y responder Sí. | Se elimina solo ese material. | "Material eliminado". |
| Buscar "gregori" y filtrar por tipo Mapa. | Muestra solo los materiales que coinciden. | No aparece ya que es de tipo Libro |
| Limpiar el formulario. | Los campos quedan vacíos, listos para un material nuevo. | Los campos aparecen vacíos |
| Cerrar y volver a abrir la aplicación. | Se conservan los cambios guardados en el JSON. | Se conservan los cambios. |
| Compilar mediante Maven Wrapper en Windows. | Compilación sin errores. | Correcto. |
| Abrir la aplicación desde la raíz. | Mostrar los tres registros del JSON. | Correcto. |
| Pulsar los seis botones. | Mostrar avisos de funcionalidad pendiente sin modificar datos. | Correcto. |
| Iniciar sin JSON, con una ruta de prueba. | Mostrar la tabla vacía sin crear el archivo. | Correcto. |
| Iniciar con JSON inválido, usando una copia de prueba. | Informar el error sin cerrar la ventana ni modificar el archivo. | Correcto. |
| Leer y escribir registros en un archivo temporal. | Recuperar todos los atributos, incluidos caracteres con tildes. | Correcto. |
| Guardar desde la fachada tras una carga fallida. | Rechazar el guardado sin reemplazar el archivo. | Correcto. |

### Pruebas de nuestro desarrollo

**Pendiente:** realizar estas pruebas y reemplazar los resultados por lo
realmente observado. No se consideran aprobadas por estar incluidas en la tabla.

| Acción | Resultado esperado | Resultado obtenido |
| --- | --- | --- |
| Crear un registro válido. | Aparece en el listado con identificador único. | Pendiente: alta sin implementar. |
| Incumplir la primera validación. | Informar el motivo y permitir corregir. | Pendiente: validación sin implementar. |
| Incumplir la segunda validación. | Informar el motivo y permitir corregir. | Pendiente: validación sin implementar. |
| Incumplir la regla propia del problema. | Rechazar la operación y explicar el motivo. | Pendiente: regla sin implementar. |
| Modificar un registro seleccionado. | Actualizarlo conservando su identificador, sin duplicarlo. | Pendiente: modificación sin implementar. |
| Cancelar una eliminación y confirmar otra con un registro de prueba. | Conservar el primero y eliminar solo el confirmado. | Pendiente: eliminación sin implementar. |
| Aplicar una búsqueda o filtro. | Mostrar únicamente los registros que cumplen el criterio. | Pendiente: búsqueda sin implementar. |
| Limpiar el formulario. | Dejarlo preparado para una nueva operación. | Pendiente: limpieza sin implementar. |
| Crear y modificar datos, guardar, cerrar y volver a abrir. | Recuperar los cambios desde el JSON. | Pendiente: guardado sin integrar. |
| Iniciar sin JSON y con JSON inválido, usando copias de prueba. | Mantener el manejo de errores de la base. | Pendiente: volver a comprobar tras nuestros cambios. |

Las limitaciones actuales son las funciones pendientes descritas en este
documento. **Pendiente:** actualizar esta sección con las limitaciones reales
de nuestra entrega.

## Procedencia del código y recursos utilizados

- **Base docente:** estructura MVC con fachada, modelo genérico, ventana inicial,
  avisos de funcionalidad pendiente, carga de ejemplo, infraestructura JSON y
  configuración Maven con Wrapper.
- **Trabajo de nuestro equipo:** pendiente de implementar y detallar,
  identificando qué agregamos y qué modificamos.
- **Bibliotecas:** Gson 2.13.2 para leer y escribir JSON; Swing, incluido en Java,
  para la interfaz.
- **Tutoriales, documentación y herramientas de IA utilizados por el equipo:**
  pendiente de declarar. Indicar los recursos, su uso concreto y las partes
  correspondientes, o aclarar si no se utilizaron.

## Aportes de cada integrante

| Integrante | Aportes realizados |
| --- | --- |
| Laura Castro. | Creación del repositorio y configuración del entorno. Modelo (clases Material, EstadoConservacion y ValidacionException), validaciones, adaptación de la persistencia y fachada. Pruebas de validaciones y de archivos. |
| Pendiente: segundo integrante. | Pendiente de registrar aportes concretos. |

Las dos estudiamos la solución completa y participamos en el video donde cada una va a explicar lo que hizo la compañera para demostrar que sabemos lo que estamos haciendo.

## Video explicativo

**Pendiente de crear y publicar. No hay un video disponible todavía.**

Enlace público: **pendiente de agregar**.

El video debe durar como máximo cinco minutos, incluir la participación de todos
los integrantes y mostrar el problema, la aplicación, una validación, los tres
componentes nuevos, el recorrido de una operación y la conservación de datos.

Lo alojaremos en una plataforma externa, sin subir el archivo al repositorio.
Antes de entregar, comprobaremos que el enlace permita verlo desde una ventana
privada, sin iniciar sesión ni solicitar permisos.
