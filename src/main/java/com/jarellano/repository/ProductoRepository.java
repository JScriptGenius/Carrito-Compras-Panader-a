package com.jarellano.repository;

import com.jarellano.entity.Producto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE productos SET cantidad = ?1 WHERE id_producto = ?2",
            nativeQuery = true
    )
    void actualizarStock(int cantidad, Integer id);
}