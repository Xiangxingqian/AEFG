/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import android.content.IntentFilter.MalformedMimeTypeException;
import android.os.PatternMatcher;

public class AndroidIntentFilter implements Serializable{

    private final ArrayList<String> mActions;
    private ArrayList<String> mCategories = null;
    private ArrayList<String> mDataSchemes = null;
    private ArrayList<PatternMatcher> mDataPaths = null;
    private ArrayList<String> mDataTypes = null;

    /**
     * New empty IntentFilter.
     */
    public AndroidIntentFilter() {
        mActions = new ArrayList<String>();
    }

    /**
     * New IntentFilter that matches a single action with no data.  If
     * no data characteristics are subsequently specified, then the
     * filter will only match intents that contain no data.
     *
     * @param action The action to match, i.e. Intent.ACTION_MAIN.
     */
    public AndroidIntentFilter(String action) {
        mActions = new ArrayList<String>();
        addAction(action);
    }

    /**
     * New IntentFilter containing a copy of an existing filter.
     *
     * @param o The original filter to copy.
     */
    public AndroidIntentFilter(AndroidIntentFilter o) {
        mActions = new ArrayList<String>(o.mActions);
        if (o.mCategories != null) {
            mCategories = new ArrayList<String>(o.mCategories);
        }
        if (o.mDataTypes != null) {
            mDataTypes = new ArrayList<String>(o.mDataTypes);
        }
        if (o.mDataSchemes != null) {
            mDataSchemes = new ArrayList<String>(o.mDataSchemes);
        }
        if (o.mDataPaths != null) {
            mDataPaths = new ArrayList<PatternMatcher>(o.mDataPaths);
        }
    }


    /**
     * Add a new Intent action to match against.  If any actions are included
     * in the filter, then an Intent's action must be one of those values for
     * it to match.  If no actions are included, the Intent action is ignored.
     *
     * @param action Name of the action to match, i.e. Intent.ACTION_VIEW.
     */
    public final void addAction(String action) {
        if (!mActions.contains(action)) {
            mActions.add(action.intern());
        }
    }

    /**
     * Return the number of actions in the filter.
     */
    public final int countActions() {
        return mActions.size();
    }

    /**
     * Return an action in the filter.
     */
    public final String getAction(int index) {
        return mActions.get(index);
    }

    /**
     * Is the given action included in the filter?  Note that if the filter
     * does not include any actions, false will <em>always</em> be returned.
     *
     * @param action The action to look for.
     *
     * @return True if the action is explicitly mentioned in the filter.
     */
    public final boolean hasAction(String action) {
        return action != null && mActions.contains(action);
    }

    /**
     * Return an iterator over the filter's actions.  If there are no actions,
     * returns null.
     */
    public final Iterator<String> actionsIterator() {
        return mActions != null ? mActions.iterator() : null;
    }

    /**
     * Add a new Intent data type to match against.  If any types are
     * included in the filter, then an Intent's data must be <em>either</em>
     * one of these types <em>or</em> a matching scheme.  If no data types
     * are included, then an Intent will only match if it specifies no data.
     *
     * <p><em>Note: MIME type matching in the Android framework is
     * case-sensitive, unlike formal RFC MIME types.  As a result,
     * you should always write your MIME types with lower case letters,
     * and any MIME types you receive from outside of Android should be
     * converted to lower case before supplying them here.</em></p>
     *
     * <p>Throws {@link MalformedMimeTypeException} if the given MIME type is
     * not syntactically correct.
     *
     * @param type Name of the data type to match, i.e. "vnd.android.cursor.dir/person".
     *
     * @see #matchData
     */
    public final void addDataType(String type){
        final int slashpos = type.indexOf('/');
        final int typelen = type.length();
        if (slashpos > 0 && typelen >= slashpos+2) {
            if (mDataTypes == null) mDataTypes = new ArrayList<String>();
            if (typelen == slashpos+2 && type.charAt(slashpos+1) == '*') {
                String str = type.substring(0, slashpos);
                if (!mDataTypes.contains(str)) {
                    mDataTypes.add(str.intern());
                }
            } else {
                if (!mDataTypes.contains(type)) {
                    mDataTypes.add(type.intern());
                }
            }
            return;
        }
    }

    /**
     * Is the given data type included in the filter?  Note that if the filter
     * does not include any type, false will <em>always</em> be returned.
     *
     * @param type The data type to look for.
     *
     * @return True if the type is explicitly mentioned in the filter.
     */
    public final boolean hasDataType(String type) {
        return mDataTypes != null && findMimeType(type);
    }

    /**
     * Return the number of data types in the filter.
     */
    public final int countDataTypes() {
        return mDataTypes != null ? mDataTypes.size() : 0;
    }

    /**
     * Return a data type in the filter.
     */
    public final String getDataType(int index) {
        return mDataTypes.get(index);
    }

    /**
     * Return an iterator over the filter's data types.
     */
    public final Iterator<String> typesIterator() {
        return mDataTypes != null ? mDataTypes.iterator() : null;
    }

    /**
     * Add a new Intent data scheme to match against.  If any schemes are
     * included in the filter, then an Intent's data must be <em>either</em>
     * one of these schemes <em>or</em> a matching data type.  If no schemes
     * are included, then an Intent will match only if it includes no data.
     *
     * <p><em>Note: scheme matching in the Android framework is
     * case-sensitive, unlike formal RFC schemes.  As a result,
     * you should always write your schemes with lower case letters,
     * and any schemes you receive from outside of Android should be
     * converted to lower case before supplying them here.</em></p>
     *
     * @param scheme Name of the scheme to match, i.e. "http".
     *
     * @see #matchData
     */
    public final void addDataScheme(String scheme) {
        if (mDataSchemes == null) mDataSchemes = new ArrayList<String>();
        if (!mDataSchemes.contains(scheme)) {
            mDataSchemes.add(scheme.intern());
        }
    }

