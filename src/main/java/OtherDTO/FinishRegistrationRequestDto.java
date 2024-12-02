package OtherDTO;

import MS_calculator.DTO.EmploymentDto;
import MS_calculator.DTO.ScoringDataDto;

import java.time.LocalDate;

public class FinishRegistrationRequestDto {
    private ScoringDataDto.Gender gender;
    private ScoringDataDto.MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBrach;
    private EmploymentDto employment;
    private String accountNumber;

    public ScoringDataDto.Gender getGender() {
        return gender;
    }

    public void setGender(ScoringDataDto.Gender gender) {
        this.gender = gender;
    }

    public ScoringDataDto.MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(ScoringDataDto.MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getDependentAmount() {
        return dependentAmount;
    }

    public void setDependentAmount(Integer dependentAmount) {
        this.dependentAmount = dependentAmount;
    }

    public LocalDate getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(LocalDate passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public String getPassportIssueBrach() {
        return passportIssueBrach;
    }

    public void setPassportIssueBrach(String passportIssueBrach) {
        this.passportIssueBrach = passportIssueBrach;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentDto employment) {
        this.employment = employment;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
