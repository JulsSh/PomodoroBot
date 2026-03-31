package ru.skillbox.config;
import io.github.cdimascio.dotenv.Dotenv;
public class ConfigReaderEnvironment implements ConfigReader {
    @Override
    public Config read() {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("BOT_TOKEN");
        return new Config(token);
    }
}