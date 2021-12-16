package capture.the.sequence.persistence;

import capture.the.sequence.model.PriceEntity;
import capture.the.sequence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long> {


    @Modifying
    @Query("delete from PriceEntity PE where PE.itemName = :itemName and PE.user = :user")
    void deleteDuplicatePriceItem(@Param("itemName") String itemName, @Param("user") UserEntity user);

    @Query("select PE from PriceEntity PE where PE.itemName = :itemName and PE.user = :user")
    List<PriceEntity> findAllByItemName(@Param("itemName") String itemName, @Param("user") UserEntity user);

    @Query("select PE from PriceEntity PE where PE.user = :user")
    List<PriceEntity> findAllByUser(@Param("user") UserEntity user);

    @Query("select PE from PriceEntity PE where PE.user = :user and PE.itemName = :item order by PE.marketDate")
    List<PriceEntity> getCalculationMaterial(@Param("user") UserEntity user, @Param("item") String item);

}
