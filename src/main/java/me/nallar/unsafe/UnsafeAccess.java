package me.nallar.unsafe;

import me.nallar.modpatcher.PatcherLog;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeAccess {
	public static final Unsafe $;

	static {
		Unsafe temp = null;
		try {
			Field theUnsafe = Class.forName("sun.misc.Unsafe").getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			temp = (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			PatcherLog.error("Failed to get unsafe", e);
		}
		$ = temp;
	}
}
