package com.yahyafati.springbootauthenticationscaffold.models.auth.permission.userpermission

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import com.yahyafati.springbootauthenticationscaffold.models.auth.permission.Permission
import jakarta.persistence.*
import java.io.Serializable


@Entity
@Table(name = "user_permissions")
class UserPermission(
    @EmbeddedId
    var id: UserPermissionKey = UserPermissionKey(),

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    var authUser: AuthUser = AuthUser(),
    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY)
    var permission: Permission = Permission(),
    @Enumerated(EnumType.STRING)
    var type: PermissionType = PermissionType.INHERIT
) {

    @Embeddable
    class UserPermissionKey(
        var userId: Long = 0,
        var permissionId: Long = 0
    ) : Serializable {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UserPermissionKey

            if (userId != other.userId) return false
            if (permissionId != other.permissionId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = userId.hashCode()
            result = 31 * result + permissionId.hashCode()
            return result
        }
    }


    override fun toString(): String {
        return "UserPermission(id=$id, user=$authUser, permission=$permission, type=$type)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPermission

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
