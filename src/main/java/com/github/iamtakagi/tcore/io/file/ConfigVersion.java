package com.github.iamtakagi.tcore.io.file;

import com.github.iamtakagi.tcore.io.file.impl.ConfigConversion3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.github.iamtakagi.tcore.io.file.impl.ConfigConversion1;
import com.github.iamtakagi.tcore.io.file.impl.ConfigConversion2;

@AllArgsConstructor
@Getter
public enum ConfigVersion {

	VERSION_1(1, new ConfigConversion1()),
	VERSION_2(2, new ConfigConversion2()),
	VERSION_3(3, new ConfigConversion3());

	private int number;
	private ConfigConversion conversion;

}
