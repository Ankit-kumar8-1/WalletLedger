package in.ankitsaahariya.WalletLedger.exceptions;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message){
        super(message);
    }
}
