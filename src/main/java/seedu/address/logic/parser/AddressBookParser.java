package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EaddCommand;
import seedu.address.logic.commands.EdeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ElistCommand;
import seedu.address.logic.commands.ElistmCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.PaddCommand;
import seedu.address.logic.commands.TaddCommand;
import seedu.address.logic.commands.TdelCommand;
import seedu.address.logic.commands.TdoneCommand;
import seedu.address.logic.commands.TeditCommand;
import seedu.address.logic.commands.TlistCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case PaddCommand.COMMAND_WORD:
            return new PaddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case EaddCommand.COMMAND_WORD:
            return new EaddCommandParser().parse(arguments);

        case EdeleteCommand.COMMAND_WORD:
            return new EdeleteCommandParser().parse(arguments);

        case ElistCommand.COMMAND_WORD:
            return new ElistCommand();

        case ElistmCommand.COMMAND_WORD:
            return new ElistmCommandParser().parse(arguments);

        case TaddCommand.COMMAND_WORD:
            return new TaddCommandParser().parse(arguments);

        case TdelCommand.COMMAND_WORD:
            return new TdelCommandParser().parse(arguments);

        case TlistCommand.COMMAND_WORD:
            return new TlistCommandParser().parse(arguments);

        case TeditCommand.COMMAND_WORD:
            return new TeditCommandParser().parse(arguments);

        case TdoneCommand.COMMAND_WORD:
            return new TdoneCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
