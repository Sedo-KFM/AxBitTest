package com.sedo.AxBitTest.helpers;

import org.hibernate.collection.internal.PersistentSet;

import java.util.HashSet;
import java.util.Set;

public class SetHelper {
	static public <T> Set<T> SetDifference(Set<T> set1, Set<T> set2) {
		Set<T> diff1 = new HashSet<T>(set1);
		diff1.removeAll(set2);
		Set<T> diff2 = new HashSet<T>(set2);
		diff2.removeAll(set1);
		Set<T> diff = new HashSet<T>();
		diff.addAll(diff1);
		diff.addAll(diff2);
		return diff;
	}
}
