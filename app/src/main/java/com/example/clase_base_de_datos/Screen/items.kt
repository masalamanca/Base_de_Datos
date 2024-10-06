package com.example.clase_base_de_datos.Screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.clase_base_de_datos.Model.User
import com.example.clase_base_de_datos.Repository.UserRepository
import kotlinx.coroutines.launch


@Composable
fun UserApp(userRepository: UserRepository) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRegisterDialog by remember { mutableStateOf(false) }
    var numUsersToRegister by remember { mutableStateOf(1) }

    // Observa la lista de usuarios
    val users by userRepository.getAllUsers().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                SnackbarHost(snackbarHostState, modifier = Modifier.align(Alignment.TopCenter))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Campos de entrada
                TextField(
                    value = nombre,
                    onValueChange = {
                        // Manejo de excepciones y restricciones
                        if (it.all { char -> char.isLetter() || char.isWhitespace() }) {
                            nombre = it
                        }
                    },
                    label = { Text(text = "Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = apellido,
                    onValueChange = {
                        if (it.all { char -> char.isLetter() || char.isWhitespace() }) {
                            apellido = it
                        }
                    },
                    label = { Text(text = "Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = edad,
                    onValueChange = {
                        // Manejo de excepciones y restricciones
                        if (it.isEmpty() || (it.toIntOrNull()?.let { edadVal -> edadVal in 0..130 } == true)) {
                            edad = it
                        }
                    },
                    label = { Text(text = "Edad") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para registrar usuarios manualmente
                Button(
                    onClick = {
                        if (nombre.isNotEmpty() && apellido.isNotEmpty() && edad.isNotEmpty()) {
                            scope.launch {
                                val user = User(
                                    nombre = nombre,
                                    apellido = apellido,
                                    edad = edad.toIntOrNull() ?: 0
                                )

                                // Verificar si el usuario ya existe
                                if (userRepository.userExists(user)) {
                                    snackbarHostState.showSnackbar("Usuario ya registrado")
                                } else {
                                    userRepository.insert(user)
                                    snackbarHostState.showSnackbar("Usuario ${nombre} ${apellido} registrado")
                                    // Resetea los campos
                                    nombre = ""
                                    apellido = ""
                                    edad = ""
                                }
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Por favor, completa todos los campos")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4), Color.White),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(text = "Registrar Usuario Manual")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para registrar usuarios aleatorios
                Button(
                    onClick = { showRegisterDialog = true },
                    colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4), Color.White),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(text = "Registrar Usuarios Aleatorios")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para modificar
                Button(
                    onClick = {
                        selectedUser?.let { user ->
                            scope.launch {
                                userRepository.update(user.copy(
                                    nombre = nombre,
                                    apellido = apellido,
                                    edad = edad.toIntOrNull() ?: user.edad
                                ))
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4), Color.White),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(text = "Modificar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para eliminar
                Button(
                    onClick = {
                        showDeleteDialog = true // Mostrar diálogo de eliminación
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4), Color.White),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(text = "Eliminar")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Muestra la lista de usuarios
            items(users) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedUser = user
                            nombre = user.nombre
                            apellido = user.apellido
                            edad = user.edad.toString()
                        }
                        .background(if (selectedUser == user) Color.LightGray else Color.Transparent)
                        .padding(8.dp)
                ) {
                    Text("${user.nombre} ${user.apellido}, Age: ${user.edad}",
                        color = if (selectedUser == user) Color.Black else Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

    // Diálogo de registro
    if (showRegisterDialog) {
        AlertDialog(
            onDismissRequest = { showRegisterDialog = false },
            title = { Text("Registrar Usuarios Aleatorios", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¿Cuántos usuarios deseas registrar?")
                    TextField(
                        value = numUsersToRegister.toString(),
                        onValueChange = {
                            numUsersToRegister = it.toIntOrNull() ?: 1
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            // Registrar usuarios aleatorios
                            for (i in 1..numUsersToRegister) {
                                val randomNombre = generateRandomName()
                                val randomApellido = generateRandomSurname()
                                val randomEdad = (1..130).random()

                                val user = User(
                                    nombre = randomNombre,
                                    apellido = randomApellido,
                                    edad = randomEdad
                                )

                                // Verificar si el usuario ya existe
                                if (userRepository.userExists(user)) {
                                    snackbarHostState.showSnackbar("Usuario ${randomNombre} ${randomApellido} ya registrado")
                                } else {
                                    userRepository.insert(user)
                                    snackbarHostState.showSnackbar("Usuario ${randomNombre} ${randomApellido} registrado")
                                }
                            }
                        }
                        showRegisterDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4), Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Usuarios Aleatorios")
                }
            },
            dismissButton = null // No hay botón de cancelar en este caso
        )
    }

    // Diálogo de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Usuario", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¿Qué deseas hacer?", modifier = Modifier.padding(bottom = 8.dp))
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            selectedUser?.let { user ->
                                scope.launch {
                                    userRepository.delete(user) // Eliminar usuario seleccionado
                                    snackbarHostState.showSnackbar("Usuario eliminado")
                                    selectedUser = null
                                    nombre = ""
                                    apellido = ""
                                    edad = ""
                                }
                            }
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4), Color.White),
                        modifier = Modifier.fillMaxWidth().padding(8.dp) // Tamaño del botón
                    ) {
                        Text("Eliminar Seleccionado")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                userRepository.deleteAll() // Eliminar todos los usuarios
                                snackbarHostState.showSnackbar("Todos los usuarios eliminados")
                            }
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4), Color.White),
                        modifier = Modifier.fillMaxWidth().padding(8.dp) // Tamaño del botón
                    ) {
                        Text("Eliminar Todos")
                    }
                }
            },
            dismissButton = null // No hay botón de cancelar en este caso
        )
    }
}

// Funciones para generar nombres y apellidos aleatorios
fun generateRandomName(): String {
    val names = listOf("Juan", "María", "Pedro", "Laura", "Carlos", "Ana", "Luis", "Marta", "Jorge", "Sofía")
    return names.random()
}

fun generateRandomSurname(): String {
    val surnames = listOf("García", "Martínez", "López", "Pérez", "Sánchez", "Rodríguez", "Gómez", "Fernández", "Díaz", "Morales")
    return surnames.random()
}
