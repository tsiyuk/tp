package seedu.address.testutil;

import seedu.address.logic.commands.event.EeditCommand;
import seedu.address.model.module.Name;
import seedu.address.model.module.event.Event;
import seedu.address.model.module.event.EventDate;

/**
 * A utility class to help with building EditEventDescriptor objects.
 */
public class EditEventDescriptorBuilder {

    private EeditCommand.EditEventDescriptor descriptor;

    public EditEventDescriptorBuilder() {
        descriptor = new EeditCommand.EditEventDescriptor();
    }

    public EditEventDescriptorBuilder(EeditCommand.EditEventDescriptor descriptor) {
        this.descriptor = new EeditCommand.EditEventDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditEventDescriptor} with fields containing {@code event}'s details
     */
    public EditEventDescriptorBuilder(Event event) {
        descriptor = new EeditCommand.EditEventDescriptor();
        descriptor.setName(event.getName());
        descriptor.setDate(event.getDate());
    }

    /**
     * Sets the {@code Name} of the {@code EditEventDescriptor} that we are building.
     */
    public EditEventDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Parses the {@code EventDate} of the {@code EditEventDescriptor} that we are building.
     */
    public EditEventDescriptorBuilder withDate(String date) {
        descriptor.setDate(new EventDate(date));
        return this;
    }

    public EeditCommand.EditEventDescriptor build() {
        return descriptor;
    }
}
