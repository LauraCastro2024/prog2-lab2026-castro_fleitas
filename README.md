# Materiales de una biblioteca

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
el registro de los materiales y su estado, y pueda responder en el momento qué está realmente disponible.

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
 
Las validaciones están en el método `validar()` de `Material`. Si una regla no se cumple, se lanza una [ValidacionException](src/main/java/modelo/ValidacionException.java)
con el motivo; la vista muestra el mensaje y el formulario conserva los datos para corregirlos.

| Validación requerida | Regla de nuestro proyecto | Estado |
| --- | --- | --- |
| Primera validación | **Campo obligatorio:** El título no puede ser nulo ni estar vacío (`titulo.trim().isEmpty()`). | Implementado. |
| Segunda validación | **Coherencia de ejemplares:** Debe haber al menos 1 ejemplar total en biblioteca, y los ejemplares prestados no pueden ser negativos ni superar la cantidad total de ejemplares. | Implementado. |
| Tercera validación, propia del problema | **Restricción de préstamos y bajas:** Un material `DETERIORADO` no se puede prestar a domicilio, tiene que quedar solo para consulta en sala. Además, un material solo para sala no puede tener ejemplares prestados. | Implementado. |

Además, no se puede eliminar un material con ejemplares prestados
(`verificarQueSePuedeEliminar()`).
 
Otras decisiones:
 
- **Identificador único:** la fachada asigna el mayor identificador existente
  más uno. El campo del formulario no es editable.
- **Modificación sin duplicados:** se reemplaza el material que está en la
  misma posición de la lista; no se agrega uno nuevo.
- **Eliminación:** pide confirmación mostrando el título del material.
- **Búsqueda:** por texto en el título o el autor, sin distinguir mayúsculas,
  combinable con un filtro por tipo. Eligiendo "Todos los tipos" se vuelve a
  ver la lista completa.
- **Limpieza del formulario:** el botón Limpiar deja los campos vacíos y quita
  la selección de la tabla.
- **Guardado:** cada registro, modificación o eliminación se guarda
  automáticamente en el JSON. El botón Guardar permite reintentar si una
  escritura falló, y al cerrar se pide confirmación si quedaron cambios sin
  guardar.

## Componentes gráficos

La base incluye `JTextField`, `JComboBox`, `JCheckBox`, `JButton` y `JTable`.
JLabel y los contenedores de apoyo no cuentan para alcanzar el mínimo solicitado.

Los adaptamos así: JTextField (título, autor y búsqueda), JComboBox (tipo de
material y filtro por tipo), JCheckBox (solo para consulta en sala), JButton
(acciones) y JTable (listado y selección).

Se conservaron todos los componentes iniciales y se incorporaron tres tipos nuevos con una función real dentro del sistema:

| Componente nuevo | Dato o acción real que permite gestionar | Estado |
| --- | --- | --- |
| JSpinner | Cantidad de ejemplares en la biblioteca y de ejemplares prestados. Con ellos se calculan los disponibles y se aplica la segunda validación. | Implementado. |
| JRadioButton | Estado de conservación: Bueno, Regular o Deteriorado. Son opciones excluyentes gracias a un `ButtonGroup`, y "Deteriorado" participa en la regla propia. | Implementado. |
| JTextArea | Observaciones sobre el material, que se guardan en el JSON. | Implementado. |

## Organización de paquetes y recorrido de una operación

| Paquete o carpeta | Responsabilidad |
| --- | --- |
| [app](src/main/java/app) | Iniciar la aplicación y conectar los objetos. |
| [modelo](src/main/java/modelo) | Representar los materiales y sus reglas (`Material`, `EstadoConservacion`, `ValidacionException`). |
| [vista](src/main/java/vista) | Mostrar la interfaz Swing y los mensajes. |
| [controlador](src/main/java/controlador) | Recibir eventos y coordinar las acciones mediante la fachada. |
| [fachada](src/main/java/fachada) | Ofrecer operaciones y coordinar el modelo con la persistencia. |
| [persistencia](src/main/java/persistencia) | Leer y escribir JSON, sin depender de Swing. |
| [datos](datos) | Contener el archivo JSON; no es un paquete Java. |

El recorrido de la carga inicial:
[Aplicacion](src/main/java/app/Aplicacion.java) conecta los objetos; el controlador solicita la carga a la fachada; la fachada utiliza la persistencia para leer el archivo; y el controlador entrega los registros a la vista para mostrarlos en la tabla. La vista no lee ni escribe archivos.

**Recorrido de la operación Registrar, implementada por nuestro equipo:**
 
