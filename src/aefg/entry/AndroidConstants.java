package aefg.entry;

public class AndroidConstants {
	public static class SubSig{
		public static final String STARTACTIVITY = "void startActivity(android.content.Intent)";
		public static final String STARTACTIVITYFORRESULT = "void startActivityForResult(android.content.Intent,int)";

	}
	
	public static class Sig{
		public static final String STARTACTIVITY = "<android.app.Activity: void startActivity(android.content.Intent)>";
		public static final String STARTACTIVITYFORRESULT = "<android.app.Activity: void startActivityForResult(android.content.Intent,int)";
		public static final String STARTACTIVITY_CONTENT = "<android.context.Content: void startActivity(android.content.Intent)>";
		public static final String STARTACTIVITYFORRESULT_CONTENT = "<android.context.Content: void startActivityForResult(android.content.Intent,int)";
	}
}
