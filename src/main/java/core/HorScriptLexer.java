// Generated from java-escape by ANTLR 4.11.1
package core;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class HorScriptLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		VAR=1, GLOBAL=2, IMPORT=3, EXPORT=4, AS=5, ASSERT=6, DEF=7, RETURN=8, 
		IF=9, ELSE=10, FOR=11, WHILE=12, DO=13, TO=14, IN=15, NULL=16, PRINT=17, 
		PRINTLN=18, INPUT=19, SIZE=20, TRUE=21, FALSE=22, PLUS=23, MINUS=24, MUL=25, 
		DIV=26, MOD=27, LBT=28, RBT=29, AND=30, OR=31, NOT=32, XOR=33, LSHIFT=34, 
		RSHIFT=35, RSHIFT2=36, GT=37, LT=38, GTEquals=39, LTEquals=40, EQ=41, 
		NE=42, SC_OR=43, SC_AND=44, ROU=45, COMMA=46, COLON=47, ASS=48, DOT=49, 
		LSBT=50, RSBT=51, OCBR=52, CCBR=53, QMark=54, SEM=55, LAMBDA=56, STRING=57, 
		INTEGER_NUM=58, DECIMAL_NUM=59, IDENTIFIER=60, Space=61, Comment=62, EOL=63;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"VAR", "GLOBAL", "IMPORT", "EXPORT", "AS", "ASSERT", "DEF", "RETURN", 
			"IF", "ELSE", "FOR", "WHILE", "DO", "TO", "IN", "NULL", "PRINT", "PRINTLN", 
			"INPUT", "SIZE", "TRUE", "FALSE", "PLUS", "MINUS", "MUL", "DIV", "MOD", 
			"LBT", "RBT", "AND", "OR", "NOT", "XOR", "LSHIFT", "RSHIFT", "RSHIFT2", 
			"GT", "LT", "GTEquals", "LTEquals", "EQ", "NE", "SC_OR", "SC_AND", "ROU", 
			"COMMA", "COLON", "ASS", "DOT", "LSBT", "RSBT", "OCBR", "CCBR", "QMark", 
			"SEM", "LAMBDA", "STRING", "INTEGER_NUM", "DECIMAL_NUM", "INT", "DIGIT", 
			"IDENTIFIER", "Space", "Comment", "EOL"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\\u8D4B\\u503C'", "'\\u5168\\u5C40'", "'\\u5BFC\\u5165'", "'\\u5BFC\\u51FA'", 
			"'\\u4E3A'", "'\\u65AD\\u8A00'", "'\\u51FD\\u6570'", "'\\u8FD4\\u56DE'", 
			"'\\u5982\\u679C'", "'\\u5426\\u5219'", "'\\u5FAA\\u73AF'", "'\\u5224\\u65AD\\u5FAA\\u73AF'", 
			"'\\u5F00\\u59CB'", "'\\u5230'", "'\\u5728'", "'\\u7A7A'", "'\\u6253\\u5370'", 
			"'\\u6253\\u5370\\u6362\\u884C'", "'\\u8F93\\u5165'", "'\\u957F\\u5EA6'", 
			"'\\u771F'", "'\\u5047'", "'+'", "'-'", "'*'", "'/'", "'%'", "'('", "')'", 
			"'&'", "'|'", "'!'", "'^'", "'<<'", "'>>'", "'>>>'", "'>'", "'<'", "'>='", 
			"'<='", "'=='", "'!='", "'||'", "'&&'", "'@'", "','", "':'", "'='", "'.'", 
			"'['", "']'", "'{'", "'}'", "'?'", "';'", "'=>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "VAR", "GLOBAL", "IMPORT", "EXPORT", "AS", "ASSERT", "DEF", "RETURN", 
			"IF", "ELSE", "FOR", "WHILE", "DO", "TO", "IN", "NULL", "PRINT", "PRINTLN", 
			"INPUT", "SIZE", "TRUE", "FALSE", "PLUS", "MINUS", "MUL", "DIV", "MOD", 
			"LBT", "RBT", "AND", "OR", "NOT", "XOR", "LSHIFT", "RSHIFT", "RSHIFT2", 
			"GT", "LT", "GTEquals", "LTEquals", "EQ", "NE", "SC_OR", "SC_AND", "ROU", 
			"COMMA", "COLON", "ASS", "DOT", "LSBT", "RSBT", "OCBR", "CCBR", "QMark", 
			"SEM", "LAMBDA", "STRING", "INTEGER_NUM", "DECIMAL_NUM", "IDENTIFIER", 
			"Space", "Comment", "EOL"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public HorScriptLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "HorScriptLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000?\u0165\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u0007"+
		"0\u00021\u00071\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u0007"+
		"5\u00026\u00076\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007"+
		":\u0002;\u0007;\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007"+
		"?\u0002@\u0007@\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n"+
		"\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0018"+
		"\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001b"+
		"\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001e"+
		"\u0001\u001e\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001!\u0001!\u0001"+
		"!\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0001$\u0001$\u0001"+
		"%\u0001%\u0001&\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0001(\u0001(\u0001"+
		"(\u0001)\u0001)\u0001)\u0001*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001"+
		",\u0001,\u0001-\u0001-\u0001.\u0001.\u0001/\u0001/\u00010\u00010\u0001"+
		"1\u00011\u00012\u00012\u00013\u00013\u00014\u00014\u00015\u00015\u0001"+
		"6\u00016\u00017\u00017\u00017\u00018\u00018\u00018\u00018\u00058\u0117"+
		"\b8\n8\f8\u011a\t8\u00018\u00018\u00018\u00018\u00018\u00058\u0121\b8"+
		"\n8\f8\u0124\t8\u00018\u00038\u0127\b8\u00019\u00019\u0001:\u0001:\u0001"+
		":\u0005:\u012e\b:\n:\f:\u0131\t:\u0003:\u0133\b:\u0001;\u0001;\u0005;"+
		"\u0137\b;\n;\f;\u013a\t;\u0001;\u0003;\u013d\b;\u0001<\u0001<\u0001=\u0001"+
		"=\u0005=\u0143\b=\n=\f=\u0146\t=\u0001>\u0001>\u0001>\u0001>\u0001?\u0001"+
		"?\u0001?\u0001?\u0005?\u0150\b?\n?\f?\u0153\t?\u0001?\u0001?\u0001?\u0001"+
		"?\u0005?\u0159\b?\n?\f?\u015c\t?\u0001?\u0001?\u0003?\u0160\b?\u0001?"+
		"\u0001?\u0001@\u0001@\u0001\u015a\u0000A\u0001\u0001\u0003\u0002\u0005"+
		"\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n"+
		"\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011"+
		"#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u001a5\u001b"+
		"7\u001c9\u001d;\u001e=\u001f? A!C\"E#G$I%K&M\'O(Q)S*U+W,Y-[.]/_0a1c2e"+
		"3g4i5k6m7o8q9s:u;w\u0000y\u0000{<}=\u007f>\u0081?\u0001\u0000\u000b\u0001"+
		"\u0000\"\"\u0004\u0000\n\n\r\r\"\"\\\\\u0002\u0000\n\n\r\r\u0001\u0000"+
		"\'\'\u0004\u0000\n\n\r\r\'\'\\\\\u0001\u000019\u0001\u000009\u0004\u0000"+
		"AZ__az\u4e00\u8000\u9fa5\u0005\u000009AZ__az\u4e00\u8000\u9fa5\u0003\u0000"+
		"\t\n\f\r  \u0002\u0000\n\n\f\r\u016f\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"+
		"\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000"+
		"-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001"+
		"\u0000\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000"+
		"\u0000\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000"+
		";\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001"+
		"\u0000\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0000C\u0001\u0000\u0000"+
		"\u0000\u0000E\u0001\u0000\u0000\u0000\u0000G\u0001\u0000\u0000\u0000\u0000"+
		"I\u0001\u0000\u0000\u0000\u0000K\u0001\u0000\u0000\u0000\u0000M\u0001"+
		"\u0000\u0000\u0000\u0000O\u0001\u0000\u0000\u0000\u0000Q\u0001\u0000\u0000"+
		"\u0000\u0000S\u0001\u0000\u0000\u0000\u0000U\u0001\u0000\u0000\u0000\u0000"+
		"W\u0001\u0000\u0000\u0000\u0000Y\u0001\u0000\u0000\u0000\u0000[\u0001"+
		"\u0000\u0000\u0000\u0000]\u0001\u0000\u0000\u0000\u0000_\u0001\u0000\u0000"+
		"\u0000\u0000a\u0001\u0000\u0000\u0000\u0000c\u0001\u0000\u0000\u0000\u0000"+
		"e\u0001\u0000\u0000\u0000\u0000g\u0001\u0000\u0000\u0000\u0000i\u0001"+
		"\u0000\u0000\u0000\u0000k\u0001\u0000\u0000\u0000\u0000m\u0001\u0000\u0000"+
		"\u0000\u0000o\u0001\u0000\u0000\u0000\u0000q\u0001\u0000\u0000\u0000\u0000"+
		"s\u0001\u0000\u0000\u0000\u0000u\u0001\u0000\u0000\u0000\u0000{\u0001"+
		"\u0000\u0000\u0000\u0000}\u0001\u0000\u0000\u0000\u0000\u007f\u0001\u0000"+
		"\u0000\u0000\u0000\u0081\u0001\u0000\u0000\u0000\u0001\u0083\u0001\u0000"+
		"\u0000\u0000\u0003\u0086\u0001\u0000\u0000\u0000\u0005\u0089\u0001\u0000"+
		"\u0000\u0000\u0007\u008c\u0001\u0000\u0000\u0000\t\u008f\u0001\u0000\u0000"+
		"\u0000\u000b\u0091\u0001\u0000\u0000\u0000\r\u0094\u0001\u0000\u0000\u0000"+
		"\u000f\u0097\u0001\u0000\u0000\u0000\u0011\u009a\u0001\u0000\u0000\u0000"+
		"\u0013\u009d\u0001\u0000\u0000\u0000\u0015\u00a0\u0001\u0000\u0000\u0000"+
		"\u0017\u00a3\u0001\u0000\u0000\u0000\u0019\u00a8\u0001\u0000\u0000\u0000"+
		"\u001b\u00ab\u0001\u0000\u0000\u0000\u001d\u00ad\u0001\u0000\u0000\u0000"+
		"\u001f\u00af\u0001\u0000\u0000\u0000!\u00b1\u0001\u0000\u0000\u0000#\u00b4"+
		"\u0001\u0000\u0000\u0000%\u00b9\u0001\u0000\u0000\u0000\'\u00bc\u0001"+
		"\u0000\u0000\u0000)\u00bf\u0001\u0000\u0000\u0000+\u00c1\u0001\u0000\u0000"+
		"\u0000-\u00c3\u0001\u0000\u0000\u0000/\u00c5\u0001\u0000\u0000\u00001"+
		"\u00c7\u0001\u0000\u0000\u00003\u00c9\u0001\u0000\u0000\u00005\u00cb\u0001"+
		"\u0000\u0000\u00007\u00cd\u0001\u0000\u0000\u00009\u00cf\u0001\u0000\u0000"+
		"\u0000;\u00d1\u0001\u0000\u0000\u0000=\u00d3\u0001\u0000\u0000\u0000?"+
		"\u00d5\u0001\u0000\u0000\u0000A\u00d7\u0001\u0000\u0000\u0000C\u00d9\u0001"+
		"\u0000\u0000\u0000E\u00dc\u0001\u0000\u0000\u0000G\u00df\u0001\u0000\u0000"+
		"\u0000I\u00e3\u0001\u0000\u0000\u0000K\u00e5\u0001\u0000\u0000\u0000M"+
		"\u00e7\u0001\u0000\u0000\u0000O\u00ea\u0001\u0000\u0000\u0000Q\u00ed\u0001"+
		"\u0000\u0000\u0000S\u00f0\u0001\u0000\u0000\u0000U\u00f3\u0001\u0000\u0000"+
		"\u0000W\u00f6\u0001\u0000\u0000\u0000Y\u00f9\u0001\u0000\u0000\u0000["+
		"\u00fb\u0001\u0000\u0000\u0000]\u00fd\u0001\u0000\u0000\u0000_\u00ff\u0001"+
		"\u0000\u0000\u0000a\u0101\u0001\u0000\u0000\u0000c\u0103\u0001\u0000\u0000"+
		"\u0000e\u0105\u0001\u0000\u0000\u0000g\u0107\u0001\u0000\u0000\u0000i"+
		"\u0109\u0001\u0000\u0000\u0000k\u010b\u0001\u0000\u0000\u0000m\u010d\u0001"+
		"\u0000\u0000\u0000o\u010f\u0001\u0000\u0000\u0000q\u0126\u0001\u0000\u0000"+
		"\u0000s\u0128\u0001\u0000\u0000\u0000u\u012a\u0001\u0000\u0000\u0000w"+
		"\u013c\u0001\u0000\u0000\u0000y\u013e\u0001\u0000\u0000\u0000{\u0140\u0001"+
		"\u0000\u0000\u0000}\u0147\u0001\u0000\u0000\u0000\u007f\u015f\u0001\u0000"+
		"\u0000\u0000\u0081\u0163\u0001\u0000\u0000\u0000\u0083\u0084\u0005\u8000"+
		"\u8d4b\u0000\u0000\u0084\u0085\u0005\u503c\u0000\u0000\u0085\u0002\u0001"+
		"\u0000\u0000\u0000\u0086\u0087\u0005\u5168\u0000\u0000\u0087\u0088\u0005"+
		"\u5c40\u0000\u0000\u0088\u0004\u0001\u0000\u0000\u0000\u0089\u008a\u0005"+
		"\u5bfc\u0000\u0000\u008a\u008b\u0005\u5165\u0000\u0000\u008b\u0006\u0001"+
		"\u0000\u0000\u0000\u008c\u008d\u0005\u5bfc\u0000\u0000\u008d\u008e\u0005"+
		"\u51fa\u0000\u0000\u008e\b\u0001\u0000\u0000\u0000\u008f\u0090\u0005\u4e3a"+
		"\u0000\u0000\u0090\n\u0001\u0000\u0000\u0000\u0091\u0092\u0005\u65ad\u0000"+
		"\u0000\u0092\u0093\u0005\u8000\u8a00\u0000\u0000\u0093\f\u0001\u0000\u0000"+
		"\u0000\u0094\u0095\u0005\u51fd\u0000\u0000\u0095\u0096\u0005\u6570\u0000"+
		"\u0000\u0096\u000e\u0001\u0000\u0000\u0000\u0097\u0098\u0005\u8000\u8fd4"+
		"\u0000\u0000\u0098\u0099\u0005\u56de\u0000\u0000\u0099\u0010\u0001\u0000"+
		"\u0000\u0000\u009a\u009b\u0005\u5982\u0000\u0000\u009b\u009c\u0005\u679c"+
		"\u0000\u0000\u009c\u0012\u0001\u0000\u0000\u0000\u009d\u009e\u0005\u5426"+
		"\u0000\u0000\u009e\u009f\u0005\u5219\u0000\u0000\u009f\u0014\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a1\u0005\u5faa\u0000\u0000\u00a1\u00a2\u0005\u73af"+
		"\u0000\u0000\u00a2\u0016\u0001\u0000\u0000\u0000\u00a3\u00a4\u0005\u5224"+
		"\u0000\u0000\u00a4\u00a5\u0005\u65ad\u0000\u0000\u00a5\u00a6\u0005\u5faa"+
		"\u0000\u0000\u00a6\u00a7\u0005\u73af\u0000\u0000\u00a7\u0018\u0001\u0000"+
		"\u0000\u0000\u00a8\u00a9\u0005\u5f00\u0000\u0000\u00a9\u00aa\u0005\u59cb"+
		"\u0000\u0000\u00aa\u001a\u0001\u0000\u0000\u0000\u00ab\u00ac\u0005\u5230"+
		"\u0000\u0000\u00ac\u001c\u0001\u0000\u0000\u0000\u00ad\u00ae\u0005\u5728"+
		"\u0000\u0000\u00ae\u001e\u0001\u0000\u0000\u0000\u00af\u00b0\u0005\u7a7a"+
		"\u0000\u0000\u00b0 \u0001\u0000\u0000\u0000\u00b1\u00b2\u0005\u6253\u0000"+
		"\u0000\u00b2\u00b3\u0005\u5370\u0000\u0000\u00b3\"\u0001\u0000\u0000\u0000"+
		"\u00b4\u00b5\u0005\u6253\u0000\u0000\u00b5\u00b6\u0005\u5370\u0000\u0000"+
		"\u00b6\u00b7\u0005\u6362\u0000\u0000\u00b7\u00b8\u0005\u8000\u884c\u0000"+
		"\u0000\u00b8$\u0001\u0000\u0000\u0000\u00b9\u00ba\u0005\u8000\u8f93\u0000"+
		"\u0000\u00ba\u00bb\u0005\u5165\u0000\u0000\u00bb&\u0001\u0000\u0000\u0000"+
		"\u00bc\u00bd\u0005\u8000\u957f\u0000\u0000\u00bd\u00be\u0005\u5ea6\u0000"+
		"\u0000\u00be(\u0001\u0000\u0000\u0000\u00bf\u00c0\u0005\u771f\u0000\u0000"+
		"\u00c0*\u0001\u0000\u0000\u0000\u00c1\u00c2\u0005\u5047\u0000\u0000\u00c2"+
		",\u0001\u0000\u0000\u0000\u00c3\u00c4\u0005+\u0000\u0000\u00c4.\u0001"+
		"\u0000\u0000\u0000\u00c5\u00c6\u0005-\u0000\u0000\u00c60\u0001\u0000\u0000"+
		"\u0000\u00c7\u00c8\u0005*\u0000\u0000\u00c82\u0001\u0000\u0000\u0000\u00c9"+
		"\u00ca\u0005/\u0000\u0000\u00ca4\u0001\u0000\u0000\u0000\u00cb\u00cc\u0005"+
		"%\u0000\u0000\u00cc6\u0001\u0000\u0000\u0000\u00cd\u00ce\u0005(\u0000"+
		"\u0000\u00ce8\u0001\u0000\u0000\u0000\u00cf\u00d0\u0005)\u0000\u0000\u00d0"+
		":\u0001\u0000\u0000\u0000\u00d1\u00d2\u0005&\u0000\u0000\u00d2<\u0001"+
		"\u0000\u0000\u0000\u00d3\u00d4\u0005|\u0000\u0000\u00d4>\u0001\u0000\u0000"+
		"\u0000\u00d5\u00d6\u0005!\u0000\u0000\u00d6@\u0001\u0000\u0000\u0000\u00d7"+
		"\u00d8\u0005^\u0000\u0000\u00d8B\u0001\u0000\u0000\u0000\u00d9\u00da\u0005"+
		"<\u0000\u0000\u00da\u00db\u0005<\u0000\u0000\u00dbD\u0001\u0000\u0000"+
		"\u0000\u00dc\u00dd\u0005>\u0000\u0000\u00dd\u00de\u0005>\u0000\u0000\u00de"+
		"F\u0001\u0000\u0000\u0000\u00df\u00e0\u0005>\u0000\u0000\u00e0\u00e1\u0005"+
		">\u0000\u0000\u00e1\u00e2\u0005>\u0000\u0000\u00e2H\u0001\u0000\u0000"+
		"\u0000\u00e3\u00e4\u0005>\u0000\u0000\u00e4J\u0001\u0000\u0000\u0000\u00e5"+
		"\u00e6\u0005<\u0000\u0000\u00e6L\u0001\u0000\u0000\u0000\u00e7\u00e8\u0005"+
		">\u0000\u0000\u00e8\u00e9\u0005=\u0000\u0000\u00e9N\u0001\u0000\u0000"+
		"\u0000\u00ea\u00eb\u0005<\u0000\u0000\u00eb\u00ec\u0005=\u0000\u0000\u00ec"+
		"P\u0001\u0000\u0000\u0000\u00ed\u00ee\u0005=\u0000\u0000\u00ee\u00ef\u0005"+
		"=\u0000\u0000\u00efR\u0001\u0000\u0000\u0000\u00f0\u00f1\u0005!\u0000"+
		"\u0000\u00f1\u00f2\u0005=\u0000\u0000\u00f2T\u0001\u0000\u0000\u0000\u00f3"+
		"\u00f4\u0005|\u0000\u0000\u00f4\u00f5\u0005|\u0000\u0000\u00f5V\u0001"+
		"\u0000\u0000\u0000\u00f6\u00f7\u0005&\u0000\u0000\u00f7\u00f8\u0005&\u0000"+
		"\u0000\u00f8X\u0001\u0000\u0000\u0000\u00f9\u00fa\u0005@\u0000\u0000\u00fa"+
		"Z\u0001\u0000\u0000\u0000\u00fb\u00fc\u0005,\u0000\u0000\u00fc\\\u0001"+
		"\u0000\u0000\u0000\u00fd\u00fe\u0005:\u0000\u0000\u00fe^\u0001\u0000\u0000"+
		"\u0000\u00ff\u0100\u0005=\u0000\u0000\u0100`\u0001\u0000\u0000\u0000\u0101"+
		"\u0102\u0005.\u0000\u0000\u0102b\u0001\u0000\u0000\u0000\u0103\u0104\u0005"+
		"[\u0000\u0000\u0104d\u0001\u0000\u0000\u0000\u0105\u0106\u0005]\u0000"+
		"\u0000\u0106f\u0001\u0000\u0000\u0000\u0107\u0108\u0005{\u0000\u0000\u0108"+
		"h\u0001\u0000\u0000\u0000\u0109\u010a\u0005}\u0000\u0000\u010aj\u0001"+
		"\u0000\u0000\u0000\u010b\u010c\u0005?\u0000\u0000\u010cl\u0001\u0000\u0000"+
		"\u0000\u010d\u010e\u0005;\u0000\u0000\u010en\u0001\u0000\u0000\u0000\u010f"+
		"\u0110\u0005=\u0000\u0000\u0110\u0111\u0005>\u0000\u0000\u0111p\u0001"+
		"\u0000\u0000\u0000\u0112\u0118\u0007\u0000\u0000\u0000\u0113\u0117\b\u0001"+
		"\u0000\u0000\u0114\u0115\u0005\\\u0000\u0000\u0115\u0117\b\u0002\u0000"+
		"\u0000\u0116\u0113\u0001\u0000\u0000\u0000\u0116\u0114\u0001\u0000\u0000"+
		"\u0000\u0117\u011a\u0001\u0000\u0000\u0000\u0118\u0116\u0001\u0000\u0000"+
		"\u0000\u0118\u0119\u0001\u0000\u0000\u0000\u0119\u011b\u0001\u0000\u0000"+
		"\u0000\u011a\u0118\u0001\u0000\u0000\u0000\u011b\u0127\u0007\u0000\u0000"+
		"\u0000\u011c\u0122\u0007\u0003\u0000\u0000\u011d\u0121\b\u0004\u0000\u0000"+
		"\u011e\u011f\u0005\\\u0000\u0000\u011f\u0121\b\u0002\u0000\u0000\u0120"+
		"\u011d\u0001\u0000\u0000\u0000\u0120\u011e\u0001\u0000\u0000\u0000\u0121"+
		"\u0124\u0001\u0000\u0000\u0000\u0122\u0120\u0001\u0000\u0000\u0000\u0122"+
		"\u0123\u0001\u0000\u0000\u0000\u0123\u0125\u0001\u0000\u0000\u0000\u0124"+
		"\u0122\u0001\u0000\u0000\u0000\u0125\u0127\u0007\u0003\u0000\u0000\u0126"+
		"\u0112\u0001\u0000\u0000\u0000\u0126\u011c\u0001\u0000\u0000\u0000\u0127"+
		"r\u0001\u0000\u0000\u0000\u0128\u0129\u0003w;\u0000\u0129t\u0001\u0000"+
		"\u0000\u0000\u012a\u0132\u0003w;\u0000\u012b\u012f\u0005.\u0000\u0000"+
		"\u012c\u012e\u0003y<\u0000\u012d\u012c\u0001\u0000\u0000\u0000\u012e\u0131"+
		"\u0001\u0000\u0000\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u012f\u0130"+
		"\u0001\u0000\u0000\u0000\u0130\u0133\u0001\u0000\u0000\u0000\u0131\u012f"+
		"\u0001\u0000\u0000\u0000\u0132\u012b\u0001\u0000\u0000\u0000\u0132\u0133"+
		"\u0001\u0000\u0000\u0000\u0133v\u0001\u0000\u0000\u0000\u0134\u0138\u0007"+
		"\u0005\u0000\u0000\u0135\u0137\u0003y<\u0000\u0136\u0135\u0001\u0000\u0000"+
		"\u0000\u0137\u013a\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000"+
		"\u0000\u0138\u0139\u0001\u0000\u0000\u0000\u0139\u013d\u0001\u0000\u0000"+
		"\u0000\u013a\u0138\u0001\u0000\u0000\u0000\u013b\u013d\u00050\u0000\u0000"+
		"\u013c\u0134\u0001\u0000\u0000\u0000\u013c\u013b\u0001\u0000\u0000\u0000"+
		"\u013dx\u0001\u0000\u0000\u0000\u013e\u013f\u0007\u0006\u0000\u0000\u013f"+
		"z\u0001\u0000\u0000\u0000\u0140\u0144\u0007\u0007\u0000\u0000\u0141\u0143"+
		"\u0007\b\u0000\u0000\u0142\u0141\u0001\u0000\u0000\u0000\u0143\u0146\u0001"+
		"\u0000\u0000\u0000\u0144\u0142\u0001\u0000\u0000\u0000\u0144\u0145\u0001"+
		"\u0000\u0000\u0000\u0145|\u0001\u0000\u0000\u0000\u0146\u0144\u0001\u0000"+
		"\u0000\u0000\u0147\u0148\u0007\t\u0000\u0000\u0148\u0149\u0001\u0000\u0000"+
		"\u0000\u0149\u014a\u0006>\u0000\u0000\u014a~\u0001\u0000\u0000\u0000\u014b"+
		"\u014c\u0005/\u0000\u0000\u014c\u014d\u0005/\u0000\u0000\u014d\u0151\u0001"+
		"\u0000\u0000\u0000\u014e\u0150\b\u0002\u0000\u0000\u014f\u014e\u0001\u0000"+
		"\u0000\u0000\u0150\u0153\u0001\u0000\u0000\u0000\u0151\u014f\u0001\u0000"+
		"\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0160\u0001\u0000"+
		"\u0000\u0000\u0153\u0151\u0001\u0000\u0000\u0000\u0154\u0155\u0005/\u0000"+
		"\u0000\u0155\u0156\u0005*\u0000\u0000\u0156\u015a\u0001\u0000\u0000\u0000"+
		"\u0157\u0159\t\u0000\u0000\u0000\u0158\u0157\u0001\u0000\u0000\u0000\u0159"+
		"\u015c\u0001\u0000\u0000\u0000\u015a\u015b\u0001\u0000\u0000\u0000\u015a"+
		"\u0158\u0001\u0000\u0000\u0000\u015b\u015d\u0001\u0000\u0000\u0000\u015c"+
		"\u015a\u0001\u0000\u0000\u0000\u015d\u015e\u0005*\u0000\u0000\u015e\u0160"+
		"\u0005/\u0000\u0000\u015f\u014b\u0001\u0000\u0000\u0000\u015f\u0154\u0001"+
		"\u0000\u0000\u0000\u0160\u0161\u0001\u0000\u0000\u0000\u0161\u0162\u0006"+
		"?\u0000\u0000\u0162\u0080\u0001\u0000\u0000\u0000\u0163\u0164\u0007\n"+
		"\u0000\u0000\u0164\u0082\u0001\u0000\u0000\u0000\u000e\u0000\u0116\u0118"+
		"\u0120\u0122\u0126\u012f\u0132\u0138\u013c\u0144\u0151\u015a\u015f\u0001"+
		"\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}