package com.followapp.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AudioTemplateLoader {

	private static final Logger LOG = LoggerFactory.getLogger(AudioTemplateLoader.class);

	private Map<String, List<String>> languageTemplates;
	
	private String basePath;
	private String absolutePath;
	
	private final String defaultLanguage = "hindi";
	
	public AudioTemplateLoader(
			@Value("${app.audioResource.basePath}") String basePath,
			@Value("${app.audioResource.absolutePath}") String absolutePath,
			@Value("${app.audioResource.languages}") String languages) throws IOException {

		if (languages == null) {
			languages = defaultLanguage;
		}

		String[] languageArray = languages.split(",");

		this.basePath = basePath;
		this.absolutePath = absolutePath;
		
		languageTemplates = new HashMap<>();
//TODO:[paikar]I'll update and fix this one later today
//		for (String language : languageArray) {
//			Resource resource = new ClassPathResource("config/languageTemplates/" + language + ".txt");
//			BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//
//			languageTemplates.put(language, br.lines().collect(Collectors.toList()));
//		}
	}

	public String generate(Map<String, String> replacementMap, String maybeLanguage) {

		if (!languageTemplates.containsKey(maybeLanguage)) {
			LOG.warn("We do not have a template file for " + maybeLanguage + ". Using the default template.");
		}

		final String language = languageTemplates.containsKey(maybeLanguage) ? maybeLanguage : defaultLanguage;

		List<String> languageTemplate = languageTemplates.get(language);

		List<String> audioFileNames = languageTemplate.stream()
													  .map((word) -> getFileNameForWord(word, replacementMap, language))
													  .collect(Collectors.toList());

		// Log out the file names that don't 
		audioFileNames.stream()
					  .map(this::createFilePath)
					  .filter(Files::notExists)
					  .forEach(filePath -> LOG.error("Did not find a file at location " + filePath));
		
		return generateUrlList(audioFileNames);
	}

	public Set<String> getLoadedLanguages() {
		return languageTemplates.keySet();
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	private String getFileNameForWord(String word, Map<String, String> replacementMap, String language) {
		if (isPlaceholder(word)) {
			String placeholder = stripPlaceholderTokens(word);
			String audioFileName = replacementMap.getOrDefault(placeholder, "");

			if (isLanguageSpecific(placeholder)) {
				audioFileName = language + "/" + audioFileName;
			}
			return audioFileName + ".wav";
		}
		return language + "/" + word + ".wav";
	}
	
	private String generateUrlList(List<String> audioFileNames) {
		List<String> audioUrls = audioFileNames.stream()
											   .map(x -> createAudioUrl(x))
											   .collect(Collectors.toList());
		return String.join("\n", audioUrls);
	}

	/**
	 * Create an audio URL from the given word, by prepending the storage
	 * solution path to it, and appending the .wav file format to it
	 * 
	 * @param word
	 *            The word for which the audio is required
	 * @return A URL pointing to the path that MAY contain the audio resource
	 */
	private String createAudioUrl(String word) {
		URL wordUrl = null;
		try {
			wordUrl = new URL(basePath + word.replace(' ', '_').toLowerCase());
		} catch (MalformedURLException mue) {
			LOG.error("Exception while creating URL for " + word + ". " + mue.getMessage(), mue);
		}
		return wordUrl.toString();
	}
	
	private Path createFilePath(String audioFileName) {
		return Paths.get(absolutePath + audioFileName.replace(' ', '_').toLowerCase());
	}

	private boolean isPlaceholder(String word) {
		return word.startsWith("{{") && word.endsWith("}}");
	}

	private String stripPlaceholderTokens(String placeholder) {
		// Remove {{ and }} from the placeholder
		return placeholder.substring(2, placeholder.length() - 2);
	}

	private boolean isLanguageSpecific(String placeholder) {
		return !placeholder.toLowerCase().contains("name");
	}
}
