package com.example.laboratory.user.editor;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserEditor {
    private final String nickname;
    private final String password;
    private final LocalDate birthday;

    public UserEditor(String nickname, String password, LocalDate birthday) {
        this.nickname = nickname;
        this.password = password;
        this.birthday = birthday;
    }

    public static UserEditorBuilder builder() {
        return new UserEditorBuilder();
    }

    public static class UserEditorBuilder {
        private String nickname;
        private String password;
        private LocalDate birthday;

        UserEditorBuilder() {}

        public UserEditorBuilder nickname(final String nickname) {
            if(nickname != null) {
                this.nickname = nickname;
            }
            return this;
        }

        public UserEditorBuilder password(final String password) {
            if(password != null) {
                this.password = password;
            }
            return this;
        }

        public UserEditorBuilder birthday(final LocalDate birthday) {
            if(birthday != null) {
                this.birthday = birthday;
            }
            return this;
        }

        public UserEditor build() {
            return new UserEditor(this.nickname, this.password, this.birthday);
        }

        @Override
        public String toString() {
            return "UserEditor.UserEditorBuilder{" +
                    "nickname='" + this.nickname + '\'' +
                    ", password='" + this.password + '\'' +
                    ", birthday=" + this.birthday +
                    '}';
        }
    }
}
