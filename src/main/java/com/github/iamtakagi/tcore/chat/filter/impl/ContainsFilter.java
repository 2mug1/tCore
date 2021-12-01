package com.github.iamtakagi.tcore.chat.filter.impl;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.chat.filter.ChatFilter;

public class ContainsFilter extends ChatFilter {

	private final String phrase;

	public ContainsFilter(Core instance, String phrase) {
		this(instance, phrase, null);
	}

	public ContainsFilter(Core instance, String phrase, String command) {
		super(instance, command);
		this.phrase = phrase;
	}

	@Override
	public boolean isFiltered(String message, String[] words) {
		for (String word : words) {
			if (word.contains(this.phrase)) {
				return true;
			}
		}

		return false;
	}

}
