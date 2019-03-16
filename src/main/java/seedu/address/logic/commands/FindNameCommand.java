package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.CustomerModel;
import seedu.address.model.customer.NameContainsKeywordsPredicate;

/**
 * Finds and lists all customers in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindNameCommand extends CustomerCommand {

    public static final String COMMAND_ALIAS = "fn";
    public static final String COMMAND_WORD = "findname";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all customers whose names contain any of "
        + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
        + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
        + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public FindNameCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(CustomerModel model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredCustomerList(predicate);
        return new CommandResult(
            String.format(Messages.MESSAGE_CUSTOMERS_LISTED_OVERVIEW, model.getFilteredCustomerList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof FindNameCommand // instanceof handles nulls
            && predicate.equals(((FindNameCommand) other).predicate)); // state check
    }
}