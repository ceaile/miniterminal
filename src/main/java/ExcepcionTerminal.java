
public class ExcepcionTerminal extends Exception{

    /**
     * Creates a new instance of <code>ExcepcionTerminal</code> without detail
     * message.
     */
    public ExcepcionTerminal() {
    }

    /**
     * Constructs an instance of <code>ExcepcionTerminal</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ExcepcionTerminal(String msg) {
        super(msg);
    }
}
