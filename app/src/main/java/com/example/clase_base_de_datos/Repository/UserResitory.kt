package com.example.clase_base_de_datos.Repository

import com.example.clase_base_de_datos.DAO.UserDao
import com.example.clase_base_de_datos.Model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    suspend fun deleteAll() {
        userDao.deleteAll() // Método para eliminar todos los usuarios
    }

    // Método para verificar si el usuario ya existe
    suspend fun userExists(user: User): Boolean {
        val users = userDao.getAllUsers().first() // Obtener la lista de usuarios
        return users.any { it.nombre == user.nombre && it.apellido == user.apellido && it.edad == user.edad }
    }
}
