package parser;

import core.HorScriptLexer;
import core.HorScriptParser;
import exception.ThrowingErrorListener;
import exception.VisitorException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.HorScriptVisitor;
import parser.ReturnValue;
import parser.Scope;

import java.io.File;
import java.io.IOException;

/** 运行
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-18
 */
public class HorScript {

    /**
     * 解析 parser.HorScript 执行脚本
     * @param code 脚本代码
     */
    public void parserCode(String code) {
        CharStream input = CharStreams.fromString(code);
        parser(input);
    }

    /**
     * 解析 parser.HorScript 执行脚本文件
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
        catch (ReturnValue returnValue) {
            System.out.print(returnValue.value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void _parserCode(File fileName) throws ReturnValue {
        try {
            CharStream input = CharStreams.fromFileName(fileName.getAbsolutePath());
            parser(input);
        }catch (VisitorException e ) {
            if (e.getMessage() != null) {
                System.err.println(e.getMessage());
            }
        }
        catch (ReturnValue returnValue) {
           throw  returnValue;
        }
        catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parser(CharStream charStream) throws VisitorException {
            HorScriptLexer lexer = new HorScriptLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HorScriptParser parser = new HorScriptParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(ThrowingErrorListener.INSTANCE);
            ParseTree tree = parser.rootInstSet();
            Scope scope = new Scope(null,false);
            HorScriptVisitor visitor = new HorScriptVisitor(scope);
            visitor.visit(tree);
    }
}
