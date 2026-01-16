# Paquete DAO - Data Access Object

Este paquete contiene las clases DAO (Data Access Object) para todas las entidades de la base de datos `eduelorrieta`.

## 📋 Clases DAO Disponibles

### 1. **CiclosDAO**
Operaciones CRUD para la entidad `Ciclos`.

**Métodos principales:**
- `create(Ciclos ciclo)` - Crea un nuevo ciclo
- `findById(Integer id)` - Busca por ID
- `findByNombre(String nombre)` - Busca por nombre
- `findAll()` - Obtiene todos los ciclos
- `update(Ciclos ciclo)` - Actualiza un ciclo
- `delete(Integer id)` - Elimina un ciclo
- `count()` - Cuenta total de ciclos

**Ejemplo de uso:**
```java
CiclosDAO ciclosDAO = new CiclosDAO();

// Obtener todos los ciclos
List<Ciclos> ciclos = ciclosDAO.findAll();

// Buscar ciclo por nombre
Ciclos dam = ciclosDAO.findByNombre("DAM");

// Crear nuevo ciclo
Ciclos nuevoCiclo = new Ciclos();
nuevoCiclo.setNombre("SMR");
ciclosDAO.create(nuevoCiclo);
```

---

### 2. **ModulosDAO**
Operaciones CRUD para la entidad `Modulos`.

**Métodos principales:**
- `create(Modulos modulo)` - Crea un nuevo módulo
- `findById(Integer id)` - Busca por ID
- `findByNombre(String nombre)` - Busca por nombre
- `findByCiclo(Integer cicloId)` - Obtiene módulos de un ciclo
- `findByCicloAndCurso(Integer cicloId, Byte curso)` - Módulos por ciclo y curso
- `findAll()` - Obtiene todos los módulos
- `update(Modulos modulo)` - Actualiza un módulo
- `delete(Integer id)` - Elimina un módulo
- `count()` - Cuenta total de módulos

**Ejemplo de uso:**
```java
ModulosDAO modulosDAO = new ModulosDAO();

// Obtener módulos de DAM (ciclo_id = 1)
List<Modulos> modulosDAM = modulosDAO.findByCiclo(1);

// Obtener módulos de 2º curso de DAM
List<Modulos> modulos2DAM = modulosDAO.findByCicloAndCurso(1, (byte) 2);

// Buscar módulo específico
Modulos psp = modulosDAO.findByNombre("Programación de servicios y procesos");
```

---

### 3. **HorariosDAO**
Operaciones CRUD para la entidad `Horarios`.

**Métodos principales:**
- `create(Horarios horario)` - Crea un nuevo horario
- `findById(Integer id)` - Busca por ID
- `findByProfesor(Integer profesorId)` - Horarios de un profesor
- `findByModulo(Integer moduloId)` - Horarios de un módulo
- `findByDia(String dia)` - Horarios de un día específico
- `findByAula(String aula)` - Horarios de un aula
- `findByDiaHoraAula(String dia, Byte hora, String aula)` - Verifica disponibilidad
- `findAll()` - Obtiene todos los horarios
- `update(Horarios horario)` - Actualiza un horario
- `delete(Integer id)` - Elimina un horario
- `count()` - Cuenta total de horarios

**Ejemplo de uso:**
```java
HorariosDAO horariosDAO = new HorariosDAO();

// Obtener horario de un profesor
List<Horarios> miHorario = horariosDAO.findByProfesor(3);

// Verificar si un aula está ocupada
Horarios ocupado = horariosDAO.findByDiaHoraAula("LUNES", (byte) 1, "Aula B101");

// Obtener todos los horarios del lunes
List<Horarios> lunes = horariosDAO.findByDia("LUNES");
```

---

### 4. **MatriculacionesDAO**
Operaciones CRUD para la entidad `Matriculaciones`.

**Métodos principales:**
- `create(Matriculaciones matriculacion)` - Crea una nueva matriculación
- `findById(Integer id)` - Busca por ID
- `findByAlumno(Integer alumnoId)` - Matriculaciones de un alumno
- `findByCiclo(Integer cicloId)` - Matriculaciones de un ciclo
- `findByCicloAndCurso(Integer cicloId, Byte curso)` - Por ciclo y curso
- `existsMatriculacion(Integer alumnoId, Integer cicloId, Byte curso)` - Verifica existencia
- `findAll()` - Obtiene todas las matriculaciones
- `update(Matriculaciones matriculacion)` - Actualiza una matriculación
- `delete(Integer id)` - Elimina una matriculación
- `count()` - Cuenta total de matriculaciones

**Ejemplo de uso:**
```java
MatriculacionesDAO matriculacionesDAO = new MatriculacionesDAO();

// Verificar si un alumno ya está matriculado
boolean yaMatriculado = matriculacionesDAO.existsMatriculacion(7, 1, (byte) 1);

// Obtener matriculaciones de un alumno
List<Matriculaciones> misMatriculas = matriculacionesDAO.findByAlumno(7);

// Crear nueva matriculación
Matriculaciones nuevaMatricula = new Matriculaciones();
// ... configurar datos
matriculacionesDAO.create(nuevaMatricula);
```

---

### 5. **ReunionesDAO**
Operaciones CRUD para la entidad `Reuniones`.

**Métodos principales:**
- `create(Reuniones reunion)` - Crea una nueva reunión
- `findById(Integer id)` - Busca por ID
- `findByProfesor(Integer profesorId)` - Reuniones de un profesor
- `findByAlumno(Integer alumnoId)` - Reuniones de un alumno
- `findByEstado(String estado)` - Reuniones por estado
- `findPendientesByProfesor(Integer profesorId)` - Reuniones pendientes
- `findByFechaRange(LocalDateTime inicio, LocalDateTime fin)` - Por rango de fechas
- `findAll()` - Obtiene todas las reuniones
- `update(Reuniones reunion)` - Actualiza una reunión
- `delete(Integer id)` - Elimina una reunión
- `count()` - Cuenta total de reuniones

