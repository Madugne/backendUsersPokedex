package chunyin.backendUsersPokedex.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(UUID id){
        super("No Record with Id " + id + " found");
    }
    public NotFoundException(long id){
        super("No Record with Id " + id + " found");
    }

}
