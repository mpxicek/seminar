/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

/**
 *
 * @author mpx
 */
public class ServiceFailureException extends RuntimeException{

    public ServiceFailureException(String msg) {
        super(msg);
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
