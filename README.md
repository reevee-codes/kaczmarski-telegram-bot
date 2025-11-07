# Kaczmarski Bot

This is a work in progress project, but feel free to contribute.
A Telegram bot that emulates Jacek Kaczmarski using GPT-4 (possible to change the model!) allowing users to have conversations with an AI version of the famous Polish bard. The bot responds in Polish, maintaining Kaczmarski's characteristic style and deep knowledge of history, literature, and art.

## Features

- Chat with AI Jacek Kaczmarski using `/bot` command
- End conversations using `/end` command
- Send text and photo responses
- Maintains conversation context
- Responds in Polish language
- References Kaczmarski's songs, poems, and historical events

## Prerequisites

- Java 17 or higher
- Maven
- OpenAI API key
- Telegram Bot Token

## Setup

1. **Clone the repository**
bash
git clone [your-repository-url]
cd kaczmarski-bot

2. **Configure API Keys**
   - Create `src/main/resources/keys.properties`:
   ```properties
   openai.api.key=your-openai-api-key-here
   ```
   - Get your OpenAI API key from [OpenAI Platform](https://platform.openai.com/api-keys)

3. **Configure Bot Token**
   - Update `KaczmarskiBot.java` with your Telegram bot token:
   ```java
   private static final String BOT_TOKEN = "your-telegram-bot-token-here";
   private static final String BOT_USERNAME = "your-bot-username";
   ```
   - Get your Telegram bot token from [BotFather](https://t.me/botfather)

4. **Build the project**

```bash
mvn clean install
```

5. **Run the bot**
```bash
mvn exec:java -Dexec.mainClass="utils.BotInitializer"
```

## Usage

1. Start the bot in Telegram
2. Available commands:
   - `/start` - Welcome message
   - `/help` - List available commands
   - `/bot` - Start conversation with AI Kaczmarski
   - `/end` - End conversation
   - `/info` - Get information about the bot

## Configuration Files

- `src/main/resources/commands.properties` - Define bot commands and responses
- `src/main/resources/keys.properties` - Store API keys (do not commit this file)
- `src/main/resources/logback.xml` - Logging configuration

## Security Notes

1. Never commit your API keys to version control
2. Add `keys.properties` to your `.gitignore`:
```
src/main/resources/keys.properties
```

## Troubleshooting

1. **OpenAI API Issues**
   - Ensure your API key is valid
   - Check if you have access to GPT-4 (fallback to GPT-3.5-turbo if needed)
   - Monitor API usage and limits

2. **Telegram Bot Issues**
   - Verify bot token is correct
   - Ensure bot has required permissions
   - Check internet connectivity

3. **Build Issues**
   - Ensure Java 17+ is installed
   - Verify all dependencies in `pom.xml`
   - Clear Maven cache if needed: `mvn clean`

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

 GNU General Public License (GPL)

## Acknowledgments

- Jacek Kaczmarski's legacy and works
- OpenAI for GPT models
- Telegram Bot API
