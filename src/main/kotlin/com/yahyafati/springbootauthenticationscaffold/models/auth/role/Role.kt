package com.yahyafati.springbootauthenticationscaffold.models.auth.role

import com.yahyafati.springbootauthenticationscaffold.models.auth.permission.Permission
import com.yahyafati.springbootauthenticationscaffold.models.base.EntityModel
import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(
    override var id: Long = 0,
    @Column(unique = true, nullable = false)
    var name: String = "",

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: MutableSet<Permission> = mutableSetOf()
) : EntityModel() {
    val properRoleName: String
        get() = name.removePrefix("ROLE_")

    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}