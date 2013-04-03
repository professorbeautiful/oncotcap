package oncotcap.process.clinicaltrial;

public class Phase2SimonBundle extends java.util.ListResourceBundle{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    //Changing tags in the following block will
    //break the parsing of the related file
    //{{Start Automatic Code Generation Block
//    {"lblAlpha_label", "Probability of Accepting Poor Response"},
	{"lblAlpha_label", "<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\"><b>\"Alpha\" (Type I error)<br>Probability of accepting a poor drug.</b></p></html>"},
    {"buttonGood_label", ""},
    {"txtAlpha_text", ""},
//    {"lblBeta_label", "Probability of Rejecting Good Drug"},
	{"lblBeta_label", "<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\"><b>\"Beta\" (Type II error)<br>Probability of not accepting a good drug.</b></p></html>"},
    {"txtBeta_text", ""},
    {"lblPrResp0_label", "<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\"><b>\"P0\" (Disappointing Response Rate)<br>Response probability of a poor drug.</b></p></html>"},
   // {"txtPrResp0_text", "0.30"},
    {"txtPrResp0_text", ""},
    {"lblPrResp1_label", "<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\"><b>\"P1\" (Promising Response Rate)<br>Response probability of a good drug.<br>P1 must be greater than P0.</b></p></html>"},
   // {"txtPrResp1_text", "0.50"},
    {"txtPrResp1_text", ""},
    {"buttonBad_label", ""},
    //}}End Automatic Code Generation Block
    {"Phase2Simon_badString___new_String", "This entry ihas been changed. \nClick any buttons to update."},
    {"Phase2Simon_goodString___new_String", "Answers are now updated. \nChange numbers to explore other possibilities."},
    {"Phase2Simon_goodString___welcome_String", "Welcome!! You can change numbers and click any buttons to calculate."},
    {"System_out_println", "Number format error\\n"},
    {"int_decimal___s_indexOf", "."},
    {"System_out_println_1", "Number format error\\n"},
  };
}

