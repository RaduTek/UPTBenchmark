package testbench;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Locale;

public class SysInfoUtil {
    public static void printSystemInfo() {
        System.out.println("System information:");

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        System.out.println("Operating System: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("Architecture: " + System.getProperty("os.arch"));
        System.out.println("===========================");
        System.out.println("Available processors (cores): " + osBean.getAvailableProcessors());

        com.sun.management.OperatingSystemMXBean sunOSBean =
                (com.sun.management.OperatingSystemMXBean) osBean;
        long totalPhysicalMemorySize = sunOSBean.getTotalPhysicalMemorySize();
        long freePhysicalMemorySize = sunOSBean.getFreePhysicalMemorySize();

        System.out.printf(Locale.US, "Total Physical Memory: %.2f GB%n", totalPhysicalMemorySize / 1e9);
        System.out.printf(Locale.US, "Free Physical Memory: %.2f GB%n", freePhysicalMemorySize / 1e9);
    }
}
