package com.yahyafati.springbootauthenticationscaffold.services

import com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade.IAuthenticationFacade
import com.yahyafati.springbootauthenticationscaffold.exceptions.NoEntityFoundException
import com.yahyafati.springbootauthenticationscaffold.models.base.EntityModel
import com.yahyafati.springbootauthenticationscaffold.repo.ISpecification
import com.yahyafati.springbootauthenticationscaffold.repo.base.IModelRepoSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1


interface IModelService<T : EntityModel> {

    val repository: IModelRepoSpecification<T, Long>
    val modelProperties: Collection<KProperty1<T, *>>
    val specification: ISpecification<T>
        get() = object : ISpecification<T> {}
    val authenticationFacade: IAuthenticationFacade

    val entityName: String
        get() {
            val name = this::class.simpleName ?: "Entity"

            if (name.endsWith("Service")) {
                return name.substring(0, name.length - 7)
            } else if (name.endsWith("Services")) {
                return name.substring(0, name.length - 8)
            }
            return name
        }


//    fun findAll(pageable: Pageable, filters: List<List<FilterSpecification>>): Page<T> {
//        return repository.findAll(specification.columnEqualsOr(filters), pageable)
//    }

    fun saveNew(entity: T): T {
        entity.id = 0
        val saved = repository.save(entity)
        return saved
    }


    fun findAll(pageable: Pageable): Page<T> {
        return repository.findAll(pageable)
    }

    fun findAll(): List<T> {
        return repository.findAll()
    }

    fun findById(id: Long): T {
        return repository
            .findById(id)
            .orElseThrow { NoEntityFoundException.create(entityName, id) }
    }

    fun save(entity: T): T {
        val currentUser = authenticationFacade.forcedCurrentUser
        entity.updatedById = currentUser.id
        if (entity.id == 0L) {
            entity.createdById = currentUser.id
        }
        return repository.save(entity)
    }

    fun update(entity: T, id: Long, updatedFields: Set<String> = emptySet()): T {
        val existingEntity: Optional<T> = repository.findById(id)
        if (existingEntity.isEmpty) throw NoEntityFoundException.create(entityName, id)
        entity.id = id

        if (updatedFields.isEmpty()) {
            return save(entity)
        }

        modelProperties.forEach { property ->
            if (updatedFields.contains(property.name)) {
                if (property is KMutableProperty<*>) {
                    property.setter.call(existingEntity.get(), property.get(entity))
                }
            }
        }

        return save(existingEntity.get())
    }

    @Transactional
    fun delete(id: Long) {
        repository.deleteById(id)
    }

    @Transactional
    fun delete(ids: Collection<Long>) {
        repository.deleteAllById(ids)
    }

}