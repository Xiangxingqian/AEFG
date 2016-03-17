package com.android.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.ArrayUtil;

//被class由三部分构成：view register event, event and event handler
//摘自android.view.View, android.preference.Preference, android.view.ViewGroup等 

public class EventContants {
	
	public final static List<EventTriple> eventTriples = new ArrayList<EventTriple>();
	public final static Map<String, String> callbackAndListener = new HashMap<String, String>();
	public final static Map<String, String> listenerAndRegistrar = new HashMap<String, String>();
	public final static Map<String, String> registrarAndListener = new HashMap<String, String>();
	
	public final static Map<String, List<String>> viewRegistrarToCallBacks = new HashMap<String, List<String>>();

	//android.view.View: 1-10 
	//1. onKey
	public static final String SETONKEYLISTENER = "void setOnKeyListener(android.view.View$OnKeyListener)";
	public static final String ONKEYLISTENER = "android.view.View$OnKeyListener";
	public static final String ONKEY = "boolean onKey(android.view.View,int,android.view.KeyEvent)";
	public static final String[] array1 = {ONKEY};
	
	//2. onTouch
	public static final String SETONTOUCHLISTENER = "void setOnTouchListener(android.view.View$OnTouchListener)";
	public static final String ONTOUCHLISTENER = "android.view.View$OnTouchListener";
	public static final String ONTOUCH = "boolean onTouch(android.view.View,android.view.MotionEvent)";
	public static final String[] array2 = {ONTOUCH};
	
	//3. onHover
	public static final String SETONHOVERLISTENER = "void setOnHoverListener(android.view.View$OnHoverListener)";
	public static final String ONHOVERLISTENER = "android.view.View$OnHoverListener";
	public static final String ONHOVER = "boolean onHover(android.view.View,android.view.MotionEvent)";
	public static final String[] array3 = {ONHOVER};
	//4. onGenericMotion
	public static final String SETONGENERICMOTIONLISTENER = "void setOnGenericMotionListener(android.view.View$OnGenericMotionListener)";
	public static final String ONGENERICMOTIONLISTENER = "android.view.View$OnGenericMotionListener";
	public static final String ONGENERICMOTION = "boolean onGenericMotion(android.view.View,android.view.MotionEvent)";
	public static final String[] array4 = {ONGENERICMOTION};
	
	//5. onLongClick
	public static final String SETONLONGCLICKLISTENER = "void setOnLongClickListener(android.view.View$OnLongClickListener)";
	public static final String ONLONGCLICKLISTENER = "android.view.View$OnLongClickListener";
	public static final String ONLONGCLICK = "boolean onLongClick(android.view.View)";
	public static final String[] array5 = {ONLONGCLICK};
	
	//6. onDrag
	public static final String SETONDRAGLISTENER = "void setOnDragListener(android.view.View$OnDragListener)";
	public static final String ONDRAGLISTENER = "android.view.View$OnDragListener";
	public static final String ONDRAG = "boolean onDrag(android.view.View,android.view.DragEvent)";
	public static final String[] array6 = {ONDRAG};
	
	//7. onFocusChange
	public static final String SETONFOCUSCHANGELISTENER = "void setOnFocusChangeListener(android.view.View$OnFocusChangeListener)";
	public static final String ONFOCUSCHANGELISTENER = "android.view.View$OnFocusChangeListener";
	public static final String ONFOCUSCHANGE = "void onFocusChange(android.view.View,boolean)";
	public static final String[] array7 = {ONFOCUSCHANGE};
	
	//8. onClick
	public static final String SETONCLICKLISTENER = "void setOnClickListener(android.view.View$OnClickListener)";
	public static final String ONCLICKLISTENER = "android.view.View$OnClickListener";
	public static final String ONCLICK = "void onClick(android.view.View)";
	public static final String[] array8 = {ONCLICK};
	
	//9. onCreateContextMenuListener
	public static final String SETONCREATECONTEXTMENULISTENER = "void setOnCreateContextMenuListener(android.view.View$OnCreateContextMenuListener)";
	public static final String ONCREATECONTEXTMENULISTENER = "android.view.View$OnCreateContextMenuListener";
	public static final String ONCONTEXTITEMSELECTED = "boolean onContextItemSelected(android.view.MenuItem)";
	public static final String ONCREATECONTEXTMENU = "void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenuInfo)";
	public static final String[] array9 = {ONCREATECONTEXTMENU};
	
