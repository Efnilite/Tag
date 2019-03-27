package com.efnilite.tag.utils;

import com.efnilite.tag.Tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class UpdateChecker {

	public void check() {
		if (!Tag.getInstance().getDescription().getVersion().equals(getLatestVersion())) {
			Tag.getInstance().getLogger().info("A new version of Tag is available to download!");
			Tag.getInstance().getLogger().info("Download link: https://github.com/Efnilite/Tag/releases/");
		}
	}

	private String getLatestVersion() {
		InputStream stream;
		try {
			stream = new URL("https://raw.githubusercontent.com/Efnilite/Tag/master/src/main/resources/plugin.yml").openStream();
		} catch (IOException e) {
			Tag.getInstance().getLogger().info("Unable to check for updates!");
			return "";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines()
				.filter(s -> s.contains("version: "))
				.collect(Collectors.toList())
				.get(0)
				.replace("version: ", "");
	}
}