package com.potatonium.detection;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PotatoDetectionEngine {
	private long totalRamBytes;
	private long processorCount;
	private int currentCPUMHz = 931; // Default to throttled state
	private boolean isBatteryPowered;
	private boolean isLowEndGPU;
	private boolean isHDDStorage;
	private long hddReadSpeed = 74; // MB/s

	public void detectHardware() {
		detectRAM();
		detectCPU();
		detectBattery();
		detectGPU();
		detectStorage();

		if (shouldEnablePotatoMode()) {
			Potatonium.Potatonium.CONFIG.potatoMode = true;
		}
	}

	private void detectRAM() {
		OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		totalRamBytes = osBean.getTotalPhysicalMemorySize();
	}

	private void detectCPU() {
		operatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		processorCount = osBean.getAvailableProcessors();

		// Detect CPU throttling
		try {
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				Process process = Runtime.getRuntime().exec("powercfg /query scheme_current");
				// This is simplified - real implementation would parse CPU frequency
				process.waitFor();
			}
		} catch (Exception e) {
			Potatonium.Potatonium.LOGGER.warn("Could not detect CPU throttling", e);
		}
	}

	private void detectBattery() {
		try {
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				Process process = Runtime.getRuntime().exec("powercfg /query SCHEME_CURRENT SUB_BATTERY");
				String output = new String(process.getInputStream().readAllBytes());
				isBatteryPowered = output.contains("Battery");
				if (isBatteryPowered) {
					Potatonium.Potatonium.LOGGER.warn("Battery power detected - CPU may be throttled!");
					currentCPUMHz = 931;
				}
			}
		} catch (Exception e) {
			Potatonium.Potatonium.LOGGER.debug("Could not detect battery status", e);
		}
	}

	private void detectGPU() {
		try {
			String gpuInfo = System.getenv("PROCESSOR_IDENTIFIER");
			if (gpuInfo != null) {
				isLowEndGPU = gpuInfo.contains("Ironlake") || gpuInfo.contains("HD Graphics") ||
				             gpuInfo.contains("Intel HD") || gpuInfo.contains("UHD Graphics");
			}
			
			// Check OpenGL version
			String glVersion = System.getenv("OPENGL_VERSION");
			if (glVersion != null && glVersion.startsWith("2")) {
				isLowEndGPU = true;
			}
		} catch (Exception e) {
			Potatonium.Potatonium.LOGGER.debug("Could not detect GPU", e);
			isLowEndGPU = true; // Assume low-end to be safe
		}
	}

	private void detectStorage() {
		try {
			long freeSpace = new java.io.File(".").getFreeSpace();
			// If we can detect boot drive type
			isHDDStorage = true; // Assume HDD for low-end systems
		} catch (Exception e) {
			Potatonium.Potatonium.LOGGER.debug("Could not detect storage type", e);
		}
	}

	private boolean shouldEnablePotatoMode() {
		long ramGB = totalRamBytes / (1024L * 1024L * 1024L);
		return ramGB < 4 || isLowEndGPU || isHDDStorage || processorCount < 4;
	}

	public long getTotalRamGB() {
		return totalRamBytes / (1024L * 1024L * 1024L);
	}

	public int getCurrentCPUMHz() {
		return currentCPUMHz;
	}

	public boolean isBatteryThrottled() {
		return isBatteryPowered && currentCPUMHz < 2500;
	}

	public boolean isLowEndGPU() {
		return isLowEndGPU;
	}

	public boolean isHDDStorage() {
		return isHDDStorage;
	}

	public long getHDDReadSpeed() {
		return hddReadSpeed;
	}

	public long getProcessorCount() {
		return processorCount;
	}
}