	//10. onSystemUiVisibilityChangeListener
	public static final String SETONSYSTEMUIVISIBILITYCHANGELISTENER = "void setOnSystemUiVisibilityChangeListener(android.view.View$OnSystemUiVisibilityChangeListener)";
	public static final String ONSYSTEMUIVISIBILITYCHANGELISTENER = "android.view.View$OnSystemUiVisibilityChangeListener";
	public static final String ONSYSTEMUIVISIBILITYCHANGE = "void onSystemUiVisibilityChange(int)";
	public static final String[] array10 = {ONSYSTEMUIVISIBILITYCHANGE};
	
	//android.preference.Preference:11
	//11. onPreferenceTreeClick
	public static final String SETONPREFERENCECLICKLISTENER = "void setOnPreferenceClickListener(android.preference.Preference$OnPreferenceClickListener)";
	public static final String ONPREFERENCECLICKLISTENER = "android.preference.Preference$OnPreferenceClickListener";
	public static final String ONPREFERENCETREECLICK = "boolean onPreferenceTreeClick(android.preference.PreferenceScreen,android.preference.Preference)";
	public static final String[] array11 = {ONPREFERENCETREECLICK};
	
	//android.widget.TextView:12
	//12. OnEditorActionListener
	public static final String SETONEDITORACTIONLISTENER = "void setOnEditorActionListener(android.widget.TextView$OnEditorActionListener)";
	public static final String ONEDITORACTIONLISTENER = "android.widget.TextView$OnEditorActionListener";
	public static final String ONEDITORACTION = "boolean onEditorAction(android.widget.TextView,int,android.view.KeyEvent)";
	public static final String[] array12 = {ONEDITORACTION};
	
	//android.view.ViewGroup:13
	//13. OnHierarchyChangeListener
	public static final String SETONHIERARCHYCHANGELISTENER = "void setOnHierarchyChangeListener(android.view.ViewGroup$OnHierarchyChangeListener)";
	public static final String ONHIERARCHYCHANGELISTENER = "android.view.ViewGroup$OnHierarchyChangeListener";
	public static final String ONCHILDVIEWADDED = "void onChildViewAdded(android.view.View,android.view.View)";
	public static final String ONCHILDVIEWREMOVED = "void onChildViewRemoved(android.view.View,android.view.View)";
	public static final String[] array13 = {ONCHILDVIEWADDED,ONCHILDVIEWREMOVED};
	
	//android.view.AdapterView:14-16
	//14. OnItemClickListener
	public static final String SETONITEMCLICKLISTENER = "void setOnItemClickListener(android.view.AdapterView$OnItemClickListener)";
	public static final String ONITEMCLICKLISTENER = "android.view.AdapterView$OnItemClickListener";
	public static final String ONITEMCLICK = "void onItemClick(android.view.AdapterView,android.view.View,int,long)";
	public static final String[] array14 = {ONITEMCLICK};
	
	//15. OnItemLongClickListener
	public static final String SETONITEMLONGCLICKLISTENER = "void setOnItemLongClickListener(android.view.AdapterView$OnItemLongClickListener)";
	public static final String ONITEMLONGCLICKLISTENER = "android.view.AdapterView$OnItemLongClickListener";
	public static final String ONITEMLONGCLICK = "boolean onItemLongClick(android.view.AdapterView,android.view.View,int,long)";
	public static final String[] array15 = {ONITEMLONGCLICK};
	
	//16. OnItemSelectedListener
	public static final String SETONITEMSELECTEDLISTENER = "void setOnItemSelectedListener(android.view.AdapterView$OnItemSelectedListener)";
	public static final String ONITEMSELECTEDLISTENER = "android.view.AdapterView$OnItemSelectedListener";
	public static final String ONITEMSELECTED = "void onItemSelected(android.view.AdapterView,android.view.View,int,long)";
	public static final String ONNOTHINGSELECTED = "void onNothingSelected(android.view.AdapterView)";
	public static final String[] array16 = {ONITEMSELECTED,ONNOTHINGSELECTED};
	
