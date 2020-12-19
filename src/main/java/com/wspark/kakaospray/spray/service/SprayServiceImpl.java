package com.wspark.kakaospray.spray.service;

import com.wspark.kakaospray.spray.dto.PrizeMoneyResponse;
import com.wspark.kakaospray.spray.dto.SprayCreateRequest;
import com.wspark.kakaospray.spray.dto.SprayCreateResponse;
import com.wspark.kakaospray.spray.dto.SprayStatusResponse;
import com.wspark.kakaospray.spray.exception.InvalidTokenException;
import com.wspark.kakaospray.spray.exception.NotSprayOwnerException;
import com.wspark.kakaospray.spray.exception.SprayStatusExpiredException;
import com.wspark.kakaospray.spray.model.Spray;
import com.wspark.kakaospray.spray.model.SprayItem;
import com.wspark.kakaospray.spray.repository.SprayRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SprayServiceImpl implements SprayService {

  private final SprayRepository sprayRepository;


  @Transactional
  @Override
  public SprayCreateResponse createSpray(Integer userId, String roomId, SprayCreateRequest sprayCreateRequest) {
    Spray spray = buildSpray(userId, roomId, sprayCreateRequest);

    Spray savedSpray = sprayRepository.save(spray);

    return new SprayCreateResponse(savedSpray.getToken());
  }

  private Spray buildSpray(Integer userId, String roomId, SprayCreateRequest sprayCreateRequest) {
    Spray spray = null;

    boolean isValidToken = false;
    while(!isValidToken) {
      spray = Spray.of(userId, roomId, sprayCreateRequest);

      Optional<Spray> optionalSpray = sprayRepository.findByToken(spray.getToken());
      if (optionalSpray.isEmpty()) {
        isValidToken = true;
      }
    }

    return spray;
  }

  @Transactional
  @Override
  public PrizeMoneyResponse payPrizeMoney(String token, Integer userId, String roomId) {
    Optional<Spray> sprayOptional = sprayRepository.findByToken(token);
    Spray spray = sprayOptional.orElseThrow(InvalidTokenException::new);

    SprayItem sprayItem = spray.payPrizeMoney(userId, roomId);

    return new PrizeMoneyResponse(sprayItem.getPrizeMoney());
  }

  @Override
  public SprayStatusResponse getSprayStatus(String token, Integer userId) {
    Optional<Spray> sprayOptional = sprayRepository.findByTokenWithSprayItems(token);
    Spray spray = sprayOptional.orElseThrow(InvalidTokenException::new);

    if (!spray.getUserId().equals(userId)) {
      throw new NotSprayOwnerException();
    }

    LocalDateTime aWeekAfterCreated = spray.getCreatedAt().plusDays(7);
    if (LocalDateTime.now().isAfter(aWeekAfterCreated)) {
      throw new SprayStatusExpiredException();
    }

    return new SprayStatusResponse(spray);
  }
}