1. El usuario completa el formulario y pulsa **Registrar**.
2. `ControladorRegistros.registrar()` recibe el evento y le pide a la vista los datos con `obtenerDatosFormulario()`, que arma un `Material`.
3. El controlador llama a `FachadaRegistros.registrar(material)`.
4. La fachada comprueba que la carga inicial fue correcta y llama a `material.validar()` (modelo). Si una regla no se cumple, se lanza `ValidacionException` y no se agrega nada.
5. Si es válido, la fachada asigna el siguiente identificador, agrega una copia del material a la lista y llama a `PersistenciaRegistros.escribir()`, que guarda la lista en `datos/registros.json`.
6. El controlador actualiza la tabla, limpia el formulario y muestra la confirmación con el identificador asignado. Si hubo un error, lo muestra con `mostrarError()` sin cerrar la aplicación.

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

```text
git clone https://github.com/LauraCastro2024/prog2-lab2026-castro_fleitas.git

cd prog2-lab2026-castro_fleitas
```

Ejecutar los siguientes comandos desde la raíz del proyecto, donde se encuentra [pom.xml](pom.xml).

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
Modificamos `.vscode/settings.json` para que la terminal de VS Code use el JDK indicado por `JAVA_HOME` en cada computadora, en lugar de una ruta fija.

La biblioteca de persistencia es Gson 2.13.2. Está declarada, junto con los
complementos de compilación y ejecución, en [pom.xml](pom.xml).

### Archivo de datos

El archivo de ejemplo es [datos/registros.json](datos/registros.json).
La ruta es relativa al directorio desde el cual se inicia la aplicación.

Si el archivo no existe, la aplicación inicia con una lista vacía y lo crea al registrar el primer material. Si es inválido o ilegible (JSON mal escrito, datos incompletos o materiales que no cumplen las reglas), muestra un error y
mantiene la ventana abierta sin reemplazarlo. La fachada bloquea el guardado cuando la carga no fue correcta.

**Cada registro, modificación o eliminación se guarda automáticamente. Si una
escritura falla, se muestra el error, se puede reintentar con el botón Guardar
y, al cerrar la ventana, se pide confirmación si quedaron cambios sin guardar.**

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
| Compilar mediante Maven Wrapper en Windows. | Compilación sin errores. | Correcto. |
| Abrir la aplicación desde la raíz. | Mostrar los tres registros del JSON. | Correcto. |
| Pulsar los seis botones. | Mostrar avisos de funcionalidad pendiente sin modificar datos. | Correcto. |
| Iniciar sin JSON, con una ruta de prueba. | Mostrar la tabla vacía sin crear el archivo. | Correcto. |
| Iniciar con JSON inválido, usando una copia de prueba. | Informar el error sin cerrar la ventana ni modificar el archivo. | Correcto. |
| Leer y escribir registros en un archivo temporal. | Recuperar todos los atributos, incluidos caracteres con tildes. | Correcto. |
| Guardar desde la fachada tras una carga fallida. | Rechazar el guardado sin reemplazar el archivo. | Correcto. |

### Pruebas de nuestro desarrollo

Las pruebas se hicieron desde la ventana, en Windows, usando copias del archivo de datos para los casos de archivo inexistente e inválido.

| Acción | Resultado esperado | Resultado obtenido |
| --- | --- | --- |
| Registrar "La Tregua" con 5 ejemplares y 1 prestado. | Aparece en el listado con identificador 4 y 4 disponibles. | "Material registrado con el identificador 4". |
|Cambiar título de "La Tregua" “La Tregua (edición escolar)” | Conserva el identificador 4 y no aparece duplicada. | "Material 4 modificado" |
| Eliminar el Diccionario y responder No. | Se cancela y el material se conserva.| "Eliminación cancelada. El material se conserva".|
| Eliminar un material de prueba y responder Sí. | Se elimina solo ese material. | "Material eliminado". |
| Buscar "gregori" y filtrar por tipo Mapa. | Muestra solo los materiales que coinciden. | No aparece ya que es de tipo Libro |
| Limpiar el formulario. | Los campos quedan vacíos, listos para un material nuevo. | Los campos aparecen vacíos |
| Cerrar y volver a abrir la aplicación. | Se conservan los cambios guardados en el JSON. | Se conservan los cambios. |
| Registrar con el título vacío. | Mensaje "El título es obligatorio."; no se registra. | Correcto: mensaje "El título es obligatorio." y no se registró. |
| Registrar con 2 ejemplares y 5 prestados. | Mensaje indicando que hay más prestados que ejemplares. | Correcto: mensaje "No puede haber más ejemplares prestados (5) que ejemplares en la biblioteca (2)." |
| Registrar un material "Deteriorado" sin marcar "Solo para consulta en sala". | Mensaje con la regla propia del problema; no se registra. | Correcto: mensaje "Un material deteriorado no se puede prestar a domicilio." y no se registró. |
| Eliminar "Informática y educación", que tiene 4 ejemplares prestados. | Se rechaza y se explica el motivo. | Correcto: no se eliminó y se informó que tiene 4 ejemplares prestados. |
| Iniciar sin archivo JSON (con el original respaldado). | Tabla vacía sin error; el primer registro recibe el identificador 1 y crea el archivo. | Correcto: la tabla abrió vacía, el primer material recibió el identificador 1 y se creó el archivo. |
| Iniciar con un JSON inválido (copia de prueba). | Informa el error, no permite cambios y no modifica el archivo. | Correcto: mostró el error sin cerrarse y el archivo no se modificó. |

