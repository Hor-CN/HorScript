import core.HorScriptLexer;
import core.HorScriptParser;
import domain.ObjectModel;
import exception.ThrowingErrorListener;
import exception.VisitorException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.Function;
import parser.HorScriptVisitor;
import parser.Scope;

import java.io.File;
import java.util.Collections;
import java.util.Map;

/** 运行
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-18
 */
public class HorScript {

    /**
     * 解析 HorScript 执行脚本
     * @param code 脚本代码
     */
    public void parserCode(String code) {
        CharStream input = CharStreams.fromString(code);
        parser(input);
    }

    /**
     * 解析 HorScript 执行脚本文件
     * @param fileName 脚本代码文件地址
     */
    public void parserCode(File fileName) {
        try {
            CharStream input = CharStreams.fromFileName(fileName.getAbsolutePath());
            parser(input);
        }catch (VisitorException e ) {
            if (e.getMessage() != null) {
                System.err.println(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parser(CharStream charStream) throws VisitorException {
            HorScriptLexer lexer = new HorScriptLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HorScriptParser parser = new HorScriptParser(tokens);
            parser.setBuildParseTree(true);
            parser.removeErrorListeners();
            parser.addErrorListener(ThrowingErrorListener.INSTANCE);
            ParseTree tree = parser.rootInstSet();
            Scope scope = new Scope(null,false);
            Map<String, Function> functions = Collections.emptyMap();
            Map<String, ObjectModel> objects = Collections.emptyMap();
            HorScriptVisitor visitor = new HorScriptVisitor(scope,functions, objects);
            visitor.visit(tree);
    }
}
