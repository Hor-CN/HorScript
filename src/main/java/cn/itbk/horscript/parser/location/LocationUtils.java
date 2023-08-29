package cn.itbk.horscript.parser.location;

public class LocationUtils {
    public static BlockLocation atLocation(CodeLocation start, CodeLocation end) {
        BlockLocation info = new BlockLocation();
        info.setStartPosition(start == null ? new CodeLocation() : start);
        info.setEndPosition(end == null ? new CodeLocation() : end);
        return info;
    }
}
