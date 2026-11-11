# LASKT System — Sistema de Gestión de Nómina

<p align="center">
  <strong>Automatización de nómina y gestión de empleados</strong>
  <br/>
  Aplicación de escritorio desarrollada en Java Swing con persistencia de datos en Azure SQL Database.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-11%2B-orange?logo=openjdk" />
  <img src="https://img.shields.io/badge/GUI-Java%20Swing-blue" />
  <img src="https://img.shields.io/badge/Database-Azure%20SQL-0078D4?logo=microsoftazure" />
  <img src="https://img.shields.io/badge/Connectivity-JDBC-green" />
  <img src="https://img.shields.io/badge/Project-Academic-purple" />
</p>

---

## Descripción

**LASKT System** es una aplicación de escritorio diseñada para automatizar los procesos de gestión de empleados y cálculo de nómina de una empresa.

El sistema permite registrar colaboradores, administrar sus datos laborales, calcular automáticamente sus ingresos y deducciones salariales y consultar la información de los empleados registrados.

Desarrollado como proyecto final de la asignatura **Programación II (INF514)** de la Universidad Autónoma de Santo Domingo (UASD), el proyecto integra programación orientada a objetos, interfaces gráficas con Java Swing y persistencia de información mediante JDBC sobre una base de datos relacional en la nube.

Su arquitectura modular separa la presentación, los modelos de datos, la lógica de negocio y las utilidades, facilitando la organización y el mantenimiento del código.

## Características principales

### Gestión de empleados
- Registro de nuevos empleados.
- Captura de nombre, cédula, cargo y salario base.
- Validación de los campos obligatorios.
- Prevención de registros duplicados mediante la cédula.
- Consulta de empleados mediante nombre o número de cédula.
- Visualización de empleados registrados en una tabla.
- Eliminación de empleados con confirmación previa.

### Cálculo automático de nómina

El sistema procesa los componentes salariales de cada empleado para determinar su salario bruto, las deducciones correspondientes y el salario neto a pagar.

- Cálculo del valor de las horas ordinarias.
- Aplicación de recargo del 35 % a las horas extras.
- Incorporación de bonificaciones.
- Cálculo de deducciones de la Tesorería de la Seguridad Social (TSS).
- Obtención del salario neto.
- Generación de un resumen individual de nómina.
- Cálculo del total general de la nómina.

### Interfaz gráfica
- Diseño personalizado mediante Java Swing.
- Interfaz dibujada mediante gráficos vectoriales.
- Escalado proporcional basado en una resolución de referencia de 1920 × 1080.
- Campos de entrada personalizados con bordes redondeados.
- Menú lateral interactivo.
- Selector emergente de cargos.
- Calculadora integrada para operaciones aritméticas básicas.

### Base de datos
- Conexión a Azure SQL Database.
- Integración mediante JDBC.
- Consultas parametrizadas utilizando `PreparedStatement`.
- Recuperación de empleados al iniciar la aplicación.
- Persistencia de registros y eliminación de empleados.

---

## Cálculo de nómina

Uno de los componentes centrales de LASKT System es su módulo de cálculo salarial, implementado en la clase `Nomina`.

El procesamiento se divide en cuatro operaciones principales:

**1. Valor de la hora ordinaria**

El salario base mensual se divide entre 190.64 horas laborables:

\[
H = \frac{SalarioBase}{190.64}
\]

**2. Pago de horas extras**

Las horas extraordinarias reciben un recargo del 35 %:

\[
HE = H \times 1.35 \times HorasExtras
\]

**3. Salario bruto**

Se suman el salario base, el pago de horas extras y las bonificaciones:

\[
SB = SalarioBase + HE + Bonos
\]

**4. Deducciones y salario neto**

Cuando el empleado tiene el seguro activado, se aplica una deducción del 5.91 %, correspondiente a:

| Concepto | Porcentaje |
|---|---:|
| Seguro Familiar de Salud (SFS) | 3.04 % |
| Administradora de Fondos de Pensiones (AFP) | 2.87 % |
| **Total** | **5.91 %** |

\[
D =
\begin{cases}
SB \times 0.0591 & \text{Si tiene seguro}\\
0 & \text{Si no tiene seguro}
\end{cases}
\]

Finalmente:

\[
\boxed{SalarioNeto = SB - D}
\]

El resultado se presenta mediante un resumen que contiene los datos del empleado, sus ingresos adicionales, las deducciones y el monto neto a pagar.

> **Nota:** El cálculo implementado representa la lógica académica del proyecto. No contempla todavía los topes de salario cotizable de la TSS ni el Impuesto Sobre la Renta (ISR), por lo que no debe considerarse un motor de nómina completo para cumplimiento legal.

---

## Arquitectura del proyecto