    /**
     * Return the number of data schemes in the filter.
     */
    public final int countDataSchemes() {
        return mDataSchemes != null ? mDataSchemes.size() : 0;
    }

    /**
     * Return a data scheme in the filter.
     */
    public final String getDataScheme(int index) {
        return mDataSchemes.get(index);
    }

    /**
     * Is the given data scheme included in the filter?  Note that if the
     * filter does not include any scheme, false will <em>always</em> be
     * returned.
     *
     * @param scheme The data scheme to look for.
     *
     * @return True if the scheme is explicitly mentioned in the filter.
     */
    public final boolean hasDataScheme(String scheme) {
        return mDataSchemes != null && mDataSchemes.contains(scheme);
    }

    /**
     * Return an iterator over the filter's data schemes.
     */
    public final Iterator<String> schemesIterator() {
        return mDataSchemes != null ? mDataSchemes.iterator() : null;
    }


    /**
     * Add a new Intent category to match against.  The semantics of
     * categories is the opposite of actions -- an Intent includes the
     * categories that it requires, all of which must be included in the
     * filter in order to match.  In other words, adding a category to the
     * filter has no impact on matching unless that category is specified in
     * the intent.
     *
     * @param category Name of category to match, i.e. Intent.CATEGORY_EMBED.
     */
    public final void addCategory(String category) {
        if (mCategories == null) mCategories = new ArrayList<String>();
        if (!mCategories.contains(category)) {
            mCategories.add(category.intern());
        }
    }

    /**
     * Return the number of categories in the filter.
     */
    public final int countCategories() {
        return mCategories != null ? mCategories.size() : 0;
    }

    /**
     * Return a category in the filter.
     */
    public final String getCategory(int index) {
        return mCategories.get(index);
    }

    /**
     * Is the given category included in the filter?
     *
     * @param category The category that the filter supports.
     *
     * @return True if the category is explicitly mentioned in the filter.
     */
    public final boolean hasCategory(String category) {
        return mCategories != null && mCategories.contains(category);
    }

    /**
     * Return an iterator over the filter's categories.
     *
     * @return Iterator if this filter has categories or {@code null} if none.
     */
    public final Iterator<String> categoriesIterator() {
        return mCategories != null ? mCategories.iterator() : null;
    }

    /**
     * Match this filter against an Intent's categories.  Each category in
     * the Intent must be specified by the filter; if any are not in the
     * filter, the match fails.
     *
     * @param categories The categories included in the intent, as returned by
     *                   Intent.getCategories().
     *
     * @return If all categories match (success), null; else the name of the
     *         first category that didn't match.
     */
    public final String matchCategories(Set<String> categories) {
        if (categories == null) {
            return null;
        }

        Iterator<String> it = categories.iterator();

        if (mCategories == null) {
            return it.hasNext() ? it.next() : null;
        }

        while (it.hasNext()) {
            final String category = it.next();
            if (!mCategories.contains(category)) {
                return category;
            }
        }

        return null;
    }



    private final boolean findMimeType(String type) {
        final ArrayList<String> t = mDataTypes;

        if (type == null) {
            return false;
        }

        if (t.contains(type)) {
            return true;
        }

        // Deal with an Intent wanting to match every type in the IntentFilter.
        final int typeLength = type.length();
        if (typeLength == 3 && type.equals("*/*")) {
            return !t.isEmpty();
        }

        // Deal with this IntentFilter wanting to match every Intent type.
        if (t.contains("*")) {
            return true;
        }

        final int slashpos = type.indexOf('/');
        if (slashpos > 0) {
            if (t.contains(type.substring(0, slashpos))) {
                return true;
            }
            if (typeLength == slashpos+2 && type.charAt(slashpos+1) == '*') {
                // Need to look through all types for one that matches
                // our base...
                final int numTypes = t.size();
                for (int i = 0; i < numTypes; i++) {
                    final String v = t.get(i);
                    if (type.regionMatches(0, v, 0, slashpos+1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

	@Override
	public String toString() {
		return "mActions=" + mActions + "\n mCategories="
				+ mCategories + "\n mDataSchemes=" + mDataSchemes
				+ "\n mDataTypes=" + mDataTypes;
	}

	public ArrayList<String> getmCategories() {
		return mCategories;
	}

	public void setmCategories(ArrayList<String> mCategories) {
		this.mCategories = mCategories;
	}

	public ArrayList<String> getmDataSchemes() {
		return mDataSchemes;
	}

	public void setmDataSchemes(ArrayList<String> mDataSchemes) {
		this.mDataSchemes = mDataSchemes;
	}

	public ArrayList<PatternMatcher> getmDataPaths() {
		return mDataPaths;
	}

	public void setmDataPaths(ArrayList<PatternMatcher> mDataPaths) {
		this.mDataPaths = mDataPaths;
	}

	public ArrayList<String> getmDataTypes() {
		return mDataTypes;
	}

	public void setmDataTypes(ArrayList<String> mDataTypes) {
		this.mDataTypes = mDataTypes;
	}

	public ArrayList<String> getmActions() {
		return mActions;
	}
    
    
}