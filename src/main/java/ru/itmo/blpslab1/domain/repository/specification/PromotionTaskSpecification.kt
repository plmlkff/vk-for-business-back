package ru.itmo.blpslab1.domain.repository.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import ru.itmo.blpslab1.domain.entity.PromotionTask
import ru.itmo.blpslab1.rest.dto.request.query.PromotionTaskQuery
import ru.itmo.blpslab1.utils.core.isNotEmpty
import java.util.UUID

class PromotionTaskSpecification(
    private val promotionTaskQuery: PromotionTaskQuery
): Specification<PromotionTask> {

    override fun toPredicate(
        root: Root<PromotionTask>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        var resPredicate = criteriaBuilder.and()
        if (isNotEmpty(promotionTaskQuery.ids)){
            val idsPredicate = root.get<UUID>(PromotionTask.Fields.ID).`in`(promotionTaskQuery.ids)
            resPredicate = criteriaBuilder.and(resPredicate, idsPredicate)
        }
        if (promotionTaskQuery.isApproved != null){
            val isApprovedPredicate = criteriaBuilder.equal(root.get<Boolean>(PromotionTask.Fields.IS_APPROVED), promotionTaskQuery.isApproved)
            resPredicate = criteriaBuilder.and(resPredicate, isApprovedPredicate)
        }
        if (isNotEmpty(promotionTaskQuery.promotionTypes)){
            val promotionTypesPredicate = root.get<String>(PromotionTask.Fields.PROMOTION_TYPE).`in`(promotionTaskQuery.promotionTypes!!.map { it.name })
            resPredicate = criteriaBuilder.and(resPredicate, promotionTypesPredicate)
        }
        return resPredicate
    }
}