package uz.davrbank.educontrolservice.exception.condition;

public class DepartmentIdNotFoundException extends RuntimeException{

    public DepartmentIdNotFoundException(String exceptionMessage){
        super(exceptionMessage);
    }
}
