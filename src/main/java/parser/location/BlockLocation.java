package parser.location;

/**
 * 代码文本块的位置
 *
 * @author hor (hor@itbk.cn)
 * @version : 2022-11-11
 */
public class BlockLocation implements Location {
    private CodeLocation startPosition;
    private CodeLocation endPosition;

    public CodeLocation getStartPosition() {
        return this.startPosition;
    }

    public CodeLocation getEndPosition() {
        return this.endPosition;
    }

    public void setStartPosition(CodeLocation codeLocation) {
        this.startPosition = codeLocation;
    }

    public void setEndPosition(CodeLocation codeLocation) {
        this.endPosition = codeLocation;
    }

    @Override
    public String toString() {
        String starStr = getStartPosition().toString();
        String endStr = getEndPosition().toString();
        if ("未知".equalsIgnoreCase(starStr) && "未知".equalsIgnoreCase(endStr)) {
            return "未知";
        }
        if ("未知".equalsIgnoreCase(endStr)) {
            return "行 " + starStr;
        } else {
            return "行 " + starStr + "~" + endStr;
        }
    }
}