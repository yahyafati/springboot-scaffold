package com.yahyafati.springbootauthenticationscaffold.models.base

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.sql.Timestamp
import java.time.Instant


@MappedSuperclass
abstract class EntityModel : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0L

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    open var createdAt: Timestamp = Timestamp.from(Instant.now())

    @Column(name = "created_by_id", nullable = true)
    open var createdById: Long? = null

    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    open var updatedAt: Timestamp = Timestamp.from(Instant.now())

    @Column(name = "updated_by_id", nullable = true)
    open var updatedById: Long? = null
}