package com.github.iamtakagi.tcore.strap;

import com.github.iamtakagi.tcore.Core;
import lombok.Getter;

@Getter
public class Strapped {

	protected final Core instance;

	public Strapped(Core instance) {
		this.instance = instance;
	}

}
