package cn.itbk.horscript.parser.location;

/**
 * AST 和代码文本的位置关系
 * @author Hor (hor@itbk.cn)
 * @version : 2022-11-11
 */
public interface Location {
    CodeLocation getStartPosition();

    CodeLocation getEndPosition();

    void setStartPosition(CodeLocation codeLocation);

    void setEndPosition(CodeLocation codeLocation);
}