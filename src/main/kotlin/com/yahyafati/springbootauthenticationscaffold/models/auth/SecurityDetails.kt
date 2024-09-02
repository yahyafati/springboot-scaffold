package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.models.auth.permission.userpermission.PermissionType
import com.yahyafati.springbootauthenticationscaffold.models.auth.permission.userpermission.UserPermission
import com.yahyafati.springbootauthenticationscaffold.models.auth.role.Role
import jakarta.persistence.Transient
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

interface SecurityDetails : UserDetails {


    var permissions: MutableSet<UserPermission>
    var role: Role

    @Transient
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val permissions = permissions
            .filter { it.type != PermissionType.DENY }
            .map { SimpleGrantedAuthority(it.permission.name) }
            .toMutableList()
        permissions.add(SimpleGrantedAuthority("ROLE_${role.properRoleName}"))
        return permissions
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

}