package ru.tigran.web_editor.database.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.tigran.web_editor.database.repository.UserRepository

@Service
class UserService : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("Username is null")
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found")
    }
}