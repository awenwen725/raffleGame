package com.awenwen.trigger.http;

import com.alibaba.fastjson.JSON;
import com.awenwen.api.IRaffleService;
import com.awenwen.api.dto.RaffleAwardListRequestDTO;
import com.awenwen.api.dto.RaffleAwardListResponseDTO;
import com.awenwen.api.dto.RaffleRequestDTO;
import com.awenwen.api.dto.RaffleResponseDTO;
import com.awenwen.domain.strategy.model.entity.RaffleAwardEntity;
import com.awenwen.domain.strategy.model.entity.RaffleFactorEntity;
import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;
import com.awenwen.domain.strategy.service.IRaffleAward;
import com.awenwen.domain.strategy.service.IRaffleStrategy;
import com.awenwen.domain.strategy.service.armory.IStrategyArmory;
import com.awenwen.types.enums.ResponseCode;
import com.awenwen.types.exception.AppException;
import com.awenwen.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author awenwen
 * @description
 * @create 2025/12/13 15:42
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/")
public class RaffleController implements IRaffleService {
    /** constructor injection */
    private IRaffleAward raffleAward;

    private IRaffleStrategy raffleStrategy;

    private IStrategyArmory strategyArmory;

    public RaffleController(IRaffleAward raffleAward, IRaffleStrategy raffleStrategy, IStrategyArmory strategyArmory) {
        this.raffleAward = raffleAward;
        this.raffleStrategy = raffleStrategy;
        this.strategyArmory = strategyArmory;
    }

    /**
     * assemble strategy information into cache
     * <a href="http://localhost:8091/api/v1/raffle/strategy_armory">/api/v1/raffle/strategy_armory</a>
     * @param strategyId strategy ID
     * @return assembly result
     */
    @RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(Long strategyId) {
        try {
            log.info("Strategy Assemble start, strategyId：{}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("Strategy Assemble end, strategyId：{} response: {}", strategyId, JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.info("Strategy Assemble Error! strategyId：{} response: {}", strategyId, e.getMessage());
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * query Award list according to strategy
     * <a href="http://localhost:8091/api/v1/raffle/query_raffle_award_list">/api/v1/raffle/query_raffle_award_list</a>
     * @param requestDTO request DTO
     * @return award list
     */
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO) {
        try {
            log.info("Query Raffle Strategy Award List start, strategyId：{}", requestDTO.getStrategyId());
            List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardList(requestDTO.getStrategyId());
            List<RaffleAwardListResponseDTO> raffleAwardListResponseDTOS = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
                raffleAwardListResponseDTOS.add(RaffleAwardListResponseDTO.builder()
                        .awardId(strategyAward.getAwardId())
                        .awardTitle(strategyAward.getAwardTitle())
                        .awardSubtitle(strategyAward.getAwardSubtitle())
                        .sort(strategyAward.getSort())
                        .build());
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(raffleAwardListResponseDTOS)
                    .build();
            log.info("Query Raffle Strategy Award List end, strategyId：{}, response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.info("Query Raffle Strategy Award List Error! strategyId：{} response: {}", requestDTO.getStrategyId(), e.getMessage());
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * perform raffle
     * <a href="http://localhost:8091/api/v1/raffle/random_raffle">/api/v1/raffle/random_raffle</a>
     * @param requestDTO request DTO
     * @return award information
     */
    @Override
    public Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO) {
        try {
            log.info("Raffle process starts, strategyId：{}", requestDTO.getStrategyId());
            // user ID service not accomplished
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId("system")
                    .strategyId(requestDTO.getStrategyId())
                    .build());

            Response<RaffleResponseDTO> response = Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("Raffle process ends, strategyId：{}, response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        } catch (AppException e) {
            log.error("Raffle process Error, strategyId：{} {}", requestDTO.getStrategyId(), e.getInfo());
            return Response.<RaffleResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("Raffle process Error, strategyId：{}", requestDTO.getStrategyId(), e);
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
