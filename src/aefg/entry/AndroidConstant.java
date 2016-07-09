package aefg.entry;

public class AndroidConstant {
	public static class SubSignature{
		public static final String STARTACTIVITY = "void startActivity(android.content.Intent)";
		public static final String STARTACTIVITYFORRESULT = "void startActivityForResult(android.content.Intent,int)";
	}
	
	public static class Signature{
		public static final String STARTACTIVITY = "<android.app.Activity: void startActivity(android.content.Intent)>";
		public static final String STARTACTIVITYFORRESULT = "<android.app.Activity: void startActivityForResult(android.content.Intent,int)";
		public static final String STARTACTIVITY_CONTENT = "<android.context.Content: void startActivity(android.content.Intent)>";
		public static final String STARTACTIVITYFORRESULT_CONTENT = "<android.context.Content: void startActivityForResult(android.content.Intent,int)";
		public static final String STARTACTIVITY_FRAGMENT = "<android.app.Fragment: void startActivity(android.content.Intent)";
		public static final String STARTACTIVITYFORRESULT_FRAGMENT = "<android.app.Fragment: void startActivityForResult(android.content.Intent,int)";
		public static final String STARTACTIVITY_FRAGMENT_v4  = "<android.app.support.v4.Fragment: void startActivity(android.content.Intent)";
		public static final String STARTACTIVITYFORRESULT_FRAGMENT_v4 = "<android.support.v4.app.Fragment: void startActivityForResult(android.content.Intent,int)";
	
	}
}
