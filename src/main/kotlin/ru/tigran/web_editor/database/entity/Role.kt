package ru.tigran.web_editor.database.entity

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "roles")
class Role : GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int = 0
    lateinit var name : String

    @Transient
    @ManyToMany(mappedBy = "roles")
    lateinit var users : Set<User>

    constructor()
    constructor(name: String) {
        this.name = name
    }

    override fun getAuthority(): String {
        return name;
    }


}