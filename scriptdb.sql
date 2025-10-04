-- ============================================
--  SCRIPT DE BASE DE DATOS: ADOPCIÓN DE MASCOTAS
--  Autor: Byron A. Caal Figueroa
--  Fecha: Octubre 2025-UMG PROGRA II 
--  Uso: Ejercicio de conexión Java + SQL Server
-- ============================================

-- Crear la base de datos
CREATE DATABASE DemoMascotas;
GO

-- Usar la base recién creada
USE DemoMascotas;
GO

--Crear la tabla principal
CREATE TABLE dbo.Mascotas (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Nombre NVARCHAR(100) NOT NULL,
    Tipo NVARCHAR(50) NOT NULL,             -- Perro, Gato, Otro
    Edad INT NOT NULL,
    Estado NVARCHAR(20) NOT NULL DEFAULT 'Disponible', -- Disponible / Adoptada
    Notas NVARCHAR(200) NULL,
    FechaRegistro DATETIME DEFAULT GETDATE()
);
GO

-- Insertar algunos registros de ejemplo
INSERT INTO dbo.Mascotas (Nombre, Tipo, Edad, Estado, Notas)
VALUES
('Luna', 'Gato', 2, 'Disponible', 'Cariñosa y juguetona'),
('Toby', 'Perro', 1, 'Disponible', 'Le gusta correr'),
('Rex', 'Perro', 3, 'Adoptada', 'Muy obediente'),
('Misu', 'Gato', 1, 'Disponible', 'Ideal para departamento'),
('Rocky', 'Perro', 5, 'Disponible', 'Entrenado para obediencia');
GO

-- Consultar los registros existentes
SELECT * FROM dbo.Mascotas;
GO

--Ejemplo de actualización (marcar adoptada)
-- UPDATE dbo.Mascotas SET Estado = 'Adoptada' WHERE Id = 1;

-- Ejemplo de eliminación
-- DELETE FROM dbo.Mascotas WHERE Id = 5;

-- Ejemplo de búsqueda
-- SELECT * FROM dbo.Mascotas WHERE Tipo = 'Gato';

-- ============================================
--  FIN DEL SCRIPT
-- ============================================
