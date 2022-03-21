package ru.tigran.web_editor.database.entity

import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet
import kotlin.jvm.Transient


@Entity
@Table(name = "users")
class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    private lateinit var username: String

    constructor()
    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

    override fun getUsername(): String {
        return username
    }

    private lateinit var password: String
    override fun getPassword(): String {
        return password
    }

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.REMOVE], fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
    var roles: Set<Role> = HashSet(Collections.singleton(Role()))

    @Transient
    lateinit var confirmPassword: String

    fun getId(): Long? {
        return id
    }

    override fun getAuthorities(): Set<Role?> {
        return roles
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}