Limitaciones conocidas:
 
- No registra préstamos individuales (quién se llevó cada ejemplar ni fechas
  de devolución), solo la cantidad de ejemplares prestados.
- Si se elimina el material con el identificador más alto, ese número puede
  volver a asignarse a un material nuevo, aunque nunca coincide con el de uno
  existente.
- La búsqueda distingue tildes: "gregori" encuentra "Gregori", pero "america"
  no encuentra "América".
- Está pensada para un solo usuario a la vez.
- No hay pruebas automatizadas.

## Procedencia del código y recursos utilizados

- **Base docente:** estructura MVC con fachada, modelo genérico, ventana inicial, avisos de funcionalidad pendiente, carga de ejemplo, infraestructura JSON y
  configuración Maven con Wrapper.
- **Trabajo de nuestro equipo:**
  - Modelo: la clase `Registro` se transformó en `Material`; se agregaron el enum `EstadoConservacion`, la excepción `ValidacionException`, las
    validaciones y las reglas propias.
  - Persistencia: adaptada a los nuevos atributos y con validación de los
    materiales al leer el archivo.
  - Fachada: operaciones registrar, modificar, eliminar y buscar, asignación del identificador y guardado después de cada cambio.
  - Vista: formulario nuevo con los tres componentes, tabla con disponibles,
    filtro por tipo y confirmaciones.
  - Controlador: conexión de todos los botones, selección en la tabla, manejo
    de errores y aviso al cerrar.
  - Datos de ejemplo, configuración de VS Code y este README.
- **Bibliotecas:** Gson 2.13.2 para leer y escribir JSON; Swing, incluido en Java,
  para la interfaz.
- **Declaración de uso de Inteligencia Artificial:**
   Durante el desarrollo del proyecto utilizamos Claude, el asistente de IA de Anthropic, como herramienta de apoyo para organizar ideas y revisar el código.También nos explicó cómo trabajar las dos sobre el mismo proyecto, cada una desde su computadora.Partimos del prototipo que nos dio el profesor y lo adaptamos al problema de la biblioteca. Las decisiones sobre el problema, las reglas y los cambios en la aplicación las tomamos nosotras, y revisamos y probamos todo el código que modificamos o agregamos.
   Para realizar todo el proyecto nos basamos en los materiales de Programación 2 del
  Prof. Domingo Pérez 
  
## Aportes de cada integrante

  El desarrollo se realizó de forma modular: cada integrante incorporó y compiló su código, depurando fallos de forma iterativa. La validación final se llevó a cabo a través de la interfaz visual, ejecutando los casos de prueba manualmente en la aplicación y documentando los resultados reales en la tabla correspondiente.

| Integrante | Aportes realizados |
| --- | --- |
| María Laura Castro Bazán | Creación del repositorio y configuración del entorno. Modelo (clases Material, EstadoConservacion y ValidacionException), validaciones, adaptación de la persistencia y fachada. Pruebas de validaciones y de archivos. |
| Camila Fleitas | Vista (formulario, JSpinner, JRadioButton, JTextArea, filtro de búsqueda) y controlador (conexión de botones, selección, confirmaciones y mensajes). Pruebas de gestión, búsqueda y conservación de datos. |

Las dos estudiamos la solución completa. En el video, cada una explica su
propio aporte y comenta también una parte del trabajo de la compañera.

## Video explicativo

Enlace público: https://drive.google.com/file/d/1JqNi67NvedoY252gT90Xgo9d4Q8QMBdB/view?usp=sharing

El video dura menos de cinco minutos y participamos las dos integrantes, cada una
explicando su aporte. Muestra:

- el problema y la solución;
- la aplicación funcionando y una validación;
- los tres componentes nuevos (JSpinner, JRadioButton y JTextArea);
- el recorrido de la operación Registrar por MVC, fachada y persistencia;
- la conservación de los datos al cerrar y volver a abrir la aplicación.

El video está alojado en Google Drive, con acceso para cualquier persona que tenga el enlace, y no se subió al repositorio. Comprobamos que se puede ver desde una ventana privada, sin iniciar sesión ni solicitar permisos.