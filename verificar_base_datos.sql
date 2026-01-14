-- =====================================================
-- SCRIPT DE VERIFICACIÓN - BASE DE DATOS reto2bbdd
-- =====================================================
-- Ejecuta este script en phpMyAdmin para verificar que 
-- tu base de datos está correctamente configurada
-- =====================================================

USE reto2bbdd;

-- 1. Verificar que todas las tablas existen
SELECT 'Verificando tablas...' AS info;
SHOW TABLES;

-- 2. Contar usuarios por tipo
SELECT 'Usuarios por tipo:' AS info;
SELECT t.name AS tipo, COUNT(u.id) AS cantidad
FROM tipos t
LEFT JOIN users u ON u.tipo_id = t.id
GROUP BY t.id, t.name
ORDER BY t.id;

-- 3. Verificar usuarios con credenciales para login
SELECT 'Usuarios disponibles para login (password: 123456):' AS info;
SELECT id, username, nombre, apellidos, 
       CASE tipo_id
           WHEN 1 THEN 'God'
           WHEN 2 THEN 'Administrador'
           WHEN 3 THEN 'Profesor'
           WHEN 4 THEN 'Alumno'
       END AS tipo
FROM users
ORDER BY tipo_id, id;

-- 4. Verificar ciclos
SELECT 'Ciclos disponibles:' AS info;
SELECT * FROM ciclos ORDER BY id;

-- 5. Verificar módulos por ciclo
SELECT 'Módulos por ciclo:' AS info;
SELECT c.nombre AS ciclo, m.curso, COUNT(m.id) AS num_modulos
FROM ciclos c
LEFT JOIN modulos m ON m.ciclo_id = c.id
GROUP BY c.id, c.nombre, m.curso
ORDER BY c.id, m.curso;

-- 6. Verificar horarios del profesor1 (para testing)
SELECT 'Horarios del profesor1 (Roman Lopez):' AS info;
SELECT h.dia, h.hora, m.nombre AS modulo, h.aula
FROM horarios h
INNER JOIN modulos m ON h.modulo_id = m.id
WHERE h.profe_id = 3
ORDER BY 
    FIELD(h.dia, 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'),
    h.hora;

-- 7. Verificar reuniones
SELECT 'Total de reuniones:' AS info;
SELECT COUNT(*) AS total_reuniones FROM reuniones;

-- 8. Verificar matriculaciones
SELECT 'Total de matriculaciones:' AS info;
SELECT COUNT(*) AS total_matriculaciones FROM matriculaciones;

-- 9. Verificar integridad referencial
SELECT 'Verificando integridad de datos...' AS info;

-- Horarios sin profesor
SELECT 'Horarios sin profesor válido:' AS verificacion, COUNT(*) AS cantidad
FROM horarios h
LEFT JOIN users u ON h.profe_id = u.id
WHERE u.id IS NULL;

-- Horarios sin módulo válido  
SELECT 'Horarios sin módulo válido:' AS verificacion, COUNT(*) AS cantidad
FROM horarios h
LEFT JOIN modulos m ON h.modulo_id = m.id
WHERE m.id IS NULL;

-- Módulos sin ciclo válido
SELECT 'Módulos sin ciclo válido:' AS verificacion, COUNT(*) AS cantidad
FROM modulos m
LEFT JOIN ciclos c ON m.ciclo_id = c.id
WHERE c.id IS NULL;

-- 10. Información del profesor1 para testing
SELECT 'Datos del profesor1 para testing:' AS info;
SELECT 
    u.id,
    u.username,
    u.password,
    u.nombre,
    u.apellidos,
    u.email,
    t.name AS tipo
FROM users u
INNER JOIN tipos t ON u.tipo_id = t.id
WHERE u.username = 'profesor1';

-- =====================================================
-- RESULTADO ESPERADO:
-- - 7 tablas: users, tipos, ciclos, modulos, horarios, reuniones, matriculaciones
-- - 17 usuarios (1 god, 1 admin, 10 profesores, 4 alumnos)
-- - 5 ciclos
-- - Horarios del profesor1 (Roman Lopez)
-- - 0 errores de integridad
-- =====================================================
