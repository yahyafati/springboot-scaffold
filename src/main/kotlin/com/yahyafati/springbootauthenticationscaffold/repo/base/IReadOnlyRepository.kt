package com.yahyafati.springbootauthenticationscaffold.repo.base

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.util.*

@NoRepositoryBean
interface IReadOnlyRepository<T, ID> : Repository<T, ID> {

    fun findAll(): List<T>
    fun findAll(pageable: Pageable): Page<T>
    fun findById(id: ID): Optional<T>
    fun existsById(id: ID): Boolean
    fun count(): Long
}