LASKT System utiliza una arquitectura modular organizada en cuatro capas, cada una con responsabilidades específicas.

```text
                 ┌───────────────────┐
                 │       Main        │
                 │  Punto de entrada │
                 └─────────┬─────────┘
                           │
                 ┌─────────▼─────────┐
                 │       VIEW        │
                 │  Interfaz gráfica │
                 └─────────┬─────────┘
                           │
                 ┌─────────▼─────────┐
                 │      SERVICE      │
                 │   Lógica negocio  │
                 └─────────┬─────────┘
                           │
                 ┌─────────▼─────────┐
                 │       MODEL       │
                 │ Empleado / Nómina │
                 └───────────────────┘

                 ┌───────────────────┐
                 │       UTILS       │
                 │ Validaciones / BD │
                 └───────────────────┘
```

### Organización de paquetes

| Paquete | Responsabilidad |
|---|---|
| `model` | Representación de los empleados y cálculos de nómina. |
| `service` | Gestión de empleados y operaciones de acceso a datos. |
| `utils` | Conexión a la base de datos y validaciones reutilizables. |
| `view` | Interfaces gráficas e interacción con el usuario. |

### Clases principales

**Model (`model/`)**

- `Empleado.java`: representa a cada colaborador y almacena sus datos personales y salariales.
- `Nomina.java`: contiene los métodos responsables del cálculo de horas extras, salario bruto, deducciones y salario neto.

**Service (`service/`)**

- `NominaService.java`: centraliza las operaciones de consulta, registro y eliminación de empleados, además del cálculo de la nómina general.

**Utils (`utils/`)**

- `ConexionBD.java`: administra la conexión JDBC con Azure SQL Database.
- `Validaciones.java`: contiene funciones de normalización de texto y un `DocumentFilter` para el formato automático de la cédula dominicana.

**View (`view/`)**

- `NominaFrame.java`: ventana principal de la aplicación. Gestiona la captura de información, las interacciones y el procesamiento de los registros.
- `EmpleadosFrame.java`: ventana secundaria que presenta la tabla de empleados registrados y permite eliminar registros.

**Punto de entrada**

- `Main.java`: inicia la aplicación mediante `SwingUtilities.invokeLater()`, garantizando que la interfaz gráfica se construya en el hilo de eventos de Swing.

---

## Modelo de datos

La persistencia utiliza **Microsoft Azure SQL Database** como sistema de gestión de bases de datos relacional.

El modelo implementado utiliza dos entidades principales:

### Departamentos

Almacena los departamentos de la organización y permite agrupar a los empleados.

### Empleados

Contiene la información personal y salarial de cada colaborador.

| Campo | Descripción |
|---|---|
| `Id_Empleado` | Identificador único del empleado (PK). |
| `Id_Departamento` | Departamento al que pertenece (FK). |
| `Nombre` | Nombre completo. |
| `Cedula` | Cédula de identidad dominicana. |
| `Cargo` | Puesto de trabajo. |
| `Salario_Base` | Salario mensual base. |
| `Horas_Extras` | Cantidad de horas extraordinarias. |
| `Bonos` | Monto de bonificaciones. |
| `Seguro` | Monto de la deducción calculada. |

La relación entre departamentos y empleados es de uno a muchos: un departamento puede tener varios empleados, pero cada empleado pertenece a un departamento.

La nómina se calcula dinámicamente mediante Java y no se almacena como una entidad independiente en la implementación actual.

### Operaciones de persistencia

- **SELECT:** recuperación de los empleados registrados al iniciar el sistema.
- **INSERT:** almacenamiento de nuevos empleados.
- **DELETE:** eliminación de empleados mediante su número de cédula.

---

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java | Lenguaje principal de desarrollo. |
| Java Swing | Construcción de la interfaz gráfica. |
| Java AWT / Java 2D | Dibujo vectorial y personalización visual. |
| JDBC | Comunicación con la base de datos. |
| Microsoft SQL Server | Motor de base de datos. |
| Azure SQL Database | Alojamiento de la base de datos en la nube. |
| Microsoft SQL Server JDBC Driver | Controlador para establecer conexiones con SQL Server. |
| Git / GitHub | Control de versiones y alojamiento del código. |

---

## Estructura de directorios

La organización del repositorio es la siguiente:

```text
sistema-nomina-java/
│
├── .vscode/
│   └── settings.json
│
├── src/
│   │
│   ├── lib/
│   │   └── mssql-jdbc-13.6.0.jre11.jar
│   │
│   ├── model/
│   │   ├── Departamento.java
│   │   ├── Empleado.java
│   │   └── Nomina.java
│   │
│   ├── service/
│   │   └── NominaService.java
│   │
│   ├── utils/
│   │   ├── ConexionBD.java
│   │   └── Validaciones.java
│   │
│   ├── view/
│   │   ├── EmpleadosFrame.java
│   │   └── NominaFrame.java
│   │
│   ├── Main.java
│   └── PruebaDriver.java
│
├── Compilador.bat
├── EjecutableSitemadenomina.bat
├── iniciar.vbs
├── .gitignore
└── README.md
```