	//android.widget.SeekBar:17
	//17. OnSeekBarChangeListener
	public static final String setOnSeekBarChangeListener = "void setOnSeekBarChangeListener(android.widget.SeekBar$OnSeekBarChangeListener)";
	public static final String OnSeekBarChangeListener = "android.widget.SeekBar$OnSeekBarChangeListener";
	public static final String onProgressChanged = "void onProgressChanged(android.widget.SeekBar,int,boolean)";
	public static final String onStartTrackingTouch = "void onStartTrackingTouch(android.widget.SeekBar)";
	public static final String onStopTrackingTouch = "void onStopTrackingTouch(android.widget.SeekBar)";
	public static final String[] array17 = {onProgressChanged,onStartTrackingTouch,onStopTrackingTouch};
	
	//android.widget.CompoundButton:18
	//18. OnCheckedChangeListener
	public static final String setOnCheckedChangeListener = "void setOnCheckedChangeListener(android.widget.CompoundButton$OnCheckedChangeListener)";
	public static final String OnCheckedChangeListener = "android.widget.CompoundButton$OnCheckedChangeListener";
	public static final String onCheckedChanged = "void onCheckedChanged(android.widget.CompoundButton,boolean)";
	public static final String[] array18 = {onCheckedChanged};
	
	//android.widget.RatingBar
	//19. OnRatingBarChangeListener
	public static final String setOnRatingBarChangeListener = "void setOnRatingBarChangeListener(android.widget.RatingBar$OnRatingBarChangeListener)";
	public static final String OnRatingBarChangeListener = "android.widget.RatingBar$OnRatingBarChangeListener";
	public static final String onRatingChanged = "void onRatingChanged(android.widget.RatingBar,float,boolean)";
	public static final String[] array19 = {onRatingChanged};
	
	//android.view.ViewStub
	//20. OnInflateListener 
	public static final String setOnInflateListener = "void setOnInflateListener(android.view.ViewStub$OnInflateListener)";
	public static final String OnInflateListener = "android.view.ViewStub$OnInflateListener";
	public static final String onInflate = "void onInflate(android.view.ViewStub,android.view.View)";
	public static final String[] array20 = {onInflate};
	
	//android.widget.NumberPicker
	//21. OnScrollListener
	public static final String NumberPicker_setOnScrollListener = "void setOnScrollListener(android.widget.NumberPicker$OnScrollListener)";
	public static final String NumberPicker_OnScrollListener = "android.widget.NumberPicker$OnScrollListener";
	public static final String NumberPicker_onScrollStateChange = "void onScrollStateChange(android.widget.NumberPicker,int)";
	public static final String[] array21 = {NumberPicker_onScrollStateChange};
	
	//22. OnValueChangeListener
	public static final String setOnValueChangeListener = "void setOnValueChangeListener(android.widget.NumberPicker$OnValueChangeListener)";
	public static final String OnValueChangeListener = "android.widget.NumberPicker$OnValueChangeListener";
	public static final String onValueChange = "void onValueChange(android.widget.NumberPicker,int,int)";
	public static final String[] array22 = {ONTOUCH};
	
	//android.widget.AbsListView
	//23. OnScrollListener
	public static final String AbsListView_setOnScrollListener = "void setOnScrollListener(android.widget.AbsListView$OnScrollListener)";
	public static final String AbsListView_OnScrollListener = "android.widget.AbsListView$OnScrollListener";
	public static final String onScrollStateChanged = "void onScrollStateChange(android.widget.AbsListView,int)";
	public static final String onScroll = "void onScroll(android.widget.AbsListView,int,int,int)";
	public static final String[] array23 = {onScrollStateChanged,onScroll};
	
	//android.content.DialogInterface
	//24. OnCancelListener
	public static final String DialogInterface_setOnCancelListener = "void setOnCancelListener(android.content.DialogInterface$OnCancelListener)";
	public static final String DialogInterface_OnCancelListener = "android.content.DialogInterface$OnCancelListener";
	public static final String DialogInterface_onCancel = "void onCancel(android.content.DialogInterface)";
	public static final String[] array24 = {DialogInterface_onCancel};
	
