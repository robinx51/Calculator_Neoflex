package MS_calculator.Services;

import MS_calculator.DTO.CreditDto;
import MS_calculator.DTO.ScoringDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalcService {
    private final ScoringService scoringService;

    public CreditDto generateCredit(ScoringDataDto request){
        return new CreditDto();
    }
}