---

## Instalación y ejecución

### Requisitos previos

Antes de ejecutar el proyecto, asegúrate de disponer de:

- Windows.
- Java Development Kit (JDK) 11 o compatible con el controlador JDBC incluido.
- Microsoft Azure SQL Database configurada.
- Acceso de red a la base de datos.
- Biblioteca JDBC de Microsoft SQL Server incluida en `src/lib/`.

### 1. Clonar el repositorio

```bash
git clone https://github.com/peccgh/sistema-nomina-java.git
```

### 2. Entrar al directorio

```bash
cd sistema-nomina-java
```

### 3. Configurar la conexión a la base de datos

Abre:

```text
src/utils/ConexionBD.java
```

Configura los parámetros de conexión correspondientes a tu instancia de Azure SQL Database:

- Servidor.
- Nombre de la base de datos.
- Usuario.
- Contraseña.

**Importante:** La versión documentada utiliza credenciales definidas directamente en el código. Antes de ejecutar el proyecto con tus propios datos, configura las credenciales localmente. No publiques contraseñas ni información de acceso en GitHub.

La base de datos debe disponer de las tablas `Departamentos` y `Empleados`, y de un departamento con identificador `1`, utilizado por el servicio al registrar empleados.

### 4. Ejecutar la aplicación

Puedes abrir el proyecto en Visual Studio Code con las extensiones de Java correspondientes y ejecutar:

```text
src/Main.java
```

También se incluyen los siguientes archivos auxiliares para el entorno Windows:

- `Compilador.bat`
- `EjecutableSitemadenomina.bat`
- `iniciar.vbs`

El controlador JDBC se encuentra en:

```text
src/lib/mssql-jdbc-13.6.0.jre11.jar
```

---

## Interfaz de usuario

La aplicación cuenta con una interfaz gráfica personalizada que utiliza dibujo vectorial para construir sus componentes visuales.

La ventana principal incluye:

- Barra superior de navegación.
- Menú lateral de opciones.
- Tarjetas interactivas para horas extras, bonos y seguro.
- Campos de captura de información del empleado.
- Acciones de consulta y registro.
- Calculadora integrada.

El diseño utiliza una resolución de referencia de 1920 × 1080 píxeles y adapta proporcionalmente sus componentes al tamaño de la ventana.

---

## Alcance y mejoras futuras

El proyecto constituye una implementación funcional de los procesos básicos de gestión de empleados y cálculo de nómina. Sin embargo, existen oportunidades de ampliación:

- Externalizar las credenciales de conexión mediante variables de entorno.
- Incorporar gestión de departamentos desde la interfaz.
- Implementar un historial de nóminas por período.
- Generar comprobantes individuales y reportes generales en PDF.
- Incorporar autenticación de usuarios y permisos por roles.
- Completar el filtrado de la búsqueda de empleados.
- Implementar los topes de cotización de la TSS.
- Añadir el cálculo del Impuesto Sobre la Renta (ISR).

Estas mejoras permitirían evolucionar el proyecto hacia un sistema de gestión de nómina más completo.

---

## Contexto académico

| Información | Detalle |
|---|---|
| Proyecto | LASKT System — Sistema Automatizado de Gestión de Nómina |
| Institución | Universidad Autónoma de Santo Domingo (UASD) |
| Facultad | Facultad de Ciencias |
| Departamento | Informática |
| Asignatura | Programación II (INF514) |
| Grupo | Grupo #1 |
| Fecha | Noviembre de 2026 |

### Equipo de desarrollo

Proyecto desarrollado colaborativamente por estudiantes de la escuela de informatica de la Universidad Autónoma de Santo Domingo.

### Autoría y contribuciones

- Phillips Enmanuel Cordero Celestin
- Jose Vargas 
- Luis Manuel Carreno Ruiz
- Bryan Yovanny Kellys Pozo
- Daury Hernandez
- Merkidez Genao Padilla

---

## Repositorio

**GitHub:** https://github.com/peccgh/sistema-nomina-java

Desarrollado como proyecto académico para aplicar conceptos de programación orientada a objetos, desarrollo de interfaces gráficas, conexión a bases de datos y organización modular del software.

---

<p align="center">
  <strong>LASKT SYSTEM</strong>
  <br/>
  <em>Automatizando la gestión, optimizando el tiempo.</em>
</p>
