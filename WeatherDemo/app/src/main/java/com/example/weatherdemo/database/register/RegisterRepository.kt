package com.example.weatherdemo.database.register

class RegisterRepository(private val dao: RegisterDatabaseDAO) {

    val users = dao.getAllUsers()

    suspend fun insert(user: RegisterEntity) {
        return dao.insert(user)
    }

    suspend fun getUserName(userName: String): RegisterEntity? {
        return dao.getUsername(userName)
    }
}