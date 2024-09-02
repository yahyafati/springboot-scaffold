package com.yahyafati.springbootauthenticationscaffold.repo.base

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface IRepoSpecification<T, ID> : JpaRepository<T, ID>, JpaSpecificationExecutor<T> {}