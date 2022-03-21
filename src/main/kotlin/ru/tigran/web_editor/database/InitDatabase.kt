package ru.tigran.web_editor.database

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.tigran.web_editor.database.entity.Role
import ru.tigran.web_editor.database.entity.User
import ru.tigran.web_editor.database.repository.RoleRepository
import ru.tigran.web_editor.database.repository.UserRepository

@Component
class InitDatabase {
    @Autowired
    lateinit var roleRepository: RoleRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @EventListener
    fun appReady(event: ApplicationReadyEvent) {
        if (roleRepository.count() == 0L){
            roleRepository.save(Role("ROLE_USER"))
            roleRepository.save(Role("ROLE_ADMIN"))
        }
        if (userRepository.count() == 0L){
            val user = User("user", passwordEncoder.encode("root"))
            val roles = roleRepository.findAll()
            user.roles = HashSet(roles)
            userRepository.save(user)
        }
    }
}