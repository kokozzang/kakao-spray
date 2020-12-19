package com.wspark.kakaospray.common.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
@Getter
public abstract class AuditModel {

  // 생성 시각
  @Column(name = "create_at", nullable = false, updatable = false)
  @CreatedDate
  protected LocalDateTime createdAt;

  // 최종 수정된 시각
  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  protected LocalDateTime updatedAt;

}
