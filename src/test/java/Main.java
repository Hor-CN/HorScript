import parser.HorScript;

import java.io.File;

/** main
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-18
 */
public class Main {
    public static void main(String[] args) {
        File n = new File("src/test/java/例子/内置模块/随机模块.hs");
        HorScript horScript = new HorScript();
        horScript.parserCode(n);
    }
}
