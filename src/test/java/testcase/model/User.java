/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package testcase.model;

import java.util.*;

public class User {
    private String name;
    private int status;
    private Date birthday;
    private boolean locked;
    private List<Book> bookList;

    public static User newInstance() {
        User user = new User();
        List<Book> bookList = new ArrayList<Book>();
        bookList.add(new Book("JavaBook", 10.99, user));
        bookList.add(new Book("DotNetBook", 99.00, user));
        bookList.add(new Book("JavaScriptBook", 20.10, user));

        user.name = "Jack";
        user.status = 0;
        user.birthday = new Date();
        user.locked = false;
        user.bookList = bookList;
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Book find(String name) {
        return new Book(name, 99.99, this);
    }

    public Book notFind(String name) {
        return null;
    }

    public Book getNone() {
        return null;
    }
}
