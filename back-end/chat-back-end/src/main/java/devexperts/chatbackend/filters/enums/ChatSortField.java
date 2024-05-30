package devexperts.chatbackend.filters.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatSortField {

    ID("id"),
    NAME("name");

    private final String databaseFieldName;
}
