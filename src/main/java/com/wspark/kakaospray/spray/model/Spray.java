package com.wspark.kakaospray.spray.model;

import com.wspark.kakaospray.common.model.AuditModel;
import com.wspark.kakaospray.common.util.IntegerUtil;
import com.wspark.kakaospray.common.util.StringUtil;
import com.wspark.kakaospray.spray.dto.SprayCreateRequest;
import com.wspark.kakaospray.spray.exception.AccessByOwnerException;
import com.wspark.kakaospray.spray.exception.AllPrizeMoneyPaidException;
import com.wspark.kakaospray.spray.exception.AlreadyPaidException;
import com.wspark.kakaospray.spray.exception.InvalidRoomException;
import com.wspark.kakaospray.spray.exception.SprayExpiredException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "spray")
@Getter
@ToString(exclude = "sprayItems")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spray extends AuditModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sprayId;

  // 토큰
  @Column(name = "token", unique = true, nullable = false, updatable = false)
  private String token;

  // 뿌린 유저 아이디
  @Column(name = "userId", nullable = false, updatable = false)
  private Integer userId;

  // 대화방 아이디
  @Column(name = "roomId", nullable = false, updatable = false)
  private String roomId;

  // 뿌린 금액
  @Column(name = "budget", nullable = false, updatable = false)
  private Integer budget;

  @Column(name = "sprayItemCount", nullable = false, updatable = false)
  private Integer sprayItemCount;

  // 뿌리기 내역
  @OneToMany(mappedBy = "spray", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
  private List<SprayItem> sprayItems = new ArrayList<>();


  @Builder(access = AccessLevel.PRIVATE)
  private Spray(int userId, String roomId, int budget, String token, Integer sprayItemCount) {
    this.token = token;
    this.userId = userId;
    this.roomId = roomId;
    this.budget = budget;
    this.sprayItemCount = sprayItemCount;
    this.sprayItems = createSprayItems();
  }

  public static Spray of(Integer userId, String roomId, SprayCreateRequest sprayCreateRequest) {
    return Spray.builder()
        .userId(userId)
        .roomId(roomId)
        .token(StringUtil.generateToken())
        .budget(sprayCreateRequest.getBudget())
        .sprayItemCount(sprayCreateRequest.getNumberOfPeople())
        .build();
  }

  private List<SprayItem> createSprayItems() {
    List<SprayItem> sprayItems = new ArrayList<>();

    int[] randomizedPrizeMoney = IntegerUtil.divideBy(budget, sprayItemCount);

    for (int prizeMoney : randomizedPrizeMoney) {
      SprayItem sprayItem = SprayItem.of(this, prizeMoney);
      sprayItems.add(sprayItem);
    }

    return sprayItems;
  }

  /**
   * 상금 지급 하기
   * @param userId
   * @param roomId
   * @return
   */
  public SprayItem payPrizeMoney(Integer userId, String roomId) {
    validatePrizeMoneyCanPaid(userId, roomId);

    Optional<SprayItem> itemOptional = sprayItems.stream()
        .filter(SprayItem::isNotPaid)
        .findFirst();

    SprayItem sprayItem = itemOptional.orElseThrow(AllPrizeMoneyPaidException::new);
    sprayItem.pay(userId);

    return sprayItem;
  }

  /**
   * 뿌리기 받기가 가능한지 밸리데이션 한다.
   * @param requestUserId 뿌리기 받기 요청자의 사용자 아이디
   * @param roomId
   */
  private void validatePrizeMoneyCanPaid(Integer requestUserId, String roomId) {
    verifyIsNotAccessByOwner(requestUserId);
    verifyExpired();
    verifyRoomId(roomId);
    verifyIsPaidUser(requestUserId);
  }

  private void verifyIsNotAccessByOwner(Integer requestUserId) {
    if (this.userId.equals(requestUserId)) {
      throw new AccessByOwnerException();
    }
  }

  private void verifyRoomId(String roomId) {
    if (!this.roomId.equals(roomId)) {
      throw new InvalidRoomException();
    }
  }

  private void verifyExpired() {
    LocalDateTime endDateTime = this.createdAt.plusMinutes(10);
    if (!LocalDateTime.now().isBefore(endDateTime)) {
      throw new SprayExpiredException();
    }
  }

  private void verifyIsPaidUser(Integer requestUserId) {
    for (SprayItem sprayItem: this.sprayItems) {
      if (requestUserId.equals(sprayItem.getUserId())) {
        throw new AlreadyPaidException();
      }
    }
  }
}


