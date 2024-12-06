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

        if (loanAmount == null || salary == null) {
            return true;
        }

        BigDecimal requiredAmount = salary.multiply(new BigDecimal(24));

        if (loanAmount.compareTo(requiredAmount) >= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Сумма займа должна быть меньше 24 зарплат")
                    .addPropertyNode("amount")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
