package com.github.iamtakagi.tcore.io.file.language;

import com.github.iamtakagi.tcore.io.file.AbstractConfigurationFile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LanguageConfigurationFile extends AbstractConfigurationFile
{
    private static final LanguageConfigurationFileLocale DEFAULT_LOCALE = LanguageConfigurationFileLocale.ENGLISH;
    public Map<LanguageConfigurationFileLocale, YamlConfiguration> getConfigurations() { return this.configurations; }

    private final Map<LanguageConfigurationFileLocale, YamlConfiguration> configurations;
    public LanguageConfigurationFile(JavaPlugin plugin, String name, boolean overwrite) { super(plugin, name);

        this.configurations = new java.util.HashMap();
        for (LanguageConfigurationFileLocale locale : LanguageConfigurationFileLocale.values()) {
            File file = new File(plugin.getDataFolder(), name + "_" + locale.getAbbreviation() + ".yml");

            String path = name + "_" + locale.getAbbreviation() + ".yml";
            if (plugin.getResource(path) != null) {
                plugin.saveResource(path, overwrite);
                this.configurations.put(locale, YamlConfiguration.loadConfiguration(file));
            }
        }
    }

    public LanguageConfigurationFile(JavaPlugin plugin, String name)
    {
        this(plugin, name, false);
    }

    public List<String> replace(List<String> list, int position, Object argument) {
        List<String> toReturn = new ArrayList();

        for (String string : list) {
            toReturn.add(string.replace("{" + position + "}", argument.toString()));
        }

        return toReturn;
    }

    public List<String> replace(List<String> list, int position, Object... arguments) {
        return replace(list, 0, position, arguments);
    }

    public List<String> replace(List<String> list, int index, int position, Object... arguments) {
        List<String> toReturn = new ArrayList();

        for (String string : list) {
            for (int i = 0; i < arguments.length; i++) {
                toReturn.add(string.replace("{" + position + "}", arguments[(index + i)].toString()));
            }
        }

        return toReturn;
    }

    public List<String> getStringListWithArgumentsOrRemove(String path, LanguageConfigurationFileLocale locale, Object... arguments) {
        List<String> toReturn = new ArrayList();

        for (String string : getStringList(path, locale)) {
            for (int i = 0;; i++) { if (i >= arguments.length) break;
                if (string.contains("{" + i + "}")) {
                    Object object = arguments[i];
                    if (object == null) break;
                    if ((object instanceof List)) {
                        for (Object obj : (List)object) {
                            if ((obj instanceof String)) {
                                toReturn.add((String)obj);
                            }
                        }
                        break;
                    }
                    string = string.replace("{" + i + "}", object.toString());
                }
            }



            toReturn.add(string);
        }
        label207:
        return toReturn;
    }

    public int indexOf(List<String> list, int position) {
        for (int i = 0; i < list.size(); i++) {
            if (((String)list.get(i)).contains("{" + position + "}")) {
                return i;
            }
        }
        return -1;
    }

    public String getString(String path, LanguageConfigurationFileLocale locale) {
        if (!this.configurations.containsKey(locale)) {
            return locale == DEFAULT_LOCALE ? null : getString(path, DEFAULT_LOCALE);
        }

        YamlConfiguration configuration = (YamlConfiguration)this.configurations.get(locale);
        if (configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));
        }

        return null;
    }

    public String getString(String path, LanguageConfigurationFileLocale locale, Object... arguments) {
        String toReturn = getString(path, locale);

        if (toReturn != null) {
            for (int i = 0; i < arguments.length; i++) {
                toReturn = toReturn.replace("{" + i + "}", arguments[i].toString());
            }

            return toReturn;
        }

        return null;
    }

    public String getString(String path)
    {
        return getString(path, DEFAULT_LOCALE);
    }

    public String getStringOrDefault(String path, String or, LanguageConfigurationFileLocale locale) {
        String toReturn = getString(path, locale);

        if (toReturn == null) {
            return or;
        }

        return toReturn;
    }

    public String getStringOrDefault(String path, String or)
    {
        return getStringOrDefault(path, or, DEFAULT_LOCALE);
    }

    public int getInteger(String path)
    {
        throw new UnsupportedOperationException("");
    }

    @Deprecated
    public double getDouble(String path)
    {
        throw new UnsupportedOperationException("");
    }

    @Deprecated
    public Object get(String path)
    {
        throw new UnsupportedOperationException("");
    }

    public List<String> getStringList(String path, LanguageConfigurationFileLocale locale, Object... arguments) {
        List<String> toReturn = new ArrayList();

        for (String line : getStringList(path, locale)) {
            for (int i = 0;; i++) { if (i >= arguments.length) break;
                Object object = arguments[i];
                if (((object instanceof List)) && (line.contains("{" + i + "}")))
                {
                    for (Object obj : (List)object) {
                        if ((obj instanceof String)) {
                            toReturn.add(line.replace(new StringBuilder().append("{").append(i).append("}").toString(), "") + obj);
                        }
                    }

                    break;
                }
                line = line.replace("{" + i + "}", arguments[i].toString());
            }

            toReturn.add(line);
        }
        label249:
        return toReturn;
    }

    public List<String> getStringList(String path, LanguageConfigurationFileLocale locale) {
        if (!this.configurations.containsKey(locale)) {
            return locale == DEFAULT_LOCALE ? null : getStringList(path, DEFAULT_LOCALE);
        }

        YamlConfiguration configuration = (YamlConfiguration)this.configurations.get(locale);
        if (configuration.contains(path)) {
            List<String> toReturn = new ArrayList();

            for (String string : configuration.getStringList(path)) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', string));
            }

            return toReturn;
        }

        return java.util.Collections.emptyList();
    }

    public List<String> getStringList(String path)
    {
        return getStringList(path, DEFAULT_LOCALE);
    }
}