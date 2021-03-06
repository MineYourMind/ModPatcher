package me.nallar.util;

import com.sun.management.UnixOperatingSystemMXBean;
import me.nallar.modpatcher.PatcherLog;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class EnvironmentInfo {
	public static String getJavaVersion() {
		return ManagementFactory.getRuntimeMXBean().getSpecVersion();
	}

	public static String getOpenFileHandles() {
		OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
		if (osMxBean instanceof UnixOperatingSystemMXBean) {
			UnixOperatingSystemMXBean unixOsMxBean = (UnixOperatingSystemMXBean) osMxBean;
			return unixOsMxBean.getOpenFileDescriptorCount() + " / " + unixOsMxBean.getMaxFileDescriptorCount();
		}
		return "unknown";
	}

	public static void checkOpenFileHandles() {
		OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
		if (osMxBean instanceof UnixOperatingSystemMXBean) {
			UnixOperatingSystemMXBean unixOsMxBean = (UnixOperatingSystemMXBean) osMxBean;
			long used = unixOsMxBean.getOpenFileDescriptorCount();
			long available = unixOsMxBean.getMaxFileDescriptorCount();
			if (used != 0 && available != 0 && (used > (available * 17) / 20 || (available - used) < 3)) {
				PatcherLog.error("Used >= 85% of available file handles: " + getOpenFileHandles());
			}
		}
	}
}
