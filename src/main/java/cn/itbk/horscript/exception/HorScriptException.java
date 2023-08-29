package cn.itbk.horscript.exception;

import cn.itbk.horscript.parser.location.Location;

public class HorScriptException extends RuntimeException {
    protected final Location location;
    private final   String   localizedMessage;

    public HorScriptException(Location location, String errorMessage) {
        super(errorMessage(location, errorMessage));
        this.location = location;
        this.localizedMessage = errorMessage;
    }

    public HorScriptException(Location location, String errorMessage, Throwable e) {
        super(errorMessage(location, errorMessage), e);
        this.location = location;
        this.localizedMessage = errorMessage;
    }

    public HorScriptException(Location location, Throwable e) {
        super(errorMessage(location, e.getLocalizedMessage()), e);
        this.location = location;
        this.localizedMessage = e.getLocalizedMessage();
    }

    private static String errorMessage(Location location, String errorMessage) {
        return "[" + location.toString() + "] " + errorMessage;
    }

    public String getLocalizedMessage() {
        return this.localizedMessage;
    }

    public final Location getLocation() {
        return this.location;
    }
}
