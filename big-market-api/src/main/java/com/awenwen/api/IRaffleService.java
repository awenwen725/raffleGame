package com.awenwen.api;

import com.awenwen.api.dto.RaffleAwardListRequestDTO;
import com.awenwen.api.dto.RaffleAwardListResponseDTO;
import com.awenwen.api.dto.RaffleRequestDTO;
import com.awenwen.api.dto.RaffleResponseDTO;
import com.awenwen.types.model.Response;

import java.util.List;

/**
 * @author awenwen
 * @description interface of raffle service
 * @create 2025/12/13 15:22
 */
public interface IRaffleService {
    /**
     * assemble strategy according to ID
     * @param strategyId strategy ID
     * @return assembly result
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * query Award list according to strategy
     * @param requestDTO request DTO
     * @return Award list according to strategy
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     * perform raffle
     * @param requestDTO request DTO
     * @return award information
     */
    Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO);
}
