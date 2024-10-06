package com.example.clase_base_de_datos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.clase_base_de_datos.DAO.UserDao
import com.example.clase_base_de_datos.Database.UserDatabase
import com.example.clase_base_de_datos.Repository.UserRepository
import com.example.clase_base_de_datos.Screen.UserApp


class MainActivity : ComponentActivity() {

    private lateinit var userDao: UserDao
    private lateinit var userRepository:UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var db = UserDatabase.getDatabase(applicationContext)
        userDao = db.UserDao()
        userRepository = UserRepository (userDao)

        enableEdgeToEdge()
        setContent {
            UserApp(userRepository)

                }
            }
        }

