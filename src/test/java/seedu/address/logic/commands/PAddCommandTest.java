package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.member.Member;
import seedu.address.model.task.Task;
import seedu.address.testutil.MemberBuilder;

public class PAddCommandTest {

    @Test
    public void constructor_nullMember_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PAddCommand(null));
    }

    @Test
    public void execute_memberAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingMemberAdded modelStub = new ModelStubAcceptingMemberAdded();
        Member validMember = new MemberBuilder().build();
        CommandResult commandResult = new PAddCommand(validMember).execute(modelStub);

        assertEquals(String.format(PAddCommand.MESSAGE_SUCCESS, validMember), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validMember), modelStub.membersAdded);
    }

    @Test
    public void execute_duplicateMember_throwsCommandException() {
        Member validMember = new MemberBuilder().build();
        PAddCommand PAddCommand = new PAddCommand(validMember);
        ModelStub modelStub = new ModelStubWithMember(validMember);

        assertThrows(CommandException.class, PAddCommand.MESSAGE_DUPLICATE_MEMBER, () ->
                PAddCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Member alice = new MemberBuilder().withName("Alice").build();
        Member bob = new MemberBuilder().withName("Bob").build();
        PAddCommand addAliceCommand = new PAddCommand(alice);
        PAddCommand addBobCommand = new PAddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        PAddCommand addAliceCommandCopy = new PAddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different member -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addMember(Member member) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasMember(Member member) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteMember(Member target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMember(Member target, Member editedMember) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Member> getFilteredMemberList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredMemberList(Predicate<Member> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void loadTaskList(Member member) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void hasTask(Member member, Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTask(Member member, Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTask(Member member, Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTask(Member member, Task target, Task editedTask) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Task> getFilteredTaskList(Member member) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Member member, Predicate<Task> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single member.
     */
    private class ModelStubWithMember extends ModelStub {
        private final Member member;

        ModelStubWithMember(Member member) {
            requireNonNull(member);
            this.member = member;
        }

        @Override
        public boolean hasMember(Member member) {
            requireNonNull(member);
            return this.member.isSameMember(member);
        }
    }

    /**
     * A Model stub that always accept the member being added.
     */
    private class ModelStubAcceptingMemberAdded extends ModelStub {
        final ArrayList<Member> membersAdded = new ArrayList<>();

        @Override
        public boolean hasMember(Member member) {
            requireNonNull(member);
            return membersAdded.stream().anyMatch(member::isSameMember);
        }

        @Override
        public void addMember(Member member) {
            requireNonNull(member);
            membersAdded.add(member);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