	//25.OnDismissListener
	public static final String DialogInterface_setOnDismissListener = "void setOnDismissListener(android.content.DialogInterface$OnDismissListener)";
	public static final String DialogInterface_OnDismissListener = "android.content.DialogInterface$OnDismissListener";
	public static final String DialogInterface_onDismiss = "void onDismiss(android.content.DialogInterface)";
	public static final String[] array25 = {DialogInterface_onDismiss};
	
	//26.OnShowListener
	public static final String DialogInterface_setOnShowListener = "void setOnShowListener(android.content.DialogInterface$OnShowListener)";
	public static final String DialogInterface_OnShowListener = "android.content.DialogInterface$OnShowListener";
	public static final String DialogInterface_onShow = "void onShow(android.content.DialogInterface)";
	public static final String[] array26 = {DialogInterface_onShow};
	
	//27.OnClickListener
	public static final String DialogInterface_setOnClickListener = "void setOnClickListener(android.content.DialogInterface$OnClickListener)";
	public static final String DialogInterface_OnClickListener = "android.content.DialogInterface$OnClickListener";
	public static final String DialogInterface_onClick = "void onClick(android.content.DialogInterface,int)";
	public static final String[] array27 = {DialogInterface_onClick};
	
	//28. OnMultiChoiceClickListener
	public static final String DialogInterface_setOnMultiChoiceClickListener = "void setOnMultiChoiceClickListener(android.content.DialogInterface$OnMultiChoiceClickListener)";
	public static final String DialogInterface_OnMultiChoiceClickListener = "android.content.DialogInterface$OnMultiChoiceClickListener";
	public static final String DialogInterface_OnMultiChoiceClickListener_onClick = "void onClick(android.content.DialogInterface,int,boolean)";
	public static final String[] array28 = {DialogInterface_OnMultiChoiceClickListener_onClick};
	
	//29.OnKeyListener
	public static final String DialogInterface_setOnKeyListener = "void setOnKeyListener(android.content.DialogInterface$OnKeyListener)";
	public static final String DialogInterface_OnKeyListener = "android.content.DialogInterface$OnKeyListener";
	public static final String DialogInterface_onKey = "boolean onKey(android.content.DialogInterface,int,android.view.KeyEvent)";
	public static final String[] array29 = {DialogInterface_onKey};
	
	//android.support.v4.view.ViewPager
	//30. OnPageChangeListener
	public static final String setOnPageChangeListener = "void setOnPageChangeListener(android.support.v4.view.ViewPager$OnPageChangeListener)";
	public static final String OnPageChangeListener = "android.support.v4.view.ViewPager$OnPageChangeListener";
	public static final String onPageSelected = "void onPageSelected(int)";
	public static final String onPageScrolled = "void onPageScrolled(int,float,int)";
	public static final String onPageScrollStateChanged = "void onPageScrollStateChanged(int)";
	public static final String[] array30 = {onPageSelected,onPageScrolled,onPageScrollStateChanged};
	
	//android.widget.SearchView
	//31. OnQueryTextListener
	public static final String setOnQueryTextListener = "void setOnQueryTextListener(android.widget.SearchView$OnQueryTextListener)";
	public static final String OnQueryTextListener = "android.widget.SearchView$OnQueryTextListener";
	public static final String onQueryTextSubmit = "boolean onQueryTextSubmit(java.lang.String)";
	public static final String onQueryTextChange = "boolean onQueryTextChange(java.lang.String)";
	public static final String[] array31 = {onQueryTextSubmit,onQueryTextChange};
	
	//32. OnCloseListener
	public static final String setOnCloseListener = "void setOnCloseListener(android.widget.SearchView$OnCloseListener)";
	public static final String OnCloseListener = "android.widget.SearchView$OnCloseListener";
	public static final String onClose = "boolean onClose()";
	public static final String[] array32 = {onClose};
	
	//33.OnSuggestionListener
	public static final String setOnSuggestionListener = "void setOnSuggestionListener(android.widget.SearchView$OnSuggestionListener)";
	public static final String OnSuggestionListener = "android.widget.SearchView$OnSuggestionListener";
	public static final String onSuggestionSelect = "boolean onSuggestionSelect(int)";
	public static final String onSuggestionClick = "boolean onSuggestionClick(int)";
	public static final String[] array33 = {onSuggestionSelect,onSuggestionClick};
	
