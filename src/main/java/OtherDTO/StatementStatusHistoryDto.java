package OtherDTO;

import java.time.LocalDateTime;

public class StatementStatusHistoryDto {
    public enum Status {

    }
    public enum ChangeType {

    }
    private Status status;
    private LocalDateTime time;
    private ChangeType changeType;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }
}
