package me.magnum.il2mapintegrator.core;

/**
 * Defines a component that can have multiple versions. The version can then be used to check if the
 * current component meets the version requirement for other components to use it.
 */
interface IVersionable {
	/**
	 * @return the integer code that identifies the current version of the versionable.
	 */
	int versionCode();

	/**
	 * @return a human-readable version of the versionable.
	 */
	String version();
}