**Ejemplo de uso:**
```java
ReunionesDAO reunionesDAO = new ReunionesDAO();

// Obtener reuniones pendientes de un profesor
List<Reuniones> pendientes = reunionesDAO.findPendientesByProfesor(3);

// Buscar reuniones en un rango de fechas
LocalDateTime inicio = LocalDateTime.of(2025, 1, 1, 0, 0);
LocalDateTime fin = LocalDateTime.of(2025, 1, 31, 23, 59);
List<Reuniones> reunionesEnero = reunionesDAO.findByFechaRange(inicio, fin);

// Actualizar estado de reunión
Reuniones reunion = reunionesDAO.findById(1);
reunion.setEstado("aceptada");
reunionesDAO.update(reunion);
```

---

### 6. **UsersDAO**
Operaciones CRUD para la entidad `Users`.

**Métodos principales:**
- `create(Users user)` - Crea un nuevo usuario
- `findById(Integer id)` - Busca por ID
- `findByEmail(String email)` - Busca por email
- `findByUsername(String username)` - Busca por username
- `findByDni(String dni)` - Busca por DNI
- `findByTipo(Integer tipoId)` - Usuarios por tipo
- `findAllProfesores()` - Obtiene todos los profesores
- `findAllAlumnos()` - Obtiene todos los alumnos
- `findAllAdministradores()` - Obtiene todos los administradores
- `searchByNombreOrApellidos(String searchTerm)` - Búsqueda por nombre
- `validateCredentials(String username, String password)` - Valida login
- `existsByEmail(String email)` - Verifica si email existe
- `existsByUsername(String username)` - Verifica si username existe
- `findAll()` - Obtiene todos los usuarios
- `update(Users user)` - Actualiza un usuario
- `delete(Integer id)` - Elimina un usuario
- `count()` - Cuenta total de usuarios
- `countByTipo(Integer tipoId)` - Cuenta por tipo

**Ejemplo de uso:**
```java
UsersDAO usersDAO = new UsersDAO();

// Validar login
Users usuario = usersDAO.validateCredentials("profesor1", "123456");

// Obtener todos los profesores
List<Users> profesores = usersDAO.findAllProfesores();

// Verificar si un email ya existe
boolean existe = usersDAO.existsByEmail("nuevo@elorrieta.com");

// Buscar usuarios por nombre
List<Users> resultados = usersDAO.searchByNombreOrApellidos("Lopez");

// Crear nuevo usuario
Users nuevoUsuario = new Users();
// ... configurar datos
usersDAO.create(nuevoUsuario);
```

---

### 7. **TiposDAO**
Operaciones CRUD para la entidad `Tipos`.

**Métodos principales:**
- `create(Tipos tipo)` - Crea un nuevo tipo
- `findById(Integer id)` - Busca por ID
- `findByName(String name)` - Busca por nombre
- `findAll()` - Obtiene todos los tipos
- `update(Tipos tipo)` - Actualiza un tipo
- `delete(Integer id)` - Elimina un tipo
- `count()` - Cuenta total de tipos
- `getTipoProfesor()` - Obtiene el tipo "profesor"
- `getTipoAlumno()` - Obtiene el tipo "alumno"
- `getTipoAdministrador()` - Obtiene el tipo "administrador"
- `getTipoGod()` - Obtiene el tipo "god"

**Ejemplo de uso:**
```java
TiposDAO tiposDAO = new TiposDAO();

// Obtener tipo profesor
Tipos tipoProfesor = tiposDAO.getTipoProfesor();

// Obtener todos los tipos
List<Tipos> tipos = tiposDAO.findAll();

// Buscar tipo por nombre
Tipos alumno = tiposDAO.findByName("alumno");
```

---

## 🔧 Características Comunes

Todas las clases DAO implementan:

✅ **Gestión automática de transacciones** con rollback en caso de error  
✅ **Logging** de todas las operaciones (éxito y errores)  
✅ **Cierre automático de sesiones** Hibernate  
✅ **Manejo de excepciones** robusto  
✅ **Métodos de consulta** optimizados con HQL  

## 📝 Notas Importantes

1. **Hibernate Sessions**: Todas las sesiones se cierran automáticamente en el bloque `finally`
2. **Transacciones**: Las operaciones de escritura (create, update, delete) usan transacciones con rollback automático
3. **Logging**: Se usa `SocketLogger` para registrar operaciones y errores
4. **Null Safety**: Todos los métodos manejan correctamente valores null

## 🚀 Integración con el Servidor

Para usar estos DAOs en tu `RequestProcessor` o servicios:

```java
// En RequestProcessor.java
private UsersDAO usersDAO = new UsersDAO();
private HorariosDAO horariosDAO = new HorariosDAO();
// etc...

// Ejemplo en un método de procesamiento
public Response handleGetHorario(Request request) {
    Integer profesorId = (Integer) request.getData().get("profesorId");
    List<Horarios> horario = horariosDAO.findByProfesor(profesorId);
    
    if (horario != null) {
        return Response.success("Horario obtenido", horario);
    } else {
        return Response.error("Error al obtener horario");
    }
}
```

---

## 📚 Base de Datos

Base de datos: `eduelorrieta`  
Tablas: `users`, `tipos`, `ciclos`, `modulos`, `horarios`, `matriculaciones`, `reuniones`

---

**Autor:** Sistema DAO Generado Automáticamente  
**Fecha:** 2026-01-16  
**Versión:** 1.0
