package hu.gerviba.easyymlapi.utils;

public class TestLocation {

	private String world;
	private double x, y, z;
	private float yaw, pitch;

	public TestLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0.0F;
		this.pitch = 0.0F;
		this.setWorld("null");
	}
	
	public TestLocation(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.setWorld("null");
	}

	public TestLocation(String world, double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.setWorld(world);
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	@Override
	public String toString() {
		return "TestLocation [world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch="
				+ pitch + "]";
	}
	
}
