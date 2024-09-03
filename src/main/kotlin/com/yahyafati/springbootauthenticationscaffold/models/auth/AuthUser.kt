package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.models.auth.permission.userpermission.UserPermission
import com.yahyafati.springbootauthenticationscaffold.models.auth.role.Role
import com.yahyafati.springbootauthenticationscaffold.models.base.EntityModel
import jakarta.persistence.*

@Entity
@Table(name = "users")
class AuthUser(
    @Column(unique = true, nullable = false, length = 50)
    private var username: String = "",
    @Column(nullable = false)
    private var password: String = "",
    @Column(nullable = false, length = 100)
    var email: String = "",
    private var enabled: Boolean = true
) : SecurityDetails, EntityModel() {

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    override var role: Role = Role()
    override fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    @OneToMany(mappedBy = "authUser", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
    override var permissions: MutableSet<UserPermission> = mutableSetOf()

}