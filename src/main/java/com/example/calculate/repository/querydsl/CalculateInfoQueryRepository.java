package com.example.calculate.repository.querydsl;

import com.example.calculate.base.code.CalculateStatusCd;
import com.example.calculate.base.model.CalculateResponseModel;
import com.example.calculate.entity.QCalculateDetailEntity;
import com.example.calculate.entity.QCalculateInfoEntity;
import com.example.calculate.utils.StringUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class CalculateInfoQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<CalculateResponseModel> findByDynamicQuery(Long id, Long userId) {
        QCalculateInfoEntity qCalculateInfo = QCalculateInfoEntity.calculateInfoEntity;
        QCalculateDetailEntity qCalculateDetail = QCalculateDetailEntity.calculateDetailEntity;
        return jpaQueryFactory.from(qCalculateInfo)
                .select(Projections.constructor(CalculateResponseModel.class,
                        qCalculateInfo.id.as("id"),
                        qCalculateInfo.userId.as("requestUserId"),
                        qCalculateInfo.totalAmount.as("totalAmount"),
                        qCalculateInfo.notifySetupAt.as("notifySetupAt"),
                        qCalculateInfo.status.as("mainStatus"),
                        qCalculateDetail.id.as("calculateDetailId"),
                        qCalculateDetail.senderUserId.as("senderUserId"),
                        qCalculateDetail.amount.as("amount"),
                        qCalculateDetail.status.as("status"))
                )
                .where(eqId(id, qCalculateInfo), eqUserId(userId, qCalculateInfo))
                .leftJoin(qCalculateDetail)
                .on(
                        qCalculateInfo.id.eq(qCalculateDetail.calculateInfoId)
                ).fetch();
    }


    private BooleanExpression eqId(Long id, QCalculateInfoEntity qCalculateInfo) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return qCalculateInfo.id.eq(id);
    }

    private BooleanExpression eqUserId(Long userId, QCalculateInfoEntity qCalculateInfo) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return qCalculateInfo.userId.eq(userId);
    }

    public List<CalculateResponseModel> findAllAndNotify() {
        QCalculateInfoEntity qCalculateInfo = QCalculateInfoEntity.calculateInfoEntity;
        QCalculateDetailEntity qCalculateDetail = QCalculateDetailEntity.calculateDetailEntity;
        return jpaQueryFactory.from(qCalculateInfo)
                .select(Projections.constructor(CalculateResponseModel.class,
                        qCalculateInfo.id.as("id"),
                        qCalculateInfo.userId.as("requestUserId"),
                        qCalculateInfo.totalAmount.as("totalAmount"),
                        qCalculateInfo.notifySetupAt.as("notifySetupAt"),
                        qCalculateDetail.id.as("calculateDetailId"),
                        qCalculateDetail.senderUserId.as("senderUserId"),
                        qCalculateDetail.amount.as("amount"),
                        qCalculateDetail.status.as("status"))
                )
                .where(
                        qCalculateInfo.notifySetupAt.before(LocalDateTime.now())
                                .and(qCalculateInfo.status.eq(CalculateStatusCd.ready.name()))
                )
                .leftJoin(qCalculateDetail)
                .on(
                        qCalculateInfo.id.eq(qCalculateDetail.calculateInfoId)
                ).fetch();
    }
}