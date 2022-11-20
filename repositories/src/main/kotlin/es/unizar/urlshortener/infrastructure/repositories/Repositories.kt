package es.unizar.urlshortener.infrastructure.repositories

import es.unizar.urlshortener.core.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

/**
 * Specification of the repository of [ShortUrlEntity].
 *
 * **Note**: Spring Boot is able to discover this [JpaRepository] without further configuration.
 */
interface ShortUrlEntityRepository : JpaRepository<ShortUrlEntity, String> {
    fun findByHash(hash: String): ShortUrlEntity?

    @Transactional
    fun deleteByHash(hash: String)

    @Transactional
    @Modifying
    //@Query("UPDATE ShortUrlEntity SET validation = ?2 where hash = ?1")
    @Query(value = "update ShortUrlEntity u set u.validation = ?2 where u.hash = ?1")
    fun updateValidateByHash(hash: String, status: ValidateUrlState): Int
}

/**
 * Specification of the repository of [ClickEntity].
 *
 * **Note**: Spring Boot is able to discover this [JpaRepository] without further configuration.
 */
interface ClickEntityRepository : JpaRepository<ClickEntity, Long> {

    fun existsByHash(hash: String): Boolean

    fun findAllByHash(hash: String): List<ClickEntity>

}