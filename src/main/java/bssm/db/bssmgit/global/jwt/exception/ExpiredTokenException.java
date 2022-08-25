package bssm.db.bssmgit.global.jwt.exception;

import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;

public class ExpiredTokenException extends CustomException {

    public final static ExpiredTokenException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }

}
