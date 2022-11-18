package parser.location;

/**
 * 具体到行/列到位置
 * @author hor (hor@itbk.cn)
 * @version : 2022-11-11
 */
public final class CodeLocation {
    private final int lineNumber;   // 代码行号
    private final int columnNumber; // 代码行的第几个字符

    public CodeLocation() {
        this.lineNumber = -1;
        this.columnNumber = -1;
    }

    public CodeLocation(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }

    @Override
    public String toString() {
        if (lineNumber <= 0 && columnNumber < 0) {
            return "未知";
        }
        String lineNumStr = lineNumber >= 0 ? String.valueOf(lineNumber) : "未知";
        String columnNumStr = columnNumber >= 0 ? String.valueOf(columnNumber) : "未知";
        if ("未知".equalsIgnoreCase(columnNumStr)) {
            return lineNumStr;
        } else {
            return lineNumStr + ":" + columnNumStr;
        }
    }
}