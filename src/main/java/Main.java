import java.io.File;

/** main
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-18
 */
public class Main {
    public static void main(String[] args) {
        File n = new File("./src/main/resources/testList.hs");
        HorScript horScript = new HorScript();
        horScript.parserCode(n);
    }
}
