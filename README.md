# UserApp

UserApp es una aplicación construida con Jetpack Compose que permite gestionar usuarios. Los usuarios pueden ser registrados manualmente o generados aleatoriamente, además de poder ser modificados o eliminados. 

## Requisitos

- Android Studio (Electric Eel o superior)
- Kotlin 1.5 o superior
- Jetpack Compose

## Estructura del Proyecto

- **UserApp**: Componente principal que gestiona la UI y la lógica de la aplicación.
- **UserRepository**: Interface que define las operaciones CRUD para los usuarios.
- **User**: Modelo que representa a un usuario, incluyendo su nombre, apellido y edad.
- **Funciones auxiliares**: Incluye funciones para generar nombres y apellidos aleatorios.

## Funcionalidades

1. **Registrar Usuario Manualmente**: Permite ingresar el nombre, apellido y edad de un usuario.
2. **Registrar Usuarios Aleatorios**: Genera usuarios con nombres y apellidos aleatorios y edades en un rango específico.
3. **Modificar Usuarios**: Permite actualizar los detalles de un usuario seleccionado.
4. **Eliminar Usuarios**: Opción para eliminar un usuario seleccionado o eliminar todos los usuarios.
5. **Validaciones**: Se realizan validaciones en los campos de entrada para asegurar que los datos son correctos.

## Uso

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu_usuario/UserApp.git

