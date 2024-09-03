package com.yahyafati.springbootauthenticationscaffold.models.auth.role

import com.yahyafati.springbootauthenticationscaffold.models.auth.role._dto.CreateUpdateRoleDTO
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth/roles")
@Tag(name = "User Role", description = "Role Management API")
class RoleController(
    private val roleService: RoleService
) {

    @GetMapping("")
    fun getRoles(
        @ParameterObject
        pageable: Pageable
    ): Page<Role> {
        return roleService.findAll(pageable)
    }

    @GetMapping("/{id}")
    fun getRole(
        @PathVariable
        id: Long
    ): Role {
        return roleService.findById(id)
    }

    @PostMapping("")
    fun createRole(
        @RequestBody
        @Validated
        createRole: CreateUpdateRoleDTO
    ): Role {
        return roleService.saveNew(createRole.toRole())
    }

    @PutMapping("/{id}")
    fun updateRole(
        @PathVariable
        id: Long,
        @RequestBody
        @Validated
        updateRole: CreateUpdateRoleDTO
    ): Role {
        return roleService.update(id, updateRole.toRole())
    }

    @DeleteMapping("/{id}")
    fun deleteRole(
        @PathVariable
        id: Long
    ) {
        roleService.delete(id)
    }
}