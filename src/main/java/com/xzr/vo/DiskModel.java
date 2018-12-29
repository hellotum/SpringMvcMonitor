package com.xzr.vo;

/**
 * 文件系统:成员变量的命名方式是为了对应jQuery.flot的图表属性的名称
 */
public class DiskModel {

	private double availableZoo;// 可用空间
	private double unavailableZoo;// 不可用空间
	private String diskName;// 盘符

	public double getAvailableZoo() {
		return availableZoo;
	}

	public void setAvailableZoo(double availableZoo) {
		this.availableZoo = availableZoo;
	}

	public double getUnavailableZoo() {
		return unavailableZoo;
	}

	public void setUnavailableZoo(double unavailableZoo) {
		this.unavailableZoo = unavailableZoo;
	}

	public String getDiskName() {
		return diskName;
	}

	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}

}
