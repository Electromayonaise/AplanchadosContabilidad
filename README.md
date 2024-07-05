<img src="https://user-images.githubusercontent.com/73097560/115834477-dbab4500-a447-11eb-908a-139a6edaec5c.gif">

<div align="center">
  <img src="https://github.com/Electromayonaise/Electromayonaise/blob/main/Assets/github-contribution-grid-snake%20blacktest(1).svg" alt="snake" />
</div>
<div id="user-content-toc">

# AplanchadosConAmor

AplanchadosConAmor es un sistema de gestión y registro de ventas que permite registrar transacciones y generar resúmenes diarios. También permite cargar transacciones desde archivos Excel.

## Características

- Registro de transacciones
- Visualización de transacciones registradas
- Registro de resúmenes diarios
- Visualización de resúmenes registrados
- Carga de transacciones desde archivos Excel

## Instalación

1. Clonar el repositorio
2. Instalar dependencias:
    ```
    npm install
    ```
3. Configurar las variables de entorno en un archivo `.env`:
    ```
    MONGO_URI=mongodb://localhost:27017/aplanchadosconamor
    PORT=3000
    ```
4. Iniciar la aplicación:
    ```
    npm start
    ```

## Uso

Abrir el navegador y navegar a `localhost:3000` para acceder a la interfaz de usuario.

## Estructura de carpetas del proyecto:

```bash

aplanchadosconamor/
│
├── controllers/
│   ├── transactionController.js    # Controlador para manejar operaciones relacionadas con transacciones
│   └── summaryController.js        # Controlador para manejar operaciones relacionadas con resúmenes
│
├── models/
│   ├── transaction.js              # Modelo de datos para transacciones
│   └── summary.js                  # Modelo de datos para resúmenes
│
├── routes/
│   ├── transactions.js             # Rutas para las operaciones CRUD de transacciones
│   ├── summaries.js                # Rutas para las operaciones CRUD de resúmenes
│   └── upload.js                   # Rutas para la subida de archivos Excel
│
├── services/
│   ├── transactionService.js       # Servicios para la lógica de negocio relacionada con transacciones
│   └── excelService.js             # Servicios para manejar la lógica relacionada con archivos Excel
│
├── utils/
│   └── db.js                       # Configuración y conexión a la base de datos
│
├── views/
│   ├── index.html                  # Página principal del frontend
│   ├── transactions.html           # Página para gestionar transacciones
│   ├── summaries.html              # Página para gestionar resúmenes
│   └── upload.html                 # Página para subir archivos Excel
│
├── public/
│   ├── css/
│   │   └── styles.css              # Archivo de estilos CSS
│   ├── js/
│   │   ├── index.js                # Lógica frontend para la página principal
│   │   ├── transactions.js         # Lógica frontend para gestionar transacciones
│   │   ├── summaries.js            # Lógica frontend para gestionar resúmenes
│   │   └── upload.js               # Lógica frontend para subir archivos Excel
│   └── images/                     # Directorio para imágenes (vacío por ahora)
│
├── uploads/                        # Directorio para almacenar archivos subidos
│
├── .env                            # Archivo de configuración de entorno (variables de entorno)
├── .gitignore                      # Archivo para ignorar archivos y directorios en git
├── app.js                          # Archivo principal de la aplicación (configuración del servidor)
├── package.json                    # Archivo de configuración del proyecto y dependencias de npm
└── README.md                       # Archivo README con la descripción del proyecto

```

<img src="https://user-images.githubusercontent.com/73097560/115834477-dbab4500-a447-11eb-908a-139a6edaec5c.gif">