package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.models.auth.permission.userpermission.UserPermission
import com.yahyafati.springbootauthenticationscaffold.models.auth.role.Role
import com.yahyafati.springbootauthenticationscaffold.models.base.EntityModel
import jakarta.persistence.*

@Entity
class User(
    @Id
    override var id: Long = 0,
    private var username: String = "",
    private var password: String = "",
    private var enabled: Boolean = false
) : SecurityDetails, EntityModel() {

    @ManyToOne(fetch = FetchType.LAZY)
    override var role: Role = Role()
    override fun getPassword(): String {
        return password
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
    override var permissions: MutableSet<UserPermission> = mutableSetOf()

}