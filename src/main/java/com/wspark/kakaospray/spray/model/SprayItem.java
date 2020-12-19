package com.wspark.kakaospray.spray.model;

import com.wspark.kakaospray.common.model.AuditModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "spray_item")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SprayItem extends AuditModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sprayItemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "spray_id", nullable = false, updatable = false)
  private Spray spray;

  // 지급액
  @Column(name = "prize_money", nullable = false, updatable = false)
  private int prizeMoney;

  // 지급액을 받은 유저 아이디
  @Column(name = "user_id", insertable = false)
  private Integer userId;

  // 버전
  @Version
  private long version;


  @Builder(access = AccessLevel.PRIVATE)
  private SprayItem(Spray spray, int prizeMoney) {
    this.spray = spray;
    this.prizeMoney = prizeMoney;
  }

  public static SprayItem of(Spray spray, int prizeMoney) {
    return SprayItem.builder()
        .prizeMoney(prizeMoney)
        .spray(spray)
        .build();
  }

  /**
   * 상금 지급 하기
   * @param userId
   */
  public void pay(int userId) {
    this.userId = userId;
  }

  public boolean isPaid() {
    return this.userId != null;
  }

  public boolean isNotPaid() {
    return this.userId == null;
  }
}
