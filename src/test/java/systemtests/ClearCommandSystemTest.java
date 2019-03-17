package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalCustomers.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCustomerCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.CustomerManager;
import seedu.address.model.CustomerModel;

public class ClearCommandSystemTest extends AddressBookSystemTest {
    @Test
    public void clear() {
        final CustomerModel defaultModel = getModel();

        /* Case: clear non-empty address book, command with leading spaces and trailing alphanumeric characters
        and
         * spaces -> cleared
         */
        assertCommandSuccess("   " + ClearCustomerCommand.COMMAND_WORD + " ab12   ");
        assertSelectedCardUnchanged();

        /* Case: undo clearing address book -> original address book restored */
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, defaultModel);
        assertSelectedCardUnchanged();

        /* Case: redo clearing address book -> cleared */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, new CustomerManager());
        assertSelectedCardUnchanged();

        /* Case: selects first card in customer list and clears address book -> cleared and no card selected */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original address book
        selectCustomer(Index.fromOneBased(1));
        assertCommandSuccess(ClearCustomerCommand.COMMAND_WORD);
        assertSelectedCardDeselected();

        /* Case: filters the customer list before clearing -> entire address book cleared */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original address book
        showCustomersWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(ClearCustomerCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: clear empty address book -> cleared */
        assertCommandSuccess(ClearCustomerCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ClEaR", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ClearCustomerCommand#MESSAGE_SUCCESS} and the model related components equal to an empty
     * model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class and the status bar's sync status changes.
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, CustomerModel)
     */
    private void assertCommandSuccess(String command) {
        assertCommandSuccess(command, ClearCustomerCommand.MESSAGE_SUCCESS, new CustomerManager());
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     *
     * @see ClearCommandSystemTest#assertCommandSuccess(String)
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, CustomerModel expectedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, CustomerModel)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        CustomerModel expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
