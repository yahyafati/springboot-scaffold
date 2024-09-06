package com.yahyafati.springbootauthenticationscaffold.models.auth.provider

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import com.yahyafati.springbootauthenticationscaffold.models.base.EntityModel
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table
class OAuth2UserProvider(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override var id: Long = 0L,
    
    var providerId: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var providerType: EOAuth2Provider = EOAuth2Provider.GOOGLE,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: AuthUser = AuthUser(),

    @JdbcTypeCode(SqlTypes.JSON)
    @Lob
    @Column(name = "provider_data")
    var providerData: String = "",
) : EntityModel()