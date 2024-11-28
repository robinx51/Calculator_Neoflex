package MS_calculator.Annotations;

import MS_calculator.DTO.ScoringDataDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class LoanAmountValidator implements ConstraintValidator<LoanAmountMoreThanSalaries, ScoringDataDto> {
    @Override
    public boolean isValid(ScoringDataDto loanRequestDto, ConstraintValidatorContext context) {
        BigDecimal loanAmount = loanRequestDto.getAmount();
        BigDecimal salary = loanRequestDto.getEmployment().getSalary();

        BigDecimal requiredAmount = salary.multiply(new BigDecimal(24));

        return loanAmount.compareTo(requiredAmount) > 0;
    }
}