	static{
		genEventTriple();
		callbackAndListener.put(ONKEY, ONKEYLISTENER);
		callbackAndListener.put(ONTOUCH, ONTOUCHLISTENER);
		callbackAndListener.put(ONHOVER, ONHOVERLISTENER);
		callbackAndListener.put(ONGENERICMOTION, ONGENERICMOTIONLISTENER);
		callbackAndListener.put(ONLONGCLICK, ONCLICKLISTENER);
		callbackAndListener.put(ONDRAG, ONDRAGLISTENER);
		callbackAndListener.put(ONFOCUSCHANGE, ONFOCUSCHANGELISTENER);
		callbackAndListener.put(ONCLICK,ONCLICKLISTENER);
		callbackAndListener.put(ONCONTEXTITEMSELECTED, ONCREATECONTEXTMENULISTENER);
		callbackAndListener.put(ONCREATECONTEXTMENU, ONCREATECONTEXTMENULISTENER);
		callbackAndListener.put(ONSYSTEMUIVISIBILITYCHANGE, ONSYSTEMUIVISIBILITYCHANGELISTENER);
		callbackAndListener.put(ONPREFERENCETREECLICK, ONPREFERENCECLICKLISTENER);
	}
	static{
		listenerAndRegistrar.put(ONKEYLISTENER, SETONKEYLISTENER);
		listenerAndRegistrar.put(ONTOUCHLISTENER, SETONTOUCHLISTENER);
		listenerAndRegistrar.put(ONHOVERLISTENER,SETONHOVERLISTENER);
		listenerAndRegistrar.put(ONGENERICMOTIONLISTENER, SETONGENERICMOTIONLISTENER);
		listenerAndRegistrar.put(ONLONGCLICKLISTENER, SETONLONGCLICKLISTENER);
		listenerAndRegistrar.put(ONDRAGLISTENER, SETONDRAGLISTENER);
		listenerAndRegistrar.put(ONFOCUSCHANGELISTENER, SETONFOCUSCHANGELISTENER);
		listenerAndRegistrar.put(ONCLICKLISTENER, SETONCLICKLISTENER);
		listenerAndRegistrar.put(ONCREATECONTEXTMENULISTENER, SETONCREATECONTEXTMENULISTENER);
		listenerAndRegistrar.put(ONSYSTEMUIVISIBILITYCHANGELISTENER, SETONSYSTEMUIVISIBILITYCHANGELISTENER);
		listenerAndRegistrar.put(ONPREFERENCECLICKLISTENER, SETONPREFERENCECLICKLISTENER);
	}
	static{
		registrarAndListener.put(SETONKEYLISTENER, ONKEYLISTENER);
		registrarAndListener.put(SETONTOUCHLISTENER, ONTOUCHLISTENER);
		registrarAndListener.put(SETONHOVERLISTENER, ONHOVERLISTENER);
		registrarAndListener.put(SETONGENERICMOTIONLISTENER, ONGENERICMOTIONLISTENER);
		registrarAndListener.put(SETONLONGCLICKLISTENER, ONLONGCLICKLISTENER);
		registrarAndListener.put(SETONDRAGLISTENER, ONDRAGLISTENER);
		registrarAndListener.put(SETONFOCUSCHANGELISTENER, ONFOCUSCHANGELISTENER);
		registrarAndListener.put(SETONCLICKLISTENER, ONCLICKLISTENER);
		registrarAndListener.put(SETONCREATECONTEXTMENULISTENER, ONCREATECONTEXTMENULISTENER);
		registrarAndListener.put(SETONSYSTEMUIVISIBILITYCHANGELISTENER, ONSYSTEMUIVISIBILITYCHANGELISTENER);
		registrarAndListener.put(SETONPREFERENCECLICKLISTENER, ONPREFERENCECLICKLISTENER);
	}
	
