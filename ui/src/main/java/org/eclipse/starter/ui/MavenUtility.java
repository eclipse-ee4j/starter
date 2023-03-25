package org.eclipse.starter.ui;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.maven.cli.MavenCli;

public class MavenUtility {

	public static void invokeMavenArchetype(String archetypeGroupId, String archetypeArtifactId,
			String archetypeVersion, Properties properties, File workingDirectory) {
		System.setProperty(MavenCli.MULTIMODULE_PROJECT_DIRECTORY, workingDirectory.getAbsolutePath());

		List<String> options = new LinkedList<>();
		options.addAll(Arrays.asList(new String[] { "archetype:generate", "-DinteractiveMode=false",
				"-DaskForDefaultPropertyValues=false", "-DarchetypeGroupId=" + archetypeGroupId,
				"-DarchetypeArtifactId=" + archetypeArtifactId, "-DarchetypeVersion=" + archetypeVersion }));
		properties.forEach((k, v) -> options.add("-D" + k + "=" + v));

		int result = new MavenCli().doMain(options.toArray(new String[0]), workingDirectory.getAbsolutePath(),
				System.out, System.err);

		if (result != 0) {
			throw new RuntimeException("Failed to invoke Maven Archetype.");
		}
	}
}
