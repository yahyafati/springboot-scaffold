package com.yahyafati.springbootauthenticationscaffold.models.auth.permission

import com.yahyafati.springbootauthenticationscaffold.models.base.EntityModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "permissions")
class Permission(
    override var id: Long = 0,

    @Column(unique = true, nullable = false)
    var name: String = "",
    var description: String = ""
) : EntityModel() {


    override fun toString(): String {
        return "Permission(id=$id, name='$name', description='$description')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Permission

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }


}