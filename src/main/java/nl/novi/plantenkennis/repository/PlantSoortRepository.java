package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.PlantSoort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PlantSoortRepository extends JpaRepository<PlantSoort, Long> {

    boolean existsByNederlandseNaamIgnoreCase(String nederlandseNaam);

    /**
     * Zoeken/filteren op kolommen van plant_soorten.
     * Let op: hier géén LOWER() om issues zoals lower(bytea) te vermijden.
     * (Case-sensitive; desgewenst later uitbreiden naar case-insensitive.)
     */
    @Query("""
    SELECT ps
    FROM PlantSoort ps
    WHERE
        (:q IS NULL OR
            ps.nederlandseNaam LIKE CONCAT('%', :q, '%')
            OR ps.wetenschappelijkeNaam LIKE CONCAT('%', :q, '%')
            OR ps.beschrijving LIKE CONCAT('%', :q, '%')
        )
        AND (:familie IS NULL OR ps.familie = :familie)
        AND (:giftig IS NULL OR ps.giftig = :giftig)
        AND (:inheems IS NULL OR ps.inheems = :inheems)
        AND (:onderhoudsniveau IS NULL OR ps.onderhoudsniveau = :onderhoudsniveau)
        AND (:slug IS NULL OR ps.slug = :slug)
        AND (
            :bloeimaand IS NULL OR
            (
                ps.bloeiperiodeStart IS NOT NULL
                AND ps.bloeiperiodeEinde IS NOT NULL
                AND :bloeimaand BETWEEN ps.bloeiperiodeStart AND ps.bloeiperiodeEinde
            )
        )
        AND (
            CAST(:updatedAfter AS timestamp) IS NULL OR ps.updatedAt >= :updatedAfter
        )
        AND (
            CAST(:updatedBefore AS timestamp) IS NULL OR ps.updatedAt <= :updatedBefore
        )
        """)
    Page<PlantSoort> searchTableFields(
            @Param("q") String q,
            @Param("familie") String familie,
            @Param("giftig") Boolean giftig,
            @Param("inheems") Boolean inheems,
            @Param("onderhoudsniveau") String onderhoudsniveau,
            @Param("slug") String slug,
            @Param("bloeimaand") Integer bloeimaand,
            @Param("updatedAfter") LocalDateTime updatedAfter,
            @Param("updatedBefore") LocalDateTime updatedBefore,
            Pageable pageable
    );

}
