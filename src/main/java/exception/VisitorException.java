package exception;


import parser.location.CodeLocation;
import parser.location.LocationUtils;

/** 解析异常
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-15
 */
public class VisitorException extends HorScriptException {
    public VisitorException(int line, int column, String message) {
        super(LocationUtils.atLocation(new CodeLocation(line, column), null), message);
    }

    public int getLine() {
        return this.getLocation().getStartPosition().getLineNumber();
    }

    public int getColumn() {
        return this.getLocation().getStartPosition().getColumnNumber();
    }
}