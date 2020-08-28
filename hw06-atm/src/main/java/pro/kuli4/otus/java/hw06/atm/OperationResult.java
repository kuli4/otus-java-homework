package pro.kuli4.otus.java.hw06.atm;

public class OperationResult<T> {
    private final OperationStatus operationStatus;
    private final String comment;
    private final T result;

    public OperationResult(OperationStatus operationStatus, String comment, T result) {
        this.operationStatus = operationStatus;
        this.comment = comment;
        this.result = result;
    }

    public OperationStatus getOperationStatus() {
        return this.operationStatus;
    }

    public String getComment() {
        return this.comment;
    }

    public T getResult() {
        return this.result;
    }

    public String toString() {
        if (operationStatus.equals(OperationStatus.ERROR))
        {
            return comment;
        }
        return result.toString();
    }
}
