package seedu.address.logic.parser.member;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_POSITION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.POSITION_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.POSITION_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSITION_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSITION_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.member.MeditCommand;
import seedu.address.logic.commands.member.MeditCommand.EditMemberDescriptor;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.module.Name;
import seedu.address.model.module.member.Address;
import seedu.address.model.module.member.Email;
import seedu.address.model.module.member.Phone;
import seedu.address.model.module.member.position.Position;
import seedu.address.testutil.EditMemberDescriptorBuilder;

public class MeditCommandParserTest {

    private static final String POSITION_EMPTY = " " + PREFIX_POSITION;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeditCommand.MESSAGE_USAGE);

    private MeditCommandParser parser = new MeditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, CliSyntax.PREFIX_NAME + VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, " " + CliSyntax.PREFIX_MEMBER_INDEX + "1", MeditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        String userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + "1";
        assertParseFailure(parser, userInput + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, userInput + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, userInput + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, userInput + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, userInput + INVALID_POSITION_DESC, Position.MESSAGE_CONSTRAINTS); // invalid position

        // invalid phone followed by valid email
        assertParseFailure(parser, userInput + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, userInput + PHONE_DESC_BOB + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_POSITION} alone will reset the positions of the {@code Member} being edited,
        // parsing it together with a valid position results in error
        assertParseFailure(parser, userInput + POSITION_DESC_FRIEND + POSITION_DESC_HUSBAND + POSITION_EMPTY,
                Position.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, userInput + POSITION_DESC_FRIEND + POSITION_EMPTY + POSITION_DESC_HUSBAND,
                Position.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, userInput + POSITION_EMPTY + POSITION_DESC_FRIEND + POSITION_DESC_HUSBAND,
                Position.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, userInput + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY
                        + VALID_PHONE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND;
        String userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + PHONE_DESC_BOB
                + POSITION_DESC_HUSBAND + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + POSITION_DESC_FRIEND;

        EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withPositions(VALID_POSITION_HUSBAND, VALID_POSITION_FRIEND).build();
        MeditCommand expectedCommand = new MeditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST;
        String userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + PHONE_DESC_BOB
                + EMAIL_DESC_AMY;

        EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        MeditCommand expectedCommand = new MeditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD;
        String userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + NAME_DESC_AMY;
        EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder().withName(VALID_NAME_AMY).build();
        MeditCommand expectedCommand = new MeditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditMemberDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new MeditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditMemberDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new MeditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditMemberDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new MeditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // positions
        userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + POSITION_DESC_FRIEND;
        descriptor = new EditMemberDescriptorBuilder().withPositions(VALID_POSITION_FRIEND).build();

        expectedCommand = new MeditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST;
        String userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + PHONE_DESC_AMY
                + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + POSITION_DESC_FRIEND + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + POSITION_DESC_FRIEND
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + POSITION_DESC_HUSBAND;

        EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withPositions(VALID_POSITION_FRIEND, VALID_POSITION_HUSBAND)
                .build();
        MeditCommand expectedCommand = new MeditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST;
        String userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + INVALID_PHONE_DESC
                + PHONE_DESC_BOB;
        EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder().withPhone(VALID_PHONE_BOB).build();
        MeditCommand expectedCommand = new MeditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + EMAIL_DESC_BOB
                + INVALID_PHONE_DESC + ADDRESS_DESC_BOB + PHONE_DESC_BOB;
        descriptor = new EditMemberDescriptorBuilder().withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).build();
        expectedCommand = new MeditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetPositions_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = " " + CliSyntax.PREFIX_MEMBER_INDEX + targetIndex.getOneBased() + POSITION_EMPTY;

        EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder().withPositions().build();
        MeditCommand expectedCommand = new MeditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}