	//store event, eventhandler and callback into eventTriples.
	public static void genEventTriple(){
		viewRegistrarToCallBacks.put("setOnCheckedChangeListener",Arrays.asList(array18));
		viewRegistrarToCallBacks.put("setOnCloseListener",Arrays.asList(array32));
		viewRegistrarToCallBacks.put("setOnInflateListener",Arrays.asList(array20));
		viewRegistrarToCallBacks.put("SETONITEMSELECTEDLISTENER",Arrays.asList(array16));
		viewRegistrarToCallBacks.put("setOnPageChangeListener",Arrays.asList(array30));
		viewRegistrarToCallBacks.put("setOnQueryTextListener",Arrays.asList(array31));
		viewRegistrarToCallBacks.put("setOnRatingBarChangeListener",Arrays.asList(array19));
		viewRegistrarToCallBacks.put("setOnSeekBarChangeListener",Arrays.asList(array17));
		viewRegistrarToCallBacks.put("setOnSuggestionListener",Arrays.asList(array33));
		viewRegistrarToCallBacks.put("setOnValueChangeListener",Arrays.asList(array22));
		viewRegistrarToCallBacks.put("SETONCLICKLISTENER",Arrays.asList(array8));
		viewRegistrarToCallBacks.put("SETONCREATECONTEXTMENULISTENER",Arrays.asList(array9));
		viewRegistrarToCallBacks.put("SETONDRAGLISTENER",Arrays.asList(array6));
		viewRegistrarToCallBacks.put("SETONEDITORACTIONLISTENER",Arrays.asList(array12));
		viewRegistrarToCallBacks.put("SETONFOCUSCHANGELISTENER",Arrays.asList(array7));
		viewRegistrarToCallBacks.put("SETONGENERICMOTIONLISTENER",Arrays.asList(array4));
		viewRegistrarToCallBacks.put("SETONHIERARCHYCHANGELISTENER",Arrays.asList(array13));
		viewRegistrarToCallBacks.put("SETONHOVERLISTENER",Arrays.asList(array3));
		viewRegistrarToCallBacks.put("SETONITEMCLICKLISTENER",Arrays.asList(array14));
		viewRegistrarToCallBacks.put("SETONITEMLONGCLICKLISTENER",Arrays.asList(array15));
		viewRegistrarToCallBacks.put("SETONKEYLISTENER",Arrays.asList(array1));
		viewRegistrarToCallBacks.put("SETONLONGCLICKLISTENER",Arrays.asList(array5));
		viewRegistrarToCallBacks.put("SETONPREFERENCECLICKLISTENER",Arrays.asList(array11));
		viewRegistrarToCallBacks.put("SETONSYSTEMUIVISIBILITYCHANGELISTENER",Arrays.asList(array10));
		viewRegistrarToCallBacks.put("SETONTOUCHLISTENER",Arrays.asList(array2));
		viewRegistrarToCallBacks.put("DialogInterface_setOnCancelListener",Arrays.asList(array24));
		viewRegistrarToCallBacks.put("DialogInterface_setOnClickListener",Arrays.asList(array27));
		viewRegistrarToCallBacks.put("DialogInterface_setOnDismissListener",Arrays.asList(array25));
		viewRegistrarToCallBacks.put("DialogInterface_setOnKeyListener",Arrays.asList(array29));
		viewRegistrarToCallBacks.put("DialogInterface_setOnMultiChoiceClickListener",Arrays.asList(array28));
		viewRegistrarToCallBacks.put("DialogInterface_setOnShowListener",Arrays.asList(array26));
		viewRegistrarToCallBacks.put("NumberPicker_setOnScrollListener",Arrays.asList(array21));
		viewRegistrarToCallBacks.put("AbsListView_setOnScrollListener",Arrays.asList(array23));
		
		eventTriples.add(new EventTriple(setOnCheckedChangeListener, OnCheckedChangeListener,Arrays.asList(array18)));
		eventTriples.add(new EventTriple(setOnCloseListener, OnCloseListener,Arrays.asList(array32)));
		eventTriples.add(new EventTriple(setOnInflateListener, OnInflateListener,Arrays.asList(array20)));
		eventTriples.add(new EventTriple(SETONITEMSELECTEDLISTENER, ONITEMSELECTEDLISTENER,Arrays.asList(array16)));
		eventTriples.add(new EventTriple(setOnPageChangeListener, OnPageChangeListener,Arrays.asList(array30)));
		eventTriples.add(new EventTriple(setOnQueryTextListener, OnQueryTextListener,Arrays.asList(array31)));
		eventTriples.add(new EventTriple(setOnRatingBarChangeListener, OnRatingBarChangeListener,Arrays.asList(array19)));
		eventTriples.add(new EventTriple(setOnSeekBarChangeListener, OnSeekBarChangeListener,Arrays.asList(array17)));
		eventTriples.add(new EventTriple(setOnSuggestionListener, OnSuggestionListener,Arrays.asList(array33)));
		eventTriples.add(new EventTriple(setOnValueChangeListener, OnValueChangeListener,Arrays.asList(array22)));
		eventTriples.add(new EventTriple(SETONCLICKLISTENER, ONCLICKLISTENER,Arrays.asList(array8)));
		eventTriples.add(new EventTriple(SETONCREATECONTEXTMENULISTENER, ONCREATECONTEXTMENULISTENER,Arrays.asList(array9)));
		eventTriples.add(new EventTriple(SETONDRAGLISTENER, ONDRAGLISTENER,Arrays.asList(array6)));
		eventTriples.add(new EventTriple(SETONEDITORACTIONLISTENER, ONEDITORACTIONLISTENER,Arrays.asList(array12)));
		eventTriples.add(new EventTriple(SETONFOCUSCHANGELISTENER, ONFOCUSCHANGELISTENER,Arrays.asList(array7)));
		eventTriples.add(new EventTriple(SETONGENERICMOTIONLISTENER, ONFOCUSCHANGELISTENER,Arrays.asList(array4)));
		eventTriples.add(new EventTriple(SETONHIERARCHYCHANGELISTENER, ONHIERARCHYCHANGELISTENER,Arrays.asList(array13)));
		eventTriples.add(new EventTriple(SETONHOVERLISTENER, ONHOVERLISTENER,Arrays.asList(array3)));
		eventTriples.add(new EventTriple(SETONITEMCLICKLISTENER, ONITEMCLICKLISTENER,Arrays.asList(array14)));
		eventTriples.add(new EventTriple(SETONITEMLONGCLICKLISTENER, ONITEMLONGCLICKLISTENER,Arrays.asList(array15)));
		eventTriples.add(new EventTriple(SETONKEYLISTENER, ONKEYLISTENER,Arrays.asList(array1)));
		eventTriples.add(new EventTriple(SETONLONGCLICKLISTENER, ONLONGCLICKLISTENER,Arrays.asList(array5)));
		eventTriples.add(new EventTriple(SETONPREFERENCECLICKLISTENER, ONPREFERENCECLICKLISTENER,Arrays.asList(array11)));
		eventTriples.add(new EventTriple(SETONSYSTEMUIVISIBILITYCHANGELISTENER, ONSYSTEMUIVISIBILITYCHANGELISTENER,Arrays.asList(array10)));
		eventTriples.add(new EventTriple(SETONTOUCHLISTENER, ONTOUCHLISTENER,Arrays.asList(array2)));
		eventTriples.add(new EventTriple(DialogInterface_setOnCancelListener, DialogInterface_OnCancelListener,Arrays.asList(array24)));
		eventTriples.add(new EventTriple(DialogInterface_setOnClickListener, DialogInterface_OnClickListener,Arrays.asList(array27)));
		eventTriples.add(new EventTriple(DialogInterface_setOnDismissListener, DialogInterface_OnDismissListener,Arrays.asList(array25)));
		eventTriples.add(new EventTriple(DialogInterface_setOnKeyListener, DialogInterface_OnKeyListener,Arrays.asList(array29)));
		eventTriples.add(new EventTriple(DialogInterface_setOnMultiChoiceClickListener, DialogInterface_OnMultiChoiceClickListener,Arrays.asList(array28)));
		eventTriples.add(new EventTriple(DialogInterface_setOnShowListener, DialogInterface_OnShowListener,Arrays.asList(array26)));
		eventTriples.add(new EventTriple(NumberPicker_setOnScrollListener, NumberPicker_OnScrollListener,Arrays.asList(array21)));
		eventTriples.add(new EventTriple(AbsListView_setOnScrollListener, AbsListView_OnScrollListener,Arrays.asList(array23)));
	}
	
	public static Map<String, String> getRegistrarAndlistener() {
		return registrarAndListener;
	}

	public static Map<String, String> getCallbackAndListener() {
		return callbackAndListener;
	}

	public static Map<String, String> getListenerAndListenerMethod() {
		return listenerAndRegistrar;
	}
	
	public static List<EventTriple> getEventTriples() {
		return eventTriples;
	}

}
