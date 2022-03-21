package ru.tigran.web_editor.database.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.tigran.web_editor.database.entity.Role

@Repository
interface RoleRepository : JpaRepository<Role, Int> {
    fun findByName(name: String) : Role?
}