package ar.com.playfree.exceptions;

public class ServiceException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4433296236215068464L;

	public ServiceException(String mensaje) {
		super("ERROR: " + mensaje);
	}

}
