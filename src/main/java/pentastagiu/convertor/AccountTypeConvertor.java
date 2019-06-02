package pentastagiu.convertor;

import pentastagiu.util.AccountType;

import java.beans.PropertyEditorSupport;

public class AccountTypeConvertor extends PropertyEditorSupport {
    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(AccountType.fromString(text));
    }
}
