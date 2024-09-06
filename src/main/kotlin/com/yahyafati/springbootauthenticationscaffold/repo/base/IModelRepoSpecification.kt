package com.yahyafati.springbootauthenticationscaffold.repo.base

import com.yahyafati.springbootauthenticationscaffold.models.base.EntityModel
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface IModelRepoSpecification<T : EntityModel> : IRepoSpecification<T, Long> {

}