package com.yahyafati.springbootauthenticationscaffold.models.auth.permission.userpermission

import com.yahyafati.springbootauthenticationscaffold.repo.base.IRepoSpecification

interface UserPermissionRepository : IRepoSpecification<UserPermission, UserPermission.UserPermissionKey> {
}