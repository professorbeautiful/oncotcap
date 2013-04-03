package oncotcap.engine;

import oncotcap.util.JavaRecognizer;

public class VariableDependency
{

		public static int LITERAL_if = JavaRecognizer.LITERAL_if;
		public static int LITERAL_else = JavaRecognizer.LITERAL_else;
		public static int LITERAL_for = JavaRecognizer.LITERAL_for;
		public static int LITERAL_while = JavaRecognizer.LITERAL_while;
		public static int LITERAL_do = JavaRecognizer.LITERAL_do;
		public static int ASSIGN = JavaRecognizer.ASSIGN;
		public static int PLUS_ASSIGN = JavaRecognizer.PLUS_ASSIGN;
		public static int MINUS_ASSIGN = JavaRecognizer.MINUS_ASSIGN;
		public static int STAR_ASSIGN = JavaRecognizer.STAR_ASSIGN;
		public static int DIV_ASSIGN = JavaRecognizer.DIV_ASSIGN;
		public static int MOD_ASSIGN = JavaRecognizer.MOD_ASSIGN;
		public static int SR_ASSIGN = JavaRecognizer.SR_ASSIGN;
		public static int BSR_ASSIGN = JavaRecognizer.BSR_ASSIGN;
		public static int SL_ASSIGN = JavaRecognizer.SL_ASSIGN;
		public static int BAND_ASSIGN = JavaRecognizer.BAND_ASSIGN;
		public static int BXOR_ASSIGN = JavaRecognizer.BXOR_ASSIGN;
		public static int BOR_ASSIGN = JavaRecognizer.BOR_ASSIGN;
		public static int POST_INC = JavaRecognizer.POST_INC;
		public static int POST_DEC = JavaRecognizer.POST_DEC;

	String leftVariableName;
	String rightVariableName;
	int operatorType;

	public VariableDependency(String leftVariableName, String rightVariableName,
														int operatorType)
	{
		this.leftVariableName = leftVariableName;
		this.rightVariableName = rightVariableName;
		this.operatorType = operatorType;
	}
		public String toString() {
				return leftVariableName + getOperatorString(operatorType) + rightVariableName;
		}

		public String getLeftVariableName() {
				return leftVariableName;
		}
		public String getRightVariableName() {
				return rightVariableName;
		}
		public int getOperatorType() {
				return operatorType;
		}
		public boolean equals(Object vd) {
				return equals((VariableDependency)vd);
		}
		public boolean equals(VariableDependency vd) {
				if ( leftVariableName != null && rightVariableName != null && 
						 leftVariableName.equals(vd.getLeftVariableName()) 
						 && rightVariableName.equals(vd.getRightVariableName())
						 && operatorType == vd.getOperatorType() ) 
						return true;
				return false;
		}
		public static String getOperatorString(int operatorType) {
				switch ( operatorType ) {
				case JavaRecognizer.ASSIGN:
						return "=";
				case JavaRecognizer.PLUS_ASSIGN:
						return "+=";
				case JavaRecognizer.MINUS_ASSIGN:
						return "-=";
				case JavaRecognizer.STAR_ASSIGN:
						return "*=";
				case JavaRecognizer.DIV_ASSIGN:
						return "/=";
				case JavaRecognizer.MOD_ASSIGN:
						return "%=";
				case JavaRecognizer.SR_ASSIGN:
						return ">>=";
				case JavaRecognizer.BSR_ASSIGN:
						return ">>>=";
				case JavaRecognizer.SL_ASSIGN:
						return "<<=";
				case JavaRecognizer.BAND_ASSIGN:
						return "&=";
				case JavaRecognizer.BXOR_ASSIGN:
						return "^=";
				case JavaRecognizer.BOR_ASSIGN:
						return "|=";
				case JavaRecognizer.POST_INC:
						return "++";
				case JavaRecognizer.POST_DEC:
						return "--";
	      case JavaRecognizer.LITERAL_if:
						return "if";
	      case JavaRecognizer.LITERAL_else:
						return "else";
				case JavaRecognizer.LITERAL_for:
						return "for";
				case JavaRecognizer.LITERAL_while:
						return "while";
				case JavaRecognizer.LITERAL_do:
						return "do";
				}
				return "";
				
		}
}
