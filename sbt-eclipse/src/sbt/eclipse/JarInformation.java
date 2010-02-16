package sbt.eclipse;

import java.io.File;

/**
 * Identifies a JAR file by its name and size.
 */
public class JarInformation implements Comparable<JarInformation> {

	private final String name;
	private final long size;

	public static JarInformation fromFile(File file) {
		return new JarInformation(file.getName(), file.length());
	}

	public JarInformation(String name, long size) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (size ^ (size >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JarInformation other = (JarInformation) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	public int compareTo(JarInformation o) {
		if (o == null)
			return 1;
		return this.name.compareTo(o.name);
	}